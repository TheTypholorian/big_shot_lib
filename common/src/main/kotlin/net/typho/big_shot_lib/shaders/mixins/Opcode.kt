package net.typho.big_shot_lib.shaders.mixins

import net.minecraft.util.Mth
import java.nio.ByteBuffer
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.nio.ShortBuffer
import java.util.*
import java.util.function.Consumer

data class Opcode(val index: Int, val id: Int, val length: Int) {
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

    class Builder(val id: Int) {
        val tokens = LinkedList<Consumer<ByteBuffer>>()
        var bytes = 0

        fun build(): ByteBuffer {
            if (!Mth.isMultipleOf(bytes, ShaderMixinContext.WORD_SIZE_BYTES)) {
                throw IllegalStateException("Cannot create an opcode with size (in bytes) $bytes, must be a multiple of ${ShaderMixinContext.WORD_SIZE_BYTES}")
            }

            val words = bytes / ShaderMixinContext.WORD_SIZE_BYTES + 1

            val buffer = ByteBuffer.allocate(words * ShaderMixinContext.WORD_SIZE_BYTES)
                .order(ShaderMixinContext.BYTE_ORDER)

            buffer.putInt(id or (words shl 16))

            for (token in tokens) {
                token.accept(buffer)
            }

            return buffer
        }

        fun putByte(byte: Byte?): Builder {
            byte?.let {
                bytes += Byte.SIZE_BYTES
                tokens.add { buffer -> buffer.put(byte) }
            }
            return this
        }

        fun putBytes(bytes: ByteArray?): Builder {
            bytes?.let {
                this.bytes += bytes.size * Byte.SIZE_BYTES
                tokens.add { buffer -> buffer.put(bytes) }
            }
            return this
        }

        fun putBytes(bytes: ByteBuffer?): Builder {
            bytes?.let {
                this.bytes += bytes.capacity() * Byte.SIZE_BYTES
                tokens.add { buffer -> buffer.put(bytes) }
            }
            return this
        }

        fun putShort(short: Short?): Builder {
            short?.let {
                bytes += Short.SIZE_BYTES
                tokens.add { buffer -> buffer.putShort(short) }
            }
            return this
        }

        fun putShorts(shorts: ShortArray?): Builder {
            shorts?.let {
                bytes += shorts.size * Short.SIZE_BYTES
                tokens.add { buffer -> buffer.asShortBuffer().put(shorts) }
            }
            return this
        }

        fun putShorts(shorts: ShortBuffer?): Builder {
            shorts?.let {
                bytes += shorts.capacity() * Short.SIZE_BYTES
                tokens.add { buffer -> buffer.asShortBuffer().put(shorts) }
            }
            return this
        }

        fun putInt(int: Int?): Builder {
            int?.let {
                bytes += Int.SIZE_BYTES
                tokens.add { buffer -> buffer.putInt(int) }
            }
            return this
        }

        fun putInts(ints: IntArray?): Builder {
            ints?.let {
                bytes += ints.size * Int.SIZE_BYTES
                tokens.add { buffer -> buffer.asIntBuffer().put(ints) }
            }
            return this
        }

        fun putInts(ints: IntBuffer?): Builder {
            ints?.let {
                bytes += ints.capacity() * Int.SIZE_BYTES
                tokens.add { buffer -> buffer.asIntBuffer().put(ints) }
            }
            return this
        }

        fun putFloat(float: Float?): Builder {
            float?.let {
                bytes += Float.SIZE_BYTES
                tokens.add { buffer -> buffer.putFloat(float) }
            }
            return this
        }

        fun putFloats(floats: FloatArray?): Builder {
            floats?.let {
                bytes += floats.size * Float.SIZE_BYTES
                tokens.add { buffer -> buffer.asFloatBuffer().put(floats) }
            }
            return this
        }

        fun putFloats(floats: FloatBuffer?): Builder {
            floats?.let {
                bytes += floats.capacity() * Float.SIZE_BYTES
                tokens.add { buffer -> buffer.asFloatBuffer().put(floats) }
            }
            return this
        }

        fun putString(string: String?): Builder {
            string?.let {
                val length = (Mth.positiveCeilDiv(string.length, ShaderMixinContext.WORD_SIZE_BYTES) + 1) * ShaderMixinContext.WORD_SIZE_BYTES
                bytes += length

                tokens.add { buffer ->
                    val start = buffer.position()
                    buffer.put(string.toByteArray())
                    buffer.position(start + length)
                }
            }
            return this
        }
    }
}