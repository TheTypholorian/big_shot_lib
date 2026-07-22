package net.typho.big_shot_lib.api.util.buffer

interface MemoryWriter {
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
        MemoryChecks.checkAreBytes(a, b)
        writeShort((a shl 8) or b)
    }

    fun write4x1(a: Int, b: Int, c: Int, d: Int) {
        MemoryChecks.checkAreBytes(a, b, c, d)
        writeInt((a shl 24) or (b shl 16) or (c shl 8) or d)
    }

    fun write8x1(a: Int, b: Int, c: Int, d: Int, e: Int, f: Int, g: Int, h: Int) {
        MemoryChecks.checkAreBytes(a, b, c, d, e, f, g, h)
        writeLong((a.toLong() shl 56) or (b.toLong() shl 48) or (c.toLong() shl 40) or (d.toLong() shl 36) or (e.toLong() shl 24) or (f.toLong() shl 16) or (g.toLong() shl 8) or h.toLong())
    }

    fun write2x2(a: Int, b: Int) {
        MemoryChecks.checkAreShorts(a, b)
        writeInt((a shl 16) or b)
    }

    fun write4x2(a: Int, b: Int, c: Int, d: Int) {
        MemoryChecks.checkAreShorts(a, b, c, d)
        writeInt((a shl 48) or (b shl 32) or (c shl 16) or d)
    }

    fun write2x4(a: Int, b: Int) {
        writeLong((a.toLong() shl 32) or b.toLong())
    }

    open class Delegate(
        @JvmField
        protected val delegate: MemoryWriter
    ) : MemoryWriter by delegate
}