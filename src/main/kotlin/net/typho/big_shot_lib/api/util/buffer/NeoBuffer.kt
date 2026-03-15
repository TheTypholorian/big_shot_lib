package net.typho.big_shot_lib.api.util.buffer

import org.lwjgl.system.MemoryUtil.*
import org.lwjgl.system.NativeResource
import java.lang.ref.Cleaner
import java.nio.*

interface NeoBuffer {
    companion object {
        @JvmStatic
        val NULL = Impl(0, 0, false)
    }

    val address: Long
    val size: Long

    fun checkIndex(index: Long, size: Long) {
        if (index < 0 || index + size > address + this.size) {
            throw IndexOutOfBoundsException("Invalid range $index + $size in buffer of size ${this.size}")
        }
    }

    fun range(index: Long, size: Long): NeoBuffer {
        checkIndex(index, size)
        return Impl(address + index, size)
    }

    fun put(index: Long, data: NeoBuffer)

    fun put(index: Long, data: Byte)

    fun put(index: Long, data: ByteArray)

    fun put(index: Long, data: Short)

    fun put(index: Long, data: ShortArray)

    fun put(index: Long, data: Char)

    fun put(index: Long, data: CharArray)

    fun put(index: Long, data: Int)

    fun put(index: Long, data: IntArray)

    fun put(index: Long, data: Long)

    fun put(index: Long, data: LongArray)

    fun put(index: Long, data: Float)

    fun put(index: Long, data: FloatArray)

    fun put(index: Long, data: Double)

    fun put(index: Long, data: DoubleArray)

    class Impl(
        override val address: Long,
        override val size: Long,
        autoFree: Boolean = true
    ) : NeoBuffer, NativeResource {
        var freed = false
            private set
        private val cleanup: Cleaner.Cleanable = if (autoFree)
            CLEANER.register(this, Cleanup(address))
        else
            Cleaner.Cleanable { nmemFree(address) }

        constructor(size: Long) : this(nmemAlloc(size), size)

        constructor(buffer: ByteBuffer) : this(memAddress(buffer), (buffer.limit() - buffer.position()).toLong(), false)

        constructor(buffer: ShortBuffer) : this(memAddress(buffer), (buffer.limit() - buffer.position()).toLong() * Short.SIZE_BYTES, false)

        constructor(buffer: CharBuffer) : this(memAddress(buffer), (buffer.limit() - buffer.position()).toLong() * Char.SIZE_BYTES, false)

        constructor(buffer: IntBuffer) : this(memAddress(buffer), (buffer.limit() - buffer.position()).toLong() * Int.SIZE_BYTES, false)

        constructor(buffer: LongBuffer) : this(memAddress(buffer), (buffer.limit() - buffer.position()).toLong() * Long.SIZE_BYTES, false)

        constructor(buffer: FloatBuffer) : this(memAddress(buffer), (buffer.limit() - buffer.position()).toLong() * Float.SIZE_BYTES, false)

        constructor(buffer: DoubleBuffer) : this(memAddress(buffer), (buffer.limit() - buffer.position()).toLong() * Double.SIZE_BYTES, false)

        override fun free() {
            if (!freed) {
                freed = true
                cleanup.clean()
            }
        }

        private class Cleanup(
            val ptr: Long
        ) : Runnable {
            override fun run() {
                nmemFree(ptr)
            }
        }

        companion object {
            @JvmStatic
            private val CLEANER = Cleaner.create()
        }

        override fun put(index: Long, data: NeoBuffer) {
            checkIndex(index, data.size)
            memCopy(data.address, address, data.size)
        }

        override fun put(index: Long, data: Byte) {
            checkIndex(index, Byte.SIZE_BYTES.toLong())
            memPutByte(address + index, data)
        }

        override fun put(index: Long, data: ByteArray) {
            checkIndex(index, data.size.toLong())
            data.forEachIndexed { i, b -> memPutByte(address + index + i, b) }
        }

        override fun put(index: Long, data: Short) {
            checkIndex(index, Short.SIZE_BYTES.toLong())
            memPutShort(address + index, data)
        }

        override fun put(index: Long, data: ShortArray) {
            checkIndex(index, data.size.toLong() * Short.SIZE_BYTES)
            data.forEachIndexed { i, b -> memPutShort(address + index + (i shl 1), b) }
        }

        override fun put(index: Long, data: Char) {
            checkIndex(index, Char.SIZE_BYTES.toLong())
            memPutShort(address + index, data.code.toShort())
        }

        override fun put(index: Long, data: CharArray) {
            checkIndex(index, data.size.toLong() * Char.SIZE_BYTES)
            data.forEachIndexed { i, b -> memPutShort(address + index + (i shl 1), b.code.toShort()) }
        }

        override fun put(index: Long, data: Int) {
            checkIndex(index, Int.SIZE_BYTES.toLong())
            memPutInt(address + index, data)
        }

        override fun put(index: Long, data: IntArray) {
            checkIndex(index, data.size.toLong() * Int.SIZE_BYTES)
            data.forEachIndexed { i, b -> memPutInt(address + index + (i shl 2), b) }
        }

        override fun put(index: Long, data: Long) {
            checkIndex(index, Long.SIZE_BYTES.toLong())
            memPutLong(address + index, data)
        }

        override fun put(index: Long, data: LongArray) {
            checkIndex(index, data.size.toLong() * Long.SIZE_BYTES)
            data.forEachIndexed { i, b -> memPutLong(address + index + (i shl 3), b) }
        }

        override fun put(index: Long, data: Float) {
            checkIndex(index, Float.SIZE_BYTES.toLong())
            memPutFloat(address + index, data)
        }

        override fun put(index: Long, data: FloatArray) {
            checkIndex(index, data.size.toLong() * Float.SIZE_BYTES)
            data.forEachIndexed { i, b -> memPutFloat(address + index + (i shl 2), b) }
        }

        override fun put(index: Long, data: Double) {
            checkIndex(index, Double.SIZE_BYTES.toLong())
            memPutDouble(address + index, data)
        }

        override fun put(index: Long, data: DoubleArray) {
            checkIndex(index, data.size.toLong() * Double.SIZE_BYTES)
            data.forEachIndexed { i, b -> memPutDouble(address + index + (i shl 3), b) }
        }
    }
}