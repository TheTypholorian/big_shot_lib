package net.typho.big_shot_lib.spirv

import net.typho.big_shot_lib.BigShotLib
import net.typho.big_shot_lib.spirv.at.At
import net.typho.big_shot_lib.spirv.at.AtOpcode
import net.typho.big_shot_lib.spirv.at.BeforeFirstFunction
import net.typho.big_shot_lib.spirv.vars.ShaderVariableType
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ShaderMixinContext(var code: ByteBuffer) : Iterable<Opcode> {
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

        inject(BeforeFirstFunction, buffer)

        if (name != null) {
            val bytes = name.toByteArray()
            val words = Math.ceilDiv(bytes.size, WORD_SIZE_BYTES) + 3

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
            if (opcode.id == 15) { // OpEntryPoint
                code.putInt(
                    opcode.index * WORD_SIZE_BYTES,
                    ((opcode.length + ids.size) shl 16) or opcode.id
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

    fun locateVariable(
        name: String? = null,
        location: Int? = null,
        type: ShaderVariableType? = null
    ): Int? {
        mainLoop@
        for (opcode in this) {
            if (opcode.id == 59) { // OpVariable
                val id = code.getInt((opcode.index + 2) * WORD_SIZE_BYTES)

                if (name != null) {
                    var found = false

                    for (opcode1 in this) {
                        if (opcode1.id == 5) { // OpName
                            if (code.getInt((opcode1.index + 1) * WORD_SIZE_BYTES) == id) {
                                val contents = getOpcodeData(opcode1)
                                val contentsArray = contents.array()
                                val actualName = String(
                                    contentsArray,
                                    WORD_SIZE_BYTES,
                                    contentsArray.size - 2 * WORD_SIZE_BYTES
                                ).trim(0.toChar())

                                if (name == actualName) {
                                    found = true
                                    break
                                }
                            }
                        }
                    }

                    if (!found) {
                        continue@mainLoop
                    }
                }

                if (location != null) {
                    var found = false

                    for (opcode1 in this) {
                        if (opcode1.id == 71) { // OpDecorate
                            if (code.getInt((opcode.index + 1) * WORD_SIZE_BYTES) == id) {
                                val decoration = code.getInt((opcode.index + 2) * WORD_SIZE_BYTES)

                                if (decoration == 30) { // Location
                                    val actualLocation = code.getInt((opcode.index + 3) * WORD_SIZE_BYTES)

                                    if (actualLocation == location) {
                                        found = true
                                        break
                                    }
                                }
                            }
                        }
                    }

                    if (!found) {
                        continue@mainLoop
                    }
                }

                if (type != null) {
                    var found = false

                    for (opcode1 in this) {
                        if (type.matches(opcode1, this)) {
                            val target = code.getInt((opcode.index + 1) * WORD_SIZE_BYTES)

                            if (target == id) {
                                found = true
                                break
                            }
                        }
                    }

                    if (!found) {
                        continue@mainLoop
                    }
                }

                return id
            }
        }

        return null
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
}