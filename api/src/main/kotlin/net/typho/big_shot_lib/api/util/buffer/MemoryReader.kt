package net.typho.big_shot_lib.api.util.buffer

interface MemoryReader {
    fun skip(bytes: Long)

    fun bytesLeft(): Long

    fun readByte(): Byte

    fun readUByte(): Int

    fun readShort(): Short

    fun readUShort(): Int

    fun readInt(): Int

    fun readLong(): Long

    fun readFloat(): Float

    fun readDouble(): Double

    fun readBoolean(): Boolean

    fun readPacked2x1(): Int {
        return readUShort()
    }

    fun readPacked4x1(): Int {
        return readInt()
    }

    fun readPacked2x2(): Int {
        return readInt()
    }

    fun readPacked8x1(): Long {
        return readLong()
    }

    fun readPacked4x2(): Long {
        return readLong()
    }

    fun readPacked2x4(): Long {
        return readLong()
    }

    fun readTo(output: MemoryWriter, bytes: Int) {
        repeat(bytes ushr 3) {
            output.writeLong(readLong())
        }

        if (bytes and 0b100 != 0) {
            output.writeInt(readInt())
        }

        if (bytes and 0b10 != 0) {
            output.writeShort(readUShort())
        }

        if (bytes and 0b1 != 0) {
            output.writeByte(readUByte())
        }
    }

    open class Delegate(
        @JvmField
        protected val delegate: MemoryReader
    ) : MemoryReader by delegate
}