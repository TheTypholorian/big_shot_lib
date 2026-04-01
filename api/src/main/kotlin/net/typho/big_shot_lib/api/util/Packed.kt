package net.typho.big_shot_lib.api.util

const val BYTE_MASK = 0xFF
const val LBYTE_MASK = 0xFFL
const val SHORT_MASK = 0xFFFF
const val LSHORT_MASK = 0xFFFFL
const val INT_MASK = 0xFFFFFFFF
const val LINT_MASK = 0xFFFFFFFFL

val Int.byteByteIndex: Int
    get() = this
val Int.byteBitIndex: Int
    get() = this shr 3

val Int.shortByteIndex: Int
    get() = this shr 1
val Int.shortBitIndex: Int
    get() = this shr 4

val Int.intByteIndex: Int
    get() = this shr 2
val Int.intBitIndex: Int
    get() = this shr 5

val Int.longByteIndex: Int
    get() = this shr 3
val Int.longBitIndex: Int
    get() = this shr 6

val Int.floatByteIndex: Int
    get() = this shr 2
val Int.floatBitIndex: Int
    get() = this shr 5

val Int.doubleByteIndex: Int
    get() = this shr 3
val Int.doubleBitIndex: Int
    get() = this shr 6

fun Long.intAt(index: Int): Int {
    return ((this ushr index.intBitIndex) and LINT_MASK).toInt()
}

fun Long.shortAt(index: Int): Short {
    return ((this ushr index.shortBitIndex) and LSHORT_MASK).toShort()
}

fun Long.byteAt(index: Int): Byte {
    return ((this ushr index.byteBitIndex) and LBYTE_MASK).toByte()
}

fun Long.floatAt(index: Int): Float {
    return Float.fromBits(intAt(index))
}

fun Int.shortAt(index: Int): Short {
    return ((this ushr index.shortBitIndex) and SHORT_MASK).toShort()
}

fun Int.byteAt(index: Int): Byte {
    return ((this ushr index.byteBitIndex) and BYTE_MASK).toByte()
}

fun Short.byteAt(index: Int): Byte {
    return ((this.toInt() ushr index.byteBitIndex) and BYTE_MASK).toByte()
}

fun packLong(
    b0: Byte,
    b1: Byte,
    b2: Byte,
    b3: Byte,
    b4: Byte,
    b5: Byte,
    b6: Byte,
    b7: Byte
): Long {
    return b0.toLong() or
            (b1.toLong() shl 8) or
            (b2.toLong() shl 16) or
            (b3.toLong() shl 24) or
            (b4.toLong() shl 32) or
            (b5.toLong() shl 40) or
            (b6.toLong() shl 48) or
            (b7.toLong() shl 56)
}

fun packLong(
    s0: Short,
    s1: Short,
    s2: Short,
    s3: Short
): Long {
    return s0.toLong() or
            (s1.toLong() shl 16) or
            (s2.toLong() shl 32) or
            (s3.toLong() shl 48)
}

fun packLong(
    i0: Int,
    i1: Int
): Long {
    return i0.toLong() or (i1.toLong() shl 32)
}

fun packLong(
    f0: Float,
    f1: Float
): Long {
    return packLong(
        f0.toBits(),
        f1.toBits()
    )
}

fun packInt(
    b0: Byte,
    b1: Byte,
    b2: Byte,
    b3: Byte
): Int {
    return b0.toInt() or
            (b1.toInt() shl 8) or
            (b2.toInt() shl 16) or
            (b3.toInt() shl 24)
}

fun packInt(
    s0: Short,
    s1: Short
): Int {
    return s0.toInt() or (s1.toInt() shl 16)
}

fun packShort(
    b0: Byte,
    b1: Byte
): Short {
    return (b0.toInt() or (b1.toInt() shl 8)).toShort()
}
