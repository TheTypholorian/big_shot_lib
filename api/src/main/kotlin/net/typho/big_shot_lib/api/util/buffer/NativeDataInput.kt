package net.typho.big_shot_lib.api.util.buffer

import java.nio.ByteOrder

interface NativeDataInput {
    var byteOrder: ByteOrder
        get() = ByteOrder.nativeOrder()
        set(value) {
            throw UnsupportedOperationException("Set byte order of NativeDataInput $this")
        }

    fun withByteOrder(order: ByteOrder): NativeDataInput = object : Delegate(this) {
        override var byteOrder: ByteOrder = order
    }

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
        val v = readUShort()
        return if (byteOrder == ByteOrder.LITTLE_ENDIAN) v else java.lang.Short.reverseBytes(v.toShort()).toInt()
    }

    fun readPacked4x1(): Int {
        val v = readInt()
        return if (byteOrder == ByteOrder.LITTLE_ENDIAN) v else Integer.reverseBytes(v)
    }

    fun readPacked2x2(): Int {
        val v = readInt()
        return if (byteOrder == ByteOrder.LITTLE_ENDIAN) v else {
            (v shl 16) or (v ushr 16)
        }
    }

    fun readPacked8x1(): Long {
        val v = readLong()
        return if (byteOrder == ByteOrder.LITTLE_ENDIAN) v else java.lang.Long.reverseBytes(v)
    }

    fun readPacked4x2(): Long {
        val v = readLong()
        return if (byteOrder == ByteOrder.LITTLE_ENDIAN) v else {
            ((v and 0xFFFF) shl 48) or ((v and 0xFFFF_0000) shl 16) or ((v and 0xFFFF_0000_0000) ushr 16) or (v ushr 48)
        }
    }

    fun readPacked2x4(): Long {
        val v = readLong()
        return if (byteOrder == ByteOrder.LITTLE_ENDIAN) v else {
            (v shl 32) or (v ushr 32)
        }
    }

    fun readTo(output: NativeDataOutput, bytes: Int) {
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
        protected val delegate: NativeDataInput
    ) : NativeDataInput {
        override fun skip(bytes: Long) {
            delegate.skip(bytes)
        }

        override fun bytesLeft(): Long {
            return delegate.bytesLeft()
        }

        override fun readByte(): Byte {
            return delegate.readByte()
        }

        override fun readUByte(): Int {
            return delegate.readUByte()
        }

        override fun readShort(): Short {
            return delegate.readShort()
        }

        override fun readUShort(): Int {
            return delegate.readUShort()
        }

        override fun readInt(): Int {
            return delegate.readInt()
        }

        override fun readLong(): Long {
            return delegate.readLong()
        }

        override fun readFloat(): Float {
            return delegate.readFloat()
        }

        override fun readDouble(): Double {
            return delegate.readDouble()
        }

        override fun readBoolean(): Boolean {
            return delegate.readBoolean()
        }
    }
}