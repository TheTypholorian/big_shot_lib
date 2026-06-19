package net.typho.big_shot_lib.api.util.buffer

import net.typho.big_shot_lib.api.util.platform.PlatformUtil
import java.nio.ByteOrder

interface NativeDataOutput {
    companion object {
        @JvmStatic
        @get:JvmName("areChecksEnabled")
        @set:JvmName("setChecksEnabled")
        var CHECKS = PlatformUtil.INSTANCE.isDevEnv()

        @JvmStatic
        fun checkIsByte(v: Int) {
            if (((v and 0xFF.inv()) != 0)) {
                throw IllegalArgumentException("Illegal non-byte value ${v.toHexString()}")
            }
        }

        @JvmStatic
        fun checkIsShort(v: Int) {
            if (((v and 0xFFFF.inv()) != 0)) {
                throw IllegalArgumentException("Illegal non-short value ${v.toHexString()}")
            }
        }

        @JvmStatic
        fun checkIsInt(v: Long) {
            if (((v and 0xFFFFFFFFL.inv()) != 0L)) {
                throw IllegalArgumentException("Illegal non-int value ${v.toHexString()}")
            }
        }
    }

    var byteOrder: ByteOrder
        get() = ByteOrder.nativeOrder()
        set(value) {
            throw UnsupportedOperationException("Set byte order of NativeDataOutput $this")
        }

    fun withByteOrder(order: ByteOrder): NativeDataOutput = object : Delegate(this) {
        override var byteOrder: ByteOrder = order
    }

    fun skip(bytes: Long)

    fun writeByte(v: Int)

    fun writeShort(v: Int)

    fun writeInt(v: Int)

    fun writeLong(v: Long)

    fun writeFloat(v: Float)

    fun writeDouble(v: Double)

    fun writeBoolean(v: Boolean)

    fun write2x1(a: Int, b: Int) {
        if (CHECKS) {
            checkIsByte(a)
            checkIsByte(b)
        }

        if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
            writeShort((a shl 8) or b)
        } else {
            writeShort((b shl 8) or a)
        }
    }

    fun write4x1(a: Int, b: Int, c: Int, d: Int) {
        if (CHECKS) {
            checkIsByte(a)
            checkIsByte(b)
            checkIsByte(c)
            checkIsByte(d)
        }

        if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
            writeInt((a shl 24) or (b shl 16) or (c shl 8) or d)
        } else {
            writeInt((d shl 24) or (c shl 16) or (b shl 8) or a)
        }
    }

    fun write2x2(a: Int, b: Int) {
        if (CHECKS) {
            checkIsShort(a)
            checkIsShort(b)
        }

        if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
            writeInt((a shl 16) or b)
        } else {
            writeInt((b shl 16) or a)
        }
    }

    fun write8x1(a: Int, b: Int, c: Int, d: Int, e: Int, f: Int, g: Int, h: Int) {
        if (CHECKS) {
            checkIsByte(a)
            checkIsByte(b)
            checkIsByte(c)
            checkIsByte(d)
            checkIsByte(e)
            checkIsByte(f)
            checkIsByte(g)
            checkIsByte(h)
        }

        if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
            writeLong((a.toLong() shl 56) or (b.toLong() shl 48) or (c.toLong() shl 40) or (d.toLong() shl 36) (e.toLong() shl 24) or (f.toLong() shl 16) or (g.toLong() shl 8) or h.toLong())
        } else {
            writeLong((h.toLong() shl 56) or (g.toLong() shl 48) or (f.toLong() shl 40) or (e.toLong() shl 36) (d.toLong() shl 24) or (c.toLong() shl 16) or (b.toLong() shl 8) or a.toLong())
        }
    }

    fun write4x2(a: Int, b: Int, c: Int, d: Int) {
        if (CHECKS) {
            checkIsShort(a)
            checkIsShort(b)
            checkIsShort(c)
            checkIsShort(d)
        }

        if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
            writeInt((a shl 48) or (b shl 32) or (c shl 16) or d)
        } else {
            writeInt((d shl 48) or (c shl 32) or (b shl 16) or a)
        }
    }

    fun write2x4(a: Int, b: Int) {
        if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
            writeLong((a.toLong() shl 32) or b.toLong())
        } else {
            writeLong((b.toLong() shl 32) or a.toLong())
        }
    }

    fun writePacked2x1(v: Int) {
        writeShort(if (byteOrder == ByteOrder.LITTLE_ENDIAN) v else java.lang.Short.reverseBytes(v.toShort()).toInt())
    }

    fun writePacked4x1(v: Int) {
        writeInt(if (byteOrder == ByteOrder.LITTLE_ENDIAN) v else Integer.reverseBytes(v))
    }

    fun writePacked2x2(v: Int) {
        writeInt(if (byteOrder == ByteOrder.LITTLE_ENDIAN) v else {
            (v shl 16) or (v ushr 16)
        })
    }

    fun writePacked8x1(v: Long) {
        writeLong(if (byteOrder == ByteOrder.LITTLE_ENDIAN) v else java.lang.Long.reverseBytes(v))
    }

    fun writePacked4x2(v: Long) {
        writeLong(if (byteOrder == ByteOrder.LITTLE_ENDIAN) v else {
            ((v and 0xFFFF) shl 48) or ((v and 0xFFFF_0000) shl 16) or ((v and 0xFFFF_0000_0000) ushr 16) or (v ushr 48)
        })
    }

    fun writePacked2x4(v: Long) {
        writeLong(if (byteOrder == ByteOrder.LITTLE_ENDIAN) v else {
            (v shl 32) or (v ushr 32)
        })
    }

    open class Delegate(
        @JvmField
        protected val delegate: NativeDataOutput
    ) : NativeDataOutput {
        override fun skip(bytes: Long) {
            delegate.skip(bytes)
        }

        override fun writeByte(v: Int) {
            delegate.writeByte(v)
        }

        override fun writeShort(v: Int) {
            delegate.writeByte(v)
        }

        override fun writeInt(v: Int) {
            delegate.writeInt(v)
        }

        override fun writeLong(v: Long) {
            delegate.writeLong(v)
        }

        override fun writeFloat(v: Float) {
            delegate.writeFloat(v)
        }

        override fun writeDouble(v: Double) {
            delegate.writeDouble(v)
        }

        override fun writeBoolean(v: Boolean) {
            delegate.writeBoolean(v)
        }
    }
}