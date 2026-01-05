package net.typho.big_shot_lib.spirv

import net.minecraft.util.Mth
import java.nio.ByteBuffer
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

        const val OP_FUNCTION = 54

        const val OP_VARIABLE = 59
        const val OP_LOAD = 61
        const val OP_STORE = 62

        const val OP_DECORATE = 71

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
                this.bytes += Byte.SIZE_BYTES
                tokens.add { buffer -> buffer.put(bytes) }
            }
            return this
        }

        fun putBytes(bytes: ByteBuffer?): Builder {
            bytes?.let {
                this.bytes += Byte.SIZE_BYTES
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
                bytes += Short.SIZE_BYTES
                tokens.add { buffer -> buffer.asShortBuffer().put(shorts) }
            }
            return this
        }

        fun putShorts(shorts: ShortBuffer?): Builder {
            shorts?.let {
                bytes += Short.SIZE_BYTES
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
                bytes += Int.SIZE_BYTES
                tokens.add { buffer -> buffer.asIntBuffer().put(ints) }
            }
            return this
        }

        fun putInts(ints: IntBuffer?): Builder {
            ints?.let {
                bytes += Int.SIZE_BYTES
                tokens.add { buffer -> buffer.asIntBuffer().put(ints) }
            }
            return this
        }

        fun putString(string: String?): Builder {
            string?.let {
                val length = Mth.positiveCeilDiv(string.length, 4) + 1
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