package net.typho.big_shot_lib.spirv

import net.typho.big_shot_lib.BigShotLib
import net.typho.big_shot_lib.error.ShaderMixinException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

class ShaderMixinContext : Iterable<ShaderMixinContext.Opcode> {
    val code = LinkedList<ByteBuffer>()
    var headerSize = 5
    var bound: Int = 0

    fun loadBound() {
        bound = code[0].getInt(3 * WORD_SIZE_BYTES)
    }

    fun putBound() {
        if (bound <= 0) {
            BigShotLib.LOGGER.warn("Shader Mixin Context bound $bound is a suspicious value, did you remember to call loadBound() after initializing?")
        }

        code[0].putInt(3 * WORD_SIZE_BYTES, bound)
    }

    fun split(index: Int): Int? {
        var listIndex = 0
        var sectionStart = 0
        var sectionEnd = 0

        for (section in code) {
            sectionEnd += section.capacity()

            if (index == sectionStart) {
                return listIndex
            } else if (index > sectionStart && index < sectionEnd) {
                val lowSection = ByteBuffer.allocate(index - sectionStart).order(BYTE_ORDER)
                lowSection.put(0, section, 0, lowSection.capacity())

                val highSection = ByteBuffer.allocate(sectionEnd - index).order(BYTE_ORDER)
                highSection.put(0, section, lowSection.capacity(), highSection.capacity())

                code.removeAt(listIndex)
                code.add(listIndex, lowSection)
                code.add(listIndex + 1, highSection)

                return listIndex + 1
            }

            sectionStart = sectionEnd
            listIndex++
        }

        return null
    }

    fun inject(index: Int, inject: ByteBuffer) {
        code.add(split(index)!!, inject.order(BYTE_ORDER))
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

    fun compile(): ByteBuffer {
        if (code.isEmpty()) {
            val buffer = ByteBuffer.allocate(0).order(BYTE_ORDER)
            code.add(buffer)
            return buffer
        }

        if (code.size == 1) {
            putBound()
            return code[0]
        }

        var size = 0

        for (bytes in code) {
            size += bytes.capacity()
        }

        val array = ByteBuffer.allocate(size).order(BYTE_ORDER)
        var index = 0

        for (bytes in code) {
            array.put(index, bytes, 0, bytes.capacity())
            index += bytes.capacity()
        }

        code.clear()
        code.add(array)

        putBound()

        return array
    }

    fun getOpcodeData(op: Opcode): ByteBuffer {
        val code = compile()
        val buf = ByteBuffer.allocate(op.length * WORD_SIZE_BYTES)
        buf.put(0, code, (op.index + 1) * WORD_SIZE_BYTES, buf.capacity())
        return buf
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun iterator(): Iterator<Opcode> {
        return object : Iterator<Opcode> {
            val code = compile()
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