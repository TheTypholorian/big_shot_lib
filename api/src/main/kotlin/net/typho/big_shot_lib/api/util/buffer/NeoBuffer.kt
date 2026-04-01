package net.typho.big_shot_lib.api.util.buffer

import net.typho.big_shot_lib.api.math.vec.*
import net.typho.big_shot_lib.api.util.*
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.MemoryUtil.*
import org.lwjgl.system.NativeResource
import java.io.InputStream
import java.io.OutputStream
import java.lang.ref.Cleaner
import java.nio.Buffer
import java.nio.ByteBuffer

abstract class NeoBuffer : Iterable<Byte> {
    abstract val address: Long
    abstract val size: Long

    protected fun checkIndex(index: Long, size: Long): Long {
        if (index < 0 || index + size > this.size) {
            throw IndexOutOfBoundsException(index)
        }

        return address + index
    }

    override fun iterator() = object : Iterator<Byte> {
        var index = 0L

        override fun hasNext() = index < size

        override fun next(): Byte {
            if (!hasNext()) {
                throw NoSuchElementException()
            }

            return memGetByte(address + index++)
        }
    }

    operator fun get(index: Long) = getByte(index)

    fun getByte(index: Long) = memGetByte(checkIndex(index, 1))

    fun getShort(index: Long) = memGetShort(checkIndex(index, 2))

    fun getInt(index: Long) = memGetInt(checkIndex(index, 4))

    fun getLong(index: Long) = memGetLong(checkIndex(index, 8))

    fun getFloat(index: Long) = memGetFloat(checkIndex(index, 4))

    fun getDouble(index: Long) = memGetDouble(checkIndex(index, 8))

    fun getByteArray(index: Long, length: Int): ByteArray {
        val ptr = checkIndex(index, length.toLong())
        return ByteArray(length) { memGetByte(ptr + it) }
    }

    fun getShortArray(index: Long, length: Int): ShortArray {
        val ptr = checkIndex(index, length.shortByteIndex.toLong())
        return ShortArray(length) { memGetShort(ptr + it.shortByteIndex) }
    }

    fun getIntArray(index: Long, length: Int): IntArray {
        val ptr = checkIndex(index, length.intByteIndex.toLong())
        return IntArray(length) { memGetInt(ptr + it.intByteIndex) }
    }

    fun getLongArray(index: Long, length: Int): LongArray {
        val ptr = checkIndex(index, length.longByteIndex.toLong())
        return LongArray(length) { memGetLong(ptr + it.longByteIndex) }
    }

    fun getFloatArray(index: Long, length: Int): FloatArray {
        val ptr = checkIndex(index, length.floatByteIndex.toLong())
        return FloatArray(length) { memGetFloat(ptr + it.floatByteIndex) }
    }

    fun getDoubleArray(index: Long, length: Int): DoubleArray {
        val ptr = checkIndex(index, length.doubleByteIndex.toLong())
        return DoubleArray(length) { memGetDouble(ptr + it.doubleByteIndex) }
    }

    fun getVec2d(index: Long): AbstractVec2<Double> = NeoVec2d(getDouble(index), getDouble(index + 8))

    fun getVec2f(index: Long): AbstractVec2<Float> = NeoVec2f(getFloat(index), getFloat(index + 4))

    fun getVec2i(index: Long): AbstractVec2<Int> = NeoVec2i(getInt(index), getInt(index + 4))

    fun getVec3d(index: Long): AbstractVec3<Double> = NeoVec3d(getDouble(index), getDouble(index + 8), getDouble(index + 16))

    fun getVec3f(index: Long): AbstractVec3<Float> = NeoVec3f(getFloat(index), getFloat(index + 4), getFloat(index + 8))

    fun getVec3i(index: Long): AbstractVec3<Int> = NeoVec3i(getInt(index), getInt(index + 4), getInt(index + 8))

    fun getVec4d(index: Long): AbstractVec4<Double> = NeoVec4d(getDouble(index), getDouble(index + 8), getDouble(index + 16), getDouble(index + 24))

    fun getVec4f(index: Long): AbstractVec4<Float> = NeoVec4f(getFloat(index), getFloat(index + 4), getFloat(index + 8), getFloat(index + 12))

    fun getVec4i(index: Long): AbstractVec4<Int> = NeoVec4i(getInt(index), getInt(index + 4), getInt(index + 8), getInt(index + 12))

    operator fun set(index: Long, value: Byte) = memPutByte(checkIndex(index, 1), value)

    operator fun set(index: Long, value: Short) = memPutShort(checkIndex(index, 2), value)

    operator fun set(index: Long, value: Int) = memPutInt(checkIndex(index, 4), value)

    operator fun set(index: Long, value: Long) = memPutLong(checkIndex(index, 8), value)

    operator fun set(index: Long, value: Float) = memPutFloat(checkIndex(index, 4), value)

    operator fun set(index: Long, value: Double) = memPutDouble(checkIndex(index, 8), value)

    operator fun set(index: Long, value: ByteArray) {
        val ptr = checkIndex(index, value.size.toLong())
        value.forEachIndexed { index, b -> memPutByte(ptr + index, b) }
    }

    operator fun set(index: Long, value: ShortArray) {
        val ptr = checkIndex(index, value.size.shortByteIndex.toLong())
        value.forEachIndexed { index, b -> memPutShort(ptr + index.shortByteIndex, b) }
    }

    operator fun set(index: Long, value: IntArray) {
        val ptr = checkIndex(index, value.size.intByteIndex.toLong())
        value.forEachIndexed { index, b -> memPutInt(ptr + index.intByteIndex, b) }
    }

    operator fun set(index: Long, value: LongArray) {
        val ptr = checkIndex(index, value.size.longByteIndex.toLong())
        value.forEachIndexed { index, b -> memPutLong(ptr + index.longByteIndex, b) }
    }

    operator fun set(index: Long, value: FloatArray) {
        val ptr = checkIndex(index, value.size.floatByteIndex.toLong())
        value.forEachIndexed { index, b -> memPutFloat(ptr + index.floatByteIndex, b) }
    }

    operator fun set(index: Long, value: DoubleArray) {
        val ptr = checkIndex(index, value.size.doubleByteIndex.toLong())
        value.forEachIndexed { index, b -> memPutDouble(ptr + index.doubleByteIndex, b) }
    }

    fun set(index: Long, value: ByteArray, offset: Int, length: Int) {
        val ptr = checkIndex(index, length.toLong())
        value.forEachRangeIndexed(offset, length) { relativeIndex, absoluteIndex, b -> memPutByte(ptr + relativeIndex, b) }
    }

    fun set(index: Long, value: ShortArray, offset: Int, length: Int) {
        val ptr = checkIndex(index, length.shortByteIndex.toLong())
        value.forEachRangeIndexed(offset, length) { relativeIndex, absoluteIndex, b -> memPutShort(ptr + relativeIndex.shortByteIndex, b) }
    }

    fun set(index: Long, value: IntArray, offset: Int, length: Int) {
        val ptr = checkIndex(index, length.intByteIndex.toLong())
        value.forEachRangeIndexed(offset, length) { relativeIndex, absoluteIndex, b -> memPutInt(ptr + relativeIndex.intByteIndex, b) }
    }

    fun set(index: Long, value: LongArray, offset: Int, length: Int) {
        val ptr = checkIndex(index, length.longByteIndex.toLong())
        value.forEachRangeIndexed(offset, length) { relativeIndex, absoluteIndex, b -> memPutLong(ptr + relativeIndex.longByteIndex, b) }
    }

    fun set(index: Long, value: FloatArray, offset: Int, length: Int) {
        val ptr = checkIndex(index, length.floatByteIndex.toLong())
        value.forEachRangeIndexed(offset, length) { relativeIndex, absoluteIndex, b -> memPutFloat(ptr + relativeIndex.floatByteIndex, b) }
    }

    fun set(index: Long, value: DoubleArray, offset: Int, length: Int) {
        val ptr = checkIndex(index, length.doubleByteIndex.toLong())
        value.forEachRangeIndexed(offset, length) { relativeIndex, absoluteIndex, b -> memPutDouble(ptr + relativeIndex.doubleByteIndex, b) }
    }

    operator fun <N : Number> set(index: Long, value: AbstractVec2<N>) {
        val ptr = checkIndex(index, 2L * value.opSet.byteSize)
        value.opSet.put(ptr, value.x)
        value.opSet.put(ptr + value.opSet.byteSize, value.y)
    }

    operator fun <N : Number> set(index: Long, value: AbstractVec3<N>) {
        val ptr = checkIndex(index, 3L * value.opSet.byteSize)
        value.opSet.put(ptr, value.x)
        value.opSet.put(ptr + value.opSet.byteSize, value.y)
        value.opSet.put(ptr + 2L * value.opSet.byteSize, value.z)
    }

    operator fun <N : Number> set(index: Long, value: AbstractVec4<N>) {
        val ptr = checkIndex(index, 4L * value.opSet.byteSize)
        value.opSet.put(ptr, value.x)
        value.opSet.put(ptr + value.opSet.byteSize, value.y)
        value.opSet.put(ptr + 2L * value.opSet.byteSize, value.z)
        value.opSet.put(ptr + 3L * value.opSet.byteSize, value.w)
    }

    @JvmOverloads
    fun read(offset: Long = 0L): InputStream {
        return object : InputStream() {
            var index = offset

            override fun read(): Int {
                return get(index++).toInt()
            }
        }
    }

    @JvmOverloads
    fun write(offset: Long = 0L): OutputStream {
        return object : OutputStream() {
            var index = offset

            override fun write(b: Int) {
                set(index++, b)
            }
        }
    }

    fun asByteBuffer(): ByteBuffer = memByteBuffer(address, size.toInt())

    open class Nio(
        @JvmField
        val buffer: Buffer
    ) : NeoBuffer() {
        override val address: Long = memAddress(buffer)
        override val size: Long = (buffer.limit() - buffer.position()).toLong()
    }

    open class Native(
        override val address: Long,
        override val size: Long
    ) : NeoBuffer(), NativeResource {
        var isFreed: Boolean = false
            protected set

        constructor(size: Long) : this(nmemAllocChecked(size), size)

        override fun free() {
            if (!isFreed) {
                isFreed = true
                nmemFree(address)
            }
        }
    }

    open class GCNative(
        address: Long,
        size: Long
    ) : Native(address, size) {
        companion object {
            @JvmStatic
            private val CLEANER = Cleaner.create()
        }

        protected val cleanup: Cleaner.Cleanable = CLEANER.register(this, createCleanup())

        override fun free() {
            if (!isFreed) {
                isFreed = true
                cleanup.clean()
            }
        }

        protected open fun createCleanup(): Runnable {
            val ptr = address
            return Runnable {
                nmemFree(ptr)
            }
        }
    }
}