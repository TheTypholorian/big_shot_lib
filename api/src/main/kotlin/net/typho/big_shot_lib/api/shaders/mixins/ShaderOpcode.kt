package net.typho.big_shot_lib.api.shaders.mixins

import org.lwjgl.system.MemoryUtil.memByteBuffer
import java.nio.ByteBuffer
import java.nio.IntBuffer
import java.util.*
import java.util.function.Consumer

@JvmRecord
data class ShaderOpcode(
    @JvmField
    val parent: ShaderBytecodeBuffer,
    @JvmField
    val index: Int,
    @JvmField
    val id: Int,
    @JvmField
    val length: Int
) {
    companion object {
        const val OP_NAME = 5

        const val OP_ENTRY_POINT = 15

        const val OP_TYPE_VOID = 19
        const val OP_TYPE_BOOL = 20
        const val OP_TYPE_INT = 21
        const val OP_TYPE_FLOAT = 22
        const val OP_TYPE_VECTOR = 23
        const val OP_TYPE_MATRIX = 24
        const val OP_TYPE_POINTER = 32
        const val OP_TYPE_FUNCTION = 33

        const val OP_CONSTANT = 43

        const val OP_FUNCTION = 54

        const val OP_VARIABLE = 59
        const val OP_LOAD = 61
        const val OP_STORE = 62

        const val OP_DECORATE = 71

        const val OP_IMAGE_SAMPLE_IMPLICIT_LOD = 87

        const val OP_S_NEGATE = 126
        const val OP_F_NEGATE = 127
        const val OP_I_ADD = 128
        const val OP_F_ADD = 129
        const val OP_I_SUB = 130
        const val OP_F_SUB = 131
        const val OP_I_MUL = 132
        const val OP_F_MUL = 133
        const val OP_U_DIV = 134
        const val OP_S_DIV = 135
        const val OP_F_DIV = 136
        const val OP_U_MOD = 137
        const val OP_S_REM = 138
        const val OP_S_MOD = 139
        const val OP_F_REM = 140
        const val OP_F_MOD = 141

        const val OP_RETURN = 253
        const val OP_RETURN_VALUE = 254
    }

    fun getWord(offset: Int) = parent.buffer.get(index + 1 + offset)

    fun putWord(offset: Int, word: Int) {
        parent.buffer.put(index + 1 + offset, word)
    }

    fun getString(offset: Int): String {
        val array = ByteArray(length - offset)
        memByteBuffer(parent.buffer).get(index + 1 + offset, array)
        return String(array).trim(0.toChar())
    }

    class Builder(val id: Int) {
        val tokens = LinkedList<Consumer<ByteBuffer>>()
        var words = 1

        fun build(): IntBuffer {
            val buffer = ByteBuffer.allocate(words * ShaderMixinManager.WORD_SIZE_BYTES)
                .order(ShaderMixinManager.BYTE_ORDER)

            buffer.putInt(id or (words shl 16))

            for (token in tokens) {
                token.accept(buffer)
            }

            return buffer.asIntBuffer()
        }

        fun putFloat(float: Float?): Builder {
            float?.let {
                words++
                tokens.add { buffer -> buffer.putFloat(float) }
            }
            return this
        }

        fun putWord(int: Int?): Builder {
            int?.let {
                words++
                tokens.add { buffer -> buffer.putInt(int) }
            }
            return this
        }

        fun putWords(ints: IntArray?): Builder {
            ints?.let {
                words += ints.size
                tokens.add { buffer -> buffer.asIntBuffer().put(ints) }
            }
            return this
        }

        fun putWords(ints: IntBuffer?): Builder {
            ints?.let {
                words += ints.capacity()
                tokens.add { buffer -> buffer.asIntBuffer().put(ints) }
            }
            return this
        }

        fun putString(string: String?): Builder {
            string?.let {
                val length = Math.ceilDiv(string.length, ShaderMixinManager.WORD_SIZE_BYTES) + 1
                words += length

                tokens.add { buffer ->
                    val start = buffer.position()
                    buffer.put(string.toByteArray())
                    buffer.position(start + length * ShaderMixinManager.WORD_SIZE_BYTES)
                }
            }
            return this
        }
    }
}