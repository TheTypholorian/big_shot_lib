package net.typho.big_shot_lib.api.util.buffer

import net.typho.big_shot_lib.api.util.platform.PlatformUtil
import java.nio.ByteOrder

interface MemoryWriter {
    companion object {
        @JvmStatic
        @get:JvmName("areChecksEnabled")
        @set:JvmName("setChecksEnabled")
        var CHECKS = PlatformUtil.isDevEnv()

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

    fun skip(bytes: Long)

    fun bytesLeft(): Long

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

        writeShort((a shl 8) or b)
    }

    fun write4x1(a: Int, b: Int, c: Int, d: Int) {
        if (CHECKS) {
            checkIsByte(a)
            checkIsByte(b)
            checkIsByte(c)
            checkIsByte(d)
        }

        writeInt((a shl 24) or (b shl 16) or (c shl 8) or d)
    }

    fun write2x2(a: Int, b: Int) {
        if (CHECKS) {
            checkIsShort(a)
            checkIsShort(b)
        }

        writeInt((a shl 16) or b)
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

        writeLong((a.toLong() shl 56) or (b.toLong() shl 48) or (c.toLong() shl 40) or (d.toLong() shl 36) or (e.toLong() shl 24) or (f.toLong() shl 16) or (g.toLong() shl 8) or h.toLong())
    }

    fun write4x2(a: Int, b: Int, c: Int, d: Int) {
        if (CHECKS) {
            checkIsShort(a)
            checkIsShort(b)
            checkIsShort(c)
            checkIsShort(d)
        }

        writeInt((a shl 48) or (b shl 32) or (c shl 16) or d)
    }

    fun write2x4(a: Int, b: Int) {
        writeLong((a.toLong() shl 32) or b.toLong())
    }

    fun writePacked2x1(v: Int) {
        writeShort(v)
    }

    fun writePacked4x1(v: Int) {
        writeInt(v)
    }

    fun writePacked2x2(v: Int) {
        writeInt(v)
    }

    fun writePacked8x1(v: Long) {
        writeLong(v)
    }

    fun writePacked4x2(v: Long) {
        writeLong(v)
    }

    fun writePacked2x4(v: Long) {
        writeLong(v)
    }

    open class Delegate(
        @JvmField
        protected val delegate: MemoryWriter
    ) : MemoryWriter by delegate
}