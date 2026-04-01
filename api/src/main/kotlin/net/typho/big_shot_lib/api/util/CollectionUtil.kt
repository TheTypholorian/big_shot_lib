package net.typho.big_shot_lib.api.util

fun <T> Array<T>.forEachRange(offset: Int, length: Int, out: (T) -> Unit) {
    if (offset < 0 || length < 0 || offset + length > size) {
        throw ArrayIndexOutOfBoundsException("Range $offset + $length out of bounds for size $size")
    }

    repeat(length) {
        out(get(it + offset))
    }
}

fun <T> Array<T>.forEachRangeIndexed(offset: Int, length: Int, out: (relativeIndex: Int, absoluteIndex: Int, T) -> Unit) {
    if (offset < 0 || length < 0 || offset + length > size) {
        throw ArrayIndexOutOfBoundsException("Range $offset + $length out of bounds for size $size")
    }

    repeat(length) { relativeIndex ->
        val index = relativeIndex + offset
        out(relativeIndex, index, get(index))
    }
}

fun ByteArray.forEachRange(offset: Int, length: Int, out: (Byte) -> Unit) {
    if (offset < 0 || length < 0 || offset + length > size) {
        throw ArrayIndexOutOfBoundsException("Range $offset + $length out of bounds for size $size")
    }

    repeat(length) {
        out(get(it + offset))
    }
}

fun ByteArray.forEachRangeIndexed(offset: Int, length: Int, out: (relativeIndex: Int, absoluteIndex: Int, Byte) -> Unit) {
    if (offset < 0 || length < 0 || offset + length > size) {
        throw ArrayIndexOutOfBoundsException("Range $offset + $length out of bounds for size $size")
    }

    repeat(length) { relativeIndex ->
        val index = relativeIndex + offset
        out(relativeIndex, index, get(index))
    }
}

fun ShortArray.forEachRange(offset: Int, length: Int, out: (Short) -> Unit) {
    if (offset < 0 || length < 0 || offset + length > size) {
        throw ArrayIndexOutOfBoundsException("Range $offset + $length out of bounds for size $size")
    }

    repeat(length) {
        out(get(it + offset))
    }
}

fun ShortArray.forEachRangeIndexed(offset: Int, length: Int, out: (relativeIndex: Int, absoluteIndex: Int, Short) -> Unit) {
    if (offset < 0 || length < 0 || offset + length > size) {
        throw ArrayIndexOutOfBoundsException("Range $offset + $length out of bounds for size $size")
    }

    repeat(length) { relativeIndex ->
        val index = relativeIndex + offset
        out(relativeIndex, index, get(index))
    }
}

fun IntArray.forEachRange(offset: Int, length: Int, out: (Int) -> Unit) {
    if (offset < 0 || length < 0 || offset + length > size) {
        throw ArrayIndexOutOfBoundsException("Range $offset + $length out of bounds for size $size")
    }

    repeat(length) {
        out(get(it + offset))
    }
}

fun IntArray.forEachRangeIndexed(offset: Int, length: Int, out: (relativeIndex: Int, absoluteIndex: Int, Int) -> Unit) {
    if (offset < 0 || length < 0 || offset + length > size) {
        throw ArrayIndexOutOfBoundsException("Range $offset + $length out of bounds for size $size")
    }

    repeat(length) { relativeIndex ->
        val index = relativeIndex + offset
        out(relativeIndex, index, get(index))
    }
}

fun LongArray.forEachRange(offset: Int, length: Int, out: (Long) -> Unit) {
    if (offset < 0 || length < 0 || offset + length > size) {
        throw ArrayIndexOutOfBoundsException("Range $offset + $length out of bounds for size $size")
    }

    repeat(length) {
        out(get(it + offset))
    }
}

fun LongArray.forEachRangeIndexed(offset: Int, length: Int, out: (relativeIndex: Int, absoluteIndex: Int, Long) -> Unit) {
    if (offset < 0 || length < 0 || offset + length > size) {
        throw ArrayIndexOutOfBoundsException("Range $offset + $length out of bounds for size $size")
    }

    repeat(length) { relativeIndex ->
        val index = relativeIndex + offset
        out(relativeIndex, index, get(index))
    }
}

fun FloatArray.forEachRange(offset: Int, length: Int, out: (Float) -> Unit) {
    if (offset < 0 || length < 0 || offset + length > size) {
        throw ArrayIndexOutOfBoundsException("Range $offset + $length out of bounds for size $size")
    }

    repeat(length) {
        out(get(it + offset))
    }
}

fun FloatArray.forEachRangeIndexed(offset: Int, length: Int, out: (relativeIndex: Int, absoluteIndex: Int, Float) -> Unit) {
    if (offset < 0 || length < 0 || offset + length > size) {
        throw ArrayIndexOutOfBoundsException("Range $offset + $length out of bounds for size $size")
    }

    repeat(length) { relativeIndex ->
        val index = relativeIndex + offset
        out(relativeIndex, index, get(index))
    }
}

fun DoubleArray.forEachRange(offset: Int, length: Int, out: (Double) -> Unit) {
    if (offset < 0 || length < 0 || offset + length > size) {
        throw ArrayIndexOutOfBoundsException("Range $offset + $length out of bounds for size $size")
    }

    repeat(length) {
        out(get(it + offset))
    }
}

fun DoubleArray.forEachRangeIndexed(offset: Int, length: Int, out: (relativeIndex: Int, absoluteIndex: Int, Double) -> Unit) {
    if (offset < 0 || length < 0 || offset + length > size) {
        throw ArrayIndexOutOfBoundsException("Range $offset + $length out of bounds for size $size")
    }

    repeat(length) { relativeIndex ->
        val index = relativeIndex + offset
        out(relativeIndex, index, get(index))
    }
}

fun CharArray.forEachRange(offset: Int, length: Int, out: (Char) -> Unit) {
    if (offset < 0 || length < 0 || offset + length > size) {
        throw ArrayIndexOutOfBoundsException("Range $offset + $length out of bounds for size $size")
    }

    repeat(length) {
        out(get(it + offset))
    }
}

fun CharArray.forEachRangeIndexed(offset: Int, length: Int, out: (relativeIndex: Int, absoluteIndex: Int, Char) -> Unit) {
    if (offset < 0 || length < 0 || offset + length > size) {
        throw ArrayIndexOutOfBoundsException("Range $offset + $length out of bounds for size $size")
    }

    repeat(length) { relativeIndex ->
        val index = relativeIndex + offset
        out(relativeIndex, index, get(index))
    }
}

fun BooleanArray.forEachRange(offset: Int, length: Int, out: (Boolean) -> Unit) {
    if (offset < 0 || length < 0 || offset + length > size) {
        throw ArrayIndexOutOfBoundsException("Range $offset + $length out of bounds for size $size")
    }

    repeat(length) {
        out(get(it + offset))
    }
}

fun BooleanArray.forEachRangeIndexed(offset: Int, length: Int, out: (relativeIndex: Int, absoluteIndex: Int, Boolean) -> Unit) {
    if (offset < 0 || length < 0 || offset + length > size) {
        throw ArrayIndexOutOfBoundsException("Range $offset + $length out of bounds for size $size")
    }

    repeat(length) { relativeIndex ->
        val index = relativeIndex + offset
        out(relativeIndex, index, get(index))
    }
}