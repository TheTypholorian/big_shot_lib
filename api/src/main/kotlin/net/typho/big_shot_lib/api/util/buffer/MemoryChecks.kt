package net.typho.big_shot_lib.api.util.buffer

import net.typho.big_shot_lib.api.util.platform.NeoModLoader

object MemoryChecks {
    @JvmStatic
    @get:JvmName("areChecksEnabled")
    @set:JvmName("setChecksEnabled")
    var CHECKS = NeoModLoader.isDevEnv()

    @JvmStatic
    fun checkIsByte(v: Int) {
        if (CHECKS && ((v and 0xFF.inv()) != 0)) {
            throw IllegalArgumentException("Illegal non-byte value ${v.toHexString()}")
        }
    }

    @JvmStatic
    fun checkAreBytes(a: Int, b: Int) {
        checkIsByte(a)
        checkIsByte(b)
    }

    @JvmStatic
    fun checkAreBytes(a: Int, b: Int, c: Int, d: Int) {
        checkIsByte(a)
        checkIsByte(b)
        checkIsByte(c)
        checkIsByte(d)
    }

    @JvmStatic
    fun checkAreBytes(a: Int, b: Int, c: Int, d: Int, e: Int, f: Int, g: Int, h: Int) {
        checkIsByte(a)
        checkIsByte(b)
        checkIsByte(c)
        checkIsByte(d)
        checkIsByte(e)
        checkIsByte(f)
        checkIsByte(g)
        checkIsByte(h)
    }

    @JvmStatic
    fun checkIsShort(v: Int) {
        if (CHECKS && ((v and 0xFFFF.inv()) != 0)) {
            throw IllegalArgumentException("Illegal non-short value ${v.toHexString()}")
        }
    }

    @JvmStatic
    fun checkAreShorts(a: Int, b: Int) {
        checkIsShort(a)
        checkIsShort(b)
    }

    @JvmStatic
    fun checkAreShorts(a: Int, b: Int, c: Int, d: Int) {
        checkIsShort(a)
        checkIsShort(b)
        checkIsShort(c)
        checkIsShort(d)
    }

    @JvmStatic
    fun checkIsInt(v: Long) {
        if (CHECKS && ((v and 0xFFFFFFFFL.inv()) != 0L)) {
            throw IllegalArgumentException("Illegal non-int value ${v.toHexString()}")
        }
    }

    @JvmStatic
    fun checkAreInts(a: Long, b: Long) {
        checkIsInt(a)
        checkIsInt(b)
    }

    @JvmStatic
    fun checkAreInts(a: Long, b: Long, c: Long, d: Long) {
        checkIsInt(a)
        checkIsInt(b)
        checkIsInt(c)
        checkIsInt(d)
    }
}