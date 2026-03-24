package net.typho.big_shot_lib.api.util.buffer

import net.typho.big_shot_lib.api.util.intByteIndex
import net.typho.big_shot_lib.api.util.longByteIndex
import net.typho.big_shot_lib.api.util.shortByteIndex
import org.lwjgl.system.MemoryUtil.*
import org.lwjgl.system.NativeResource
import java.lang.ref.Cleaner
import java.nio.*

fun NeoBuffer.Native?.realloc(newSize: Long): NeoBuffer.Native {
    return this?.realloc(newSize) ?: NeoBuffer.Native(newSize)
}

interface NeoBuffer {
    companion object {
        @JvmStatic
        val NULL = Native(0, 0, false)
    }

    val address: Long
    val size: Long

    fun check(index: Long, size: Long) {
        if (index < 0 || index + size > address + this.size) {
            throw IndexOutOfBoundsException("Invalid range $index + $size in buffer of size ${this.size}")
        }
    }

    fun range(index: Long, size: Long): NeoBuffer {
        check(index, size)
        return Native(address + index, size)
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

    fun asByteBuffer(): ByteBuffer = memByteBuffer(address, size.toInt())

    class Nio(
        @JvmField
        val buffer: ByteBuffer
    ) : NeoBuffer {
        override val address: Long
            get() = memAddress(buffer)
        override val size: Long
            get() = buffer.capacity().toLong()

        override fun put(index: Long, data: NeoBuffer) {
            buffer.put(index.toInt(), data.asByteBuffer(), 0, data.size.toInt())
        }

        override fun put(index: Long, data: Byte) {
            buffer.put(index.toInt(), data)
        }

        override fun put(index: Long, data: ByteArray) {
            buffer.put(index.toInt(), data)
        }

        override fun put(index: Long, data: Short) {
            buffer.putShort(index.toInt(), data)
        }

        override fun put(index: Long, data: ShortArray) {
            data.forEachIndexed { i, s ->
                buffer.putShort(index.toInt() + i.shortByteIndex, s)
            }
        }

        override fun put(index: Long, data: Char) {
            buffer.putShort(index.toInt(), data.code.toShort())
        }

        override fun put(index: Long, data: CharArray) {
            data.forEachIndexed { i, s ->
                buffer.putShort(index.toInt() + i.shortByteIndex, s.code.toShort())
            }
        }

        override fun put(index: Long, data: Int) {
            buffer.putInt(index.toInt(), data)
        }

        override fun put(index: Long, data: IntArray) {
            data.forEachIndexed { i, s ->
                buffer.putInt(index.toInt() + i.intByteIndex, s)
            }
        }

        override fun put(index: Long, data: Long) {
            buffer.putLong(index.toInt(), data)
        }

        override fun put(index: Long, data: LongArray) {
            data.forEachIndexed { i, s ->
                buffer.putLong(index.toInt() + i.longByteIndex, s)
            }
        }

        override fun put(index: Long, data: Float) {
            buffer.putFloat(index.toInt(), data)
        }

        override fun put(index: Long, data: FloatArray) {
            data.forEachIndexed { i, s ->
                buffer.putFloat(index.toInt() + i.intByteIndex, s)
            }
        }

        override fun put(index: Long, data: Double) {
            buffer.putDouble(index.toInt(), data)
        }

        override fun put(index: Long, data: DoubleArray) {
            data.forEachIndexed { i, s ->
                buffer.putDouble(index.toInt() + i.longByteIndex, s)
            }
        }
    }

    class Native(
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

        fun realloc(newSize: Long): Native {
            if (size == newSize) {
                return this
            }

            freed = true
            return Native(nmemRealloc(address, newSize), newSize)
        }

        override fun put(index: Long, data: NeoBuffer) {
            assert(!freed)
            check(index, data.size)
            memCopy(data.address, address, data.size)
        }

        override fun put(index: Long, data: Byte) {
            assert(!freed)
            check(index, Byte.SIZE_BYTES.toLong())
            memPutByte(address + index, data)
        }

        override fun put(index: Long, data: ByteArray) {
            assert(!freed)
            check(index, data.size.toLong())
            data.forEachIndexed { i, b -> memPutByte(address + index + i, b) }
        }

        override fun put(index: Long, data: Short) {
            assert(!freed)
            check(index, Short.SIZE_BYTES.toLong())
            memPutShort(address + index, data)
        }

        override fun put(index: Long, data: ShortArray) {
            assert(!freed)
            check(index, data.size.toLong() * Short.SIZE_BYTES)
            data.forEachIndexed { i, b -> memPutShort(address + index + (i shl 1), b) }
        }

        override fun put(index: Long, data: Char) {
            assert(!freed)
            check(index, Char.SIZE_BYTES.toLong())
            memPutShort(address + index, data.code.toShort())
        }

        override fun put(index: Long, data: CharArray) {
            assert(!freed)
            check(index, data.size.toLong() * Char.SIZE_BYTES)
            data.forEachIndexed { i, b -> memPutShort(address + index + (i shl 1), b.code.toShort()) }
        }

        override fun put(index: Long, data: Int) {
            assert(!freed)
            check(index, Int.SIZE_BYTES.toLong())
            memPutInt(address + index, data)
        }

        override fun put(index: Long, data: IntArray) {
            assert(!freed)
            check(index, data.size.toLong() * Int.SIZE_BYTES)
            data.forEachIndexed { i, b -> memPutInt(address + index + (i shl 2), b) }
        }

        override fun put(index: Long, data: Long) {
            assert(!freed)
            check(index, Long.SIZE_BYTES.toLong())
            memPutLong(address + index, data)
        }

        override fun put(index: Long, data: LongArray) {
            assert(!freed)
            check(index, data.size.toLong() * Long.SIZE_BYTES)
            data.forEachIndexed { i, b -> memPutLong(address + index + (i shl 3), b) }
        }

        override fun put(index: Long, data: Float) {
            assert(!freed)
            check(index, Float.SIZE_BYTES.toLong())
            memPutFloat(address + index, data)
        }

        override fun put(index: Long, data: FloatArray) {
            assert(!freed)
            check(index, data.size.toLong() * Float.SIZE_BYTES)
            data.forEachIndexed { i, b -> memPutFloat(address + index + (i shl 2), b) }
        }

        override fun put(index: Long, data: Double) {
            assert(!freed)
            check(index, Double.SIZE_BYTES.toLong())
            memPutDouble(address + index, data)
        }

        override fun put(index: Long, data: DoubleArray) {
            assert(!freed)
            check(index, data.size.toLong() * Double.SIZE_BYTES)
            data.forEachIndexed { i, b -> memPutDouble(address + index + (i shl 3), b) }
        }
    }
}