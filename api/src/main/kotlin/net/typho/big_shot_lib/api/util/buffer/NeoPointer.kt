package net.typho.big_shot_lib.api.util.buffer

import net.typho.big_shot_lib.api.math.op.OperatorSet
import net.typho.big_shot_lib.api.math.vec.AbstractVec2
import net.typho.big_shot_lib.api.math.vec.AbstractVec3
import net.typho.big_shot_lib.api.math.vec.AbstractVec4
import net.typho.big_shot_lib.api.util.doubleByteIndex
import net.typho.big_shot_lib.api.util.floatByteIndex
import net.typho.big_shot_lib.api.util.intByteIndex
import net.typho.big_shot_lib.api.util.longByteIndex
import net.typho.big_shot_lib.api.util.shortByteIndex
import org.lwjgl.system.MemoryUtil.*
import org.lwjgl.system.NativeResource
import java.lang.ref.Cleaner
import java.nio.Buffer

interface NeoPointer {
    val address: Long
    val size: Long

    fun checkIndex(index: Long, size: Long): Long {
        if (index < 0 || index + size > this.size) {
            throw IndexOutOfBoundsException(index)
        }

        return address + index
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

    operator fun set(index: Long, value: Byte) = memPutByte(checkIndex(index, 1), value)

    operator fun set(index: Long, value: Short) = memPutShort(checkIndex(index, 2), value)

    operator fun set(index: Long, value: Int) = memPutInt(checkIndex(index, 4), value)

    operator fun set(index: Long, value: Long) = memPutLong(checkIndex(index, 8), value)

    operator fun set(index: Long, value: Float) = memPutFloat(checkIndex(index, 4), value)

    operator fun set(index: Long, value: Double) = memPutDouble(checkIndex(index, 8), value)

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

    open class Nio(
        @JvmField
        val buffer: Buffer
    ) : NeoPointer {
        override val address: Long = memAddress(buffer)
        override val size: Long = (buffer.limit() - buffer.position()).toLong()
    }

    open class Native(
        override val address: Long,
        override val size: Long
    ) : NeoPointer, NativeResource {
        var isFreed: Boolean = false
            protected set

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