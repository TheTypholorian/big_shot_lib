package net.typho.big_shot_lib.spirv

import net.typho.big_shot_lib.BigShotLib
import net.typho.big_shot_lib.error.ShaderMixinException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ShaderMixinContext(var code: ByteBuffer) : Iterable<ShaderMixinContext.Opcode> {
    var headerSize = 5
    var bound: Int = 0

    init {
        code.order(BYTE_ORDER)
        loadBound()
    }

    fun loadBound() {
        bound = code.getInt(3 * WORD_SIZE_BYTES)
    }

    fun putBound() {
        if (bound <= 0) {
            BigShotLib.LOGGER.warn("Shader Mixin Context bound $bound is a suspicious value, did you remember to call loadBound() after initializing?")
        }

        code.putInt(3 * WORD_SIZE_BYTES, bound)
    }

    fun split(index: Int, size: Int) {
        val newBuffer = ByteBuffer.allocate(code.capacity() + size).order(BYTE_ORDER)

        if (index == 0) {
            newBuffer.put(size, code, 0, code.capacity())
        } else if (index > 0 && index < code.capacity()) {
            newBuffer.put(0, code, 0, index)
            newBuffer.put(index + size, code, index, code.capacity() - index)
        } else {
            newBuffer.put(0, code, 0, code.capacity())
        }

        code = newBuffer
    }

    fun inject(index: Int, inject: ByteBuffer) {
        inject.order(BYTE_ORDER)
        split(index, inject.capacity())
        code.put(index, inject, 0, inject.capacity())
    }

    fun inject(at: At, inject: ByteBuffer) = inject(at.getStart(this) * WORD_SIZE_BYTES, inject)

    fun addIntType(bits: Int = 32, signed: Boolean): Int {
        val resultId = bound++

        val buffer = ByteBuffer.allocate(4 * WORD_SIZE_BYTES)
            .order(BYTE_ORDER)

            .putInt(0x00_04_00_15)
            .putInt(resultId)
            .putInt(bits)
            .putInt(if (signed) 1 else 0)

        inject(BeforeFirstFunction(), buffer)
        putBound()
        return resultId
    }

    fun addFloatType(bits: Int = 32): Int {
        val resultId = bound++

        val buffer = ByteBuffer.allocate(3 * WORD_SIZE_BYTES)
            .order(BYTE_ORDER)

            .putInt(0x00_03_00_16)
            .putInt(resultId)
            .putInt(bits)

        inject(BeforeFirstFunction(), buffer)
        putBound()
        return resultId
    }

    fun addVectorType(typePointer: Int, components: Int): Int {
        val resultId = bound++

        val buffer = ByteBuffer.allocate(4 * WORD_SIZE_BYTES)
            .order(BYTE_ORDER)

            .putInt(0x00_04_00_17)
            .putInt(resultId)
            .putInt(typePointer)
            .putInt(components)

        inject(BeforeFirstFunction(), buffer)
        putBound()
        return resultId
    }

    fun addMatrixType(typePointer: Int, columnCount: Int): Int {
        val resultId = bound++

        val buffer = ByteBuffer.allocate(4 * WORD_SIZE_BYTES)
            .order(BYTE_ORDER)

            .putInt(0x00_04_00_18)
            .putInt(resultId)
            .putInt(typePointer)
            .putInt(columnCount)

        inject(BeforeFirstFunction(), buffer)
        putBound()
        return resultId
    }

    fun addSamplerType(): Int {
        val resultId = bound++

        val buffer = ByteBuffer.allocate(2 * WORD_SIZE_BYTES)
            .order(BYTE_ORDER)

            .putInt(0x00_02_00_1A)
            .putInt(resultId)

        inject(BeforeFirstFunction(), buffer)
        putBound()
        return resultId
    }

    fun addStaticVar(storageClass: Int, typePointer: Int, name: String? = null, initializer: Int? = null): Int {
        val typePointerId = bound++
        val variableId = bound++

        val buffer = ByteBuffer.allocate((if (initializer == null) 8 else 9) * WORD_SIZE_BYTES)
            .order(BYTE_ORDER)

            .putInt(0x00_04_00_20)
            .putInt(typePointerId)
            .putInt(storageClass)
            .putInt(typePointer)

            .putInt(if (initializer == null) 0x00_04_00_3B else 0x00_05_00_3B)
            .putInt(typePointerId)
            .putInt(variableId)
            .putInt(storageClass)

        if (initializer != null) {
            buffer.putInt(initializer)
        }

        inject(BeforeFirstFunction(), buffer)

        if (name != null) {
            val bytes = name.toByteArray()
            val words = Math.ceilDiv(bytes.size, WORD_SIZE_BYTES) + 2

            val buffer = ByteBuffer.allocate(words * WORD_SIZE_BYTES)
                .order(BYTE_ORDER)

                .putInt(0x00_05 or (words shl 16))
                .putInt(variableId)
                .put(bytes)

            inject(AtOpcode(5), buffer) // OpName
        }

        putBound()
        return variableId
    }

    fun addEntrypointVars(vararg ids: Int): Boolean {
        for (opcode in this) {
            if (opcode.type == 15) { // OpEntryPoint
                code.putInt(
                    opcode.index * WORD_SIZE_BYTES,
                    ((opcode.length + ids.size) shl 16) or opcode.type
                )
                val buffer = ByteBuffer.allocate(ids.size * WORD_SIZE_BYTES)
                        .order(BYTE_ORDER)
                buffer.asIntBuffer().put(ids)
                inject(
                    (opcode.index + opcode.length) * WORD_SIZE_BYTES,
                    buffer
                )
                return true
            }
        }

        return false
    }

    fun getOpcodeData(op: Opcode): ByteBuffer {
        val buf = ByteBuffer.allocate(op.length * WORD_SIZE_BYTES)
        buf.put(0, code, (op.index + 1) * WORD_SIZE_BYTES, buf.capacity())
        return buf
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun iterator(): Iterator<Opcode> {
        return object : Iterator<Opcode> {
            val code = this@ShaderMixinContext.code
            var index = headerSize

            override fun hasNext() = index * WORD_SIZE_BYTES < code.capacity()

            override fun next(): Opcode {
                val op = code.getInt(index * WORD_SIZE_BYTES)
                val type = op and 0xFFFF
                val length = (op ushr 16) and 0xFFFF
                val opcode = Opcode(index, type, length)

                index += length

                return opcode
            }
        }
    }

    companion object {
        const val WORD_SIZE_BYTES = 4
        val BYTE_ORDER: ByteOrder = ByteOrder.LITTLE_ENDIAN
    }

    class Opcode(val index: Int, val type: Int, val length: Int)

    interface At {
        fun getStart(context: ShaderMixinContext): Int

        fun getEnd(context: ShaderMixinContext): Int? = null
    }

    open class AtOpcode(val id: Int) : At {
        override fun getStart(context: ShaderMixinContext): Int {
            for (opcode in context) {
                if (opcode.type == id) {
                    return opcode.index
                }
            }

            return 0
        }

        override fun getEnd(context: ShaderMixinContext): Int {
            for (opcode in context) {
                if (opcode.type == id) {
                    return opcode.index + opcode.length + 1
                }
            }

            return 0
        }
    }

    open class AtOpcodeInMethod(val id: Int, val method: String): At {
        override fun getStart(context: ShaderMixinContext): Int {
            var methodId: Int? = -1

            for (opcode in context) {
                if (opcode.type == 5) { // OpName
                    val contents = context.getOpcodeData(opcode)
                    val contentsArray = contents.array()
                    val name = String(contentsArray, WORD_SIZE_BYTES, contentsArray.size - 2 * WORD_SIZE_BYTES)

                    if (name.trim(0.toChar()) == method) {
                        methodId = contents.getInt(0)
                        break;
                    }
                }
            }

            if (methodId == -1) {
                throw ShaderMixinException("Unable to find method $method")
            }

            var inMethod = false

            for (opcode in context) {
                if (opcode.type == 54) { // OpFunction
                    if (inMethod) {
                        break
                    }

                    val contents = context.getOpcodeData(opcode)

                    if (contents.getInt(WORD_SIZE_BYTES) == methodId) {
                        inMethod = true
                    }
                } else if (inMethod && opcode.type == id) {
                    return opcode.index
                }
            }

            throw ShaderMixinException("Unable to find opcode $id method $method")
        }
    }

    open class AtVoidReturn(method: String): AtOpcodeInMethod(253, method) // OpReturn

    open class AtReturnValue(method: String): AtOpcodeInMethod(254, method) // OpReturnValue

    open class BeforeFirstFunction(): AtOpcode(54) // OpFunction
}