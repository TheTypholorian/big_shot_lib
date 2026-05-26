package net.typho.big_shot_lib.api.util.buffer

import kotlin.experimental.and

const val BYTE_MASK = 0xFF
const val SBYTE_MASK = 0xFF.toShort()
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

fun Long.uintAt(index: Int): UInt {
    return ((this ushr index.intBitIndex) and LINT_MASK).toUInt()
}

fun Long.ushortAt(index: Int): UShort {
    return ((this ushr index.shortBitIndex) and LSHORT_MASK).toUShort()
}

fun Long.ubyteAt(index: Int): UByte {
    return ((this ushr index.byteBitIndex) and LBYTE_MASK).toUByte()
}

fun Int.ushortAt(index: Int): UShort {
    return ((this ushr index.shortBitIndex) and SHORT_MASK).toUShort()
}

fun Int.ubyteAt(index: Int): UByte {
    return ((this ushr index.byteBitIndex) and BYTE_MASK).toUByte()
}

fun Short.ubyteAt(index: Int): UByte {
    return ((this.toInt() ushr index.byteBitIndex) and BYTE_MASK).toUByte()
}

fun Long.lowestInt(): Int {
    return (this and LINT_MASK).toInt()
}

fun Long.lowestShort(): Short {
    return (this and LSHORT_MASK).toShort()
}

fun Long.lowestByte(): Byte {
    return (this and LBYTE_MASK).toByte()
}

fun Long.lowestFloat(): Float {
    return Float.fromBits(lowestInt())
}

fun Int.lowestShort(): Short {
    return (this and SHORT_MASK).toShort()
}

fun Int.lowestByte(): Byte {
    return (this and BYTE_MASK).toByte()
}

fun Short.lowestByte(): Byte {
    return (this and SBYTE_MASK).toByte()
}

fun Long.lowestUInt(): UInt {
    return (this and LINT_MASK).toUInt()
}

fun Long.lowestUShort(): UShort {
    return (this and LSHORT_MASK).toUShort()
}

fun Long.lowestUByte(): UByte {
    return (this and LBYTE_MASK).toUByte()
}

fun Int.lowestUShort(): UShort {
    return (this and SHORT_MASK).toUShort()
}

fun Int.lowestUByte(): UByte {
    return (this and BYTE_MASK).toUByte()
}

fun Short.lowestUByte(): UByte {
    return (this and SBYTE_MASK).toUByte()
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
    return b7.toLong() or
            (b6.toLong() shl 8) or
            (b5.toLong() shl 16) or
            (b4.toLong() shl 24) or
            (b3.toLong() shl 32) or
            (b2.toLong() shl 40) or
            (b1.toLong() shl 48) or
            (b0.toLong() shl 56)
}

fun packLong(
    s0: Short,
    s1: Short,
    s2: Short,
    s3: Short
): Long {
    return s3.toLong() or
            (s2.toLong() shl 16) or
            (s1.toLong() shl 32) or
            (s0.toLong() shl 48)
}

fun packLong(
    i0: Int,
    i1: Int
): Long {
    return i1.toLong() or (i0.toLong() shl 32)
}

fun packLong(
    f0: Float,
    f1: Float
): Long {
    return packLong(
        f1.toBits(),
        f0.toBits()
    )
}

fun packInt(
    b0: Byte,
    b1: Byte,
    b2: Byte,
    b3: Byte
): Int {
    return b3.toInt() or
            (b2.toInt() shl 8) or
            (b1.toInt() shl 16) or
            (b0.toInt() shl 24)
}

fun packInt(
    s0: Short,
    s1: Short
): Int {
    return s1.toInt() or (s0.toInt() shl 16)
}

fun packShort(
    b0: Byte,
    b1: Byte
): Short {
    return (b1.toInt() or (b0.toInt() shl 8)).toShort()
}

fun packULong(
    b0: UByte,
    b1: UByte,
    b2: UByte,
    b3: UByte,
    b4: UByte,
    b5: UByte,
    b6: UByte,
    b7: UByte
): ULong {
    return b7.toULong() or
            (b6.toULong() shl 8) or
            (b5.toULong() shl 16) or
            (b4.toULong() shl 24) or
            (b3.toULong() shl 32) or
            (b2.toULong() shl 40) or
            (b1.toULong() shl 48) or
            (b0.toULong() shl 56)
}

fun packULong(
    s0: UShort,
    s1: UShort,
    s2: UShort,
    s3: UShort
): ULong {
    return s3.toULong() or
            (s2.toULong() shl 16) or
            (s1.toULong() shl 32) or
            (s0.toULong() shl 48)
}

fun packULong(
    i0: UInt,
    i1: UInt
): ULong {
    return i1.toULong() or (i0.toULong() shl 32)
}

fun packUInt(
    b0: UByte,
    b1: UByte,
    b2: UByte,
    b3: UByte
): UInt {
    return b3.toUInt() or
            (b2.toUInt() shl 8) or
            (b1.toUInt() shl 16) or
            (b0.toUInt() shl 24)
}

fun packUInt(
    s0: UShort,
    s1: UShort
): UInt {
    return s1.toUInt() or (s0.toUInt() shl 16)
}

fun packUShort(
    b0: UByte,
    b1: UByte
): UShort {
    return (b1.toInt() or (b0.toInt() shl 8)).toUShort()
}
