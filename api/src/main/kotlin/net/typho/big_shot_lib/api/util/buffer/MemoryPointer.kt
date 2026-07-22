package net.typho.big_shot_lib.api.util.buffer

import net.typho.big_shot_lib.api.math.IVec4
import net.typho.big_shot_lib.api.math.IVec2
import net.typho.big_shot_lib.api.math.IVec3
import org.lwjgl.system.MemoryUtil.*
import org.lwjgl.system.NativeResource
import java.nio.ByteBuffer
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi

interface MemoryPointer : Iterable<Byte> {
    val address: Long
    val size: Long

    fun asByteBuffer() = memByteBuffer(address, size.toInt())

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

    fun getVec2d(index: Long): IVec2<Double> = IVec2(getDouble(index), getDouble(index + 8))

    fun getVec2f(index: Long): IVec2<Float> = IVec2(getFloat(index), getFloat(index + 4))

    fun getVec2i(index: Long): IVec2<Int> = IVec2(getInt(index), getInt(index + 4))

    fun getVec3d(index: Long): IVec3<Double> = IVec3(getDouble(index), getDouble(index + 8), getDouble(index + 16))

    fun getVec3f(index: Long): IVec3<Float> = IVec3(getFloat(index), getFloat(index + 4), getFloat(index + 8))

    fun getVec3i(index: Long): IVec3<Int> = IVec3(getInt(index), getInt(index + 4), getInt(index + 8))

    fun getVec4d(index: Long): IVec4<Double> = IVec4(getDouble(index), getDouble(index + 8), getDouble(index + 16), getDouble(index + 24))

    fun getVec4f(index: Long): IVec4<Float> = IVec4(getFloat(index), getFloat(index + 4), getFloat(index + 8), getFloat(index + 12))

    fun getVec4i(index: Long): IVec4<Int> = IVec4(getInt(index), getInt(index + 4), getInt(index + 8), getInt(index + 12))

    operator fun set(index: Long, value: Byte) = memPutByte(checkIndex(index, 1), value)

    operator fun set(index: Long, value: Short) = memPutShort(checkIndex(index, 2), value)

    operator fun set(index: Long, value: Int) = memPutInt(checkIndex(index, 4), value)

    operator fun set(index: Long, value: Long) = memPutLong(checkIndex(index, 8), value)

    operator fun set(index: Long, value: Float) = memPutFloat(checkIndex(index, 4), value)

    operator fun set(index: Long, value: Double) = memPutDouble(checkIndex(index, 8), value)

    operator fun <N : Number> set(index: Long, value: IVec2<N>) {
        val ptr = checkIndex(index, 2L * value.opSet.byteSize)
        value.opSet.put(ptr, value.x)
        value.opSet.put(ptr + value.opSet.byteSize, value.y)
    }

    operator fun <N : Number> set(index: Long, value: IVec3<N>) {
        val ptr = checkIndex(index, 3L * value.opSet.byteSize)
        value.opSet.put(ptr, value.x)
        value.opSet.put(ptr + value.opSet.byteSize, value.y)
        value.opSet.put(ptr + 2L * value.opSet.byteSize, value.z)
    }

    operator fun <N : Number> set(index: Long, value: IVec4<N>) {
        val ptr = checkIndex(index, 4L * value.opSet.byteSize)
        value.opSet.put(ptr, value.x)
        value.opSet.put(ptr + value.opSet.byteSize, value.y)
        value.opSet.put(ptr + 2L * value.opSet.byteSize, value.z)
        value.opSet.put(ptr + 3L * value.opSet.byteSize, value.w)
    }

    fun read() = read(0L)

    fun read(offset: Long): MemoryReader {
        return object : MemoryReader {
            var index = offset

            fun index(increment: Long): Long {
                val i = index
                index += increment
                return i
            }

            override fun skip(bytes: Long) {
                index += bytes
            }

            override fun bytesLeft(): Long {
                return size - index
            }

            override fun readByte(): Byte {
                return get(index(1))
            }

            override fun readUByte(): Int {
                return get(index(1)).toUByte().toInt()
            }

            override fun readShort(): Short {
                return getShort(index(2))
            }

            override fun readUShort(): Int {
                return getShort(index(2)).toUShort().toInt()
            }

            override fun readInt(): Int {
                return getInt(index(4))
            }

            override fun readLong(): Long {
                return getLong(index(8))
            }

            override fun readFloat(): Float {
                return getFloat(index(4))
            }

            override fun readDouble(): Double {
                return getDouble(index(8))
            }

            override fun readBoolean(): Boolean {
                return get(index(1)) == 1.toByte()
            }
        }
    }

    fun write() = write(0L)

    fun write(offset: Long): MemoryWriter {
        return object : MemoryWriter {
            var index = offset

            fun index(increment: Long): Long {
                val i = index
                index += increment
                return i
            }

            override fun skip(bytes: Long) {
                index += bytes
            }

            override fun bytesLeft(): Long {
                return size - index
            }

            override fun writeByte(v: Int) {
                set(index(1), v.toByte())
            }

            override fun writeShort(v: Int) {
                set(index(2), v.toShort())
            }

            override fun writeInt(v: Int) {
                set(index(4), v)
            }

            override fun writeLong(v: Long) {
                set(index(8), v)
            }

            override fun writeFloat(v: Float) {
                set(index(4), v)
            }

            override fun writeDouble(v: Double) {
                set(index(8), v)
            }

            override fun writeBoolean(v: Boolean) {
                set(index(1), if (v) 1 else 0)
            }
        }
    }

    interface Native : MemoryPointer, NativeResource

    companion object {
        private fun MemoryPointer.checkIndex(index: Long, size: Long): Long {
            if (index < 0 || index + size > this.size) {
                throw IndexOutOfBoundsException("Invalid index $index + $size for size ${this.size}")
            }

            return address + index
        }

        @JvmStatic
        fun wrap(buffer: ByteBuffer): MemoryPointer = Nio(buffer)

        @JvmStatic
        fun wrap(address: Long, size: Long): Native = Raw(address, size)

        @JvmStatic
        fun alloc(size: Long): Native = Raw(nmemAllocChecked(size), size)
    }

    private class Nio(
        nio: ByteBuffer
    ) : MemoryPointer {
        override val address: Long = memAddress(nio)
        override val size: Long = (nio.limit() - nio.position()).toLong()
    }

    @OptIn(ExperimentalAtomicApi::class)
    private class Raw(
        override val address: Long,
        override val size: Long
    ) : Native {
        private val freed = AtomicBoolean(false)

        override fun free() {
            if (freed.compareAndSet(false, true)) {
                nmemFree(address)
            }
        }
    }
}