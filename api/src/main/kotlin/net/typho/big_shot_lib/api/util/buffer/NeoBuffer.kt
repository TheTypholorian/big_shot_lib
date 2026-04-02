package net.typho.big_shot_lib.api.util.buffer

import net.typho.big_shot_lib.api.math.vec.*
import org.lwjgl.system.MemoryUtil.*
import org.lwjgl.system.NativeResource
import java.io.*
import java.lang.ref.Cleaner
import java.nio.ByteBuffer

abstract class NeoBuffer : Iterable<Byte> {
    abstract val address: Long
    abstract val size: Long
    protected abstract val nio: ByteBuffer

    protected fun checkIndex(index: Long, size: Long): Long {
        if (index < 0 || index + size > this.size) {
            throw IndexOutOfBoundsException("Invalid index $index + $size for size $size")
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
        val array = ByteArray(length)
        nio.get(index.toInt(), array)
        return array
    }

    fun getShortArray(index: Long, length: Int): ShortArray {
        val array = ShortArray(length)
        nio.asShortBuffer().get(index.toInt(), array)
        return array
    }

    fun getIntArray(index: Long, length: Int): IntArray {
        val array = IntArray(length)
        nio.asIntBuffer().get(index.toInt(), array)
        return array
    }

    fun getLongArray(index: Long, length: Int): LongArray {
        val array = LongArray(length)
        nio.asLongBuffer().get(index.toInt(), array)
        return array
    }

    fun getFloatArray(index: Long, length: Int): FloatArray {
        val array = FloatArray(length)
        nio.asFloatBuffer().get(index.toInt(), array)
        return array
    }

    fun getDoubleArray(index: Long, length: Int): DoubleArray {
        val array = DoubleArray(length)
        nio.asDoubleBuffer().get(index.toInt(), array)
        return array
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
        nio.put(index.toInt(), value)
    }

    operator fun set(index: Long, value: ShortArray) {
        nio.asShortBuffer().put(index.toInt(), value)
    }

    operator fun set(index: Long, value: IntArray) {
        nio.asIntBuffer().put(index.toInt(), value)
    }

    operator fun set(index: Long, value: LongArray) {
        nio.asLongBuffer().put(index.toInt(), value)
    }

    operator fun set(index: Long, value: FloatArray) {
        nio.asFloatBuffer().put(index.toInt(), value)
    }

    operator fun set(index: Long, value: DoubleArray) {
        nio.asDoubleBuffer().put(index.toInt(), value)
    }

    operator fun set(index: Long, value: ByteArray, offset: Int, length: Int) {
        nio.put(index.toInt(), value, offset, length)
    }

    operator fun set(index: Long, value: ShortArray, offset: Int, length: Int) {
        nio.asShortBuffer().put(index.toInt(), value, offset, length)
    }

    operator fun set(index: Long, value: IntArray, offset: Int, length: Int) {
        nio.asIntBuffer().put(index.toInt(), value, offset, length)
    }

    operator fun set(index: Long, value: LongArray, offset: Int, length: Int) {
        nio.asLongBuffer().put(index.toInt(), value, offset, length)
    }

    operator fun set(index: Long, value: FloatArray, offset: Int, length: Int) {
        nio.asFloatBuffer().put(index.toInt(), value, offset, length)
    }

    operator fun set(index: Long, value: DoubleArray, offset: Int, length: Int) {
        nio.asDoubleBuffer().put(index.toInt(), value, offset, length)
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
    fun read(offset: Long = 0L): DataInput {
        return object : DataInput {
            var index = offset

            fun index(increment: Long): Long {
                val i = index
                index += increment
                return i
            }

            override fun readFully(b: ByteArray) {
                readFully(b, 0, b.size)
            }

            override fun readFully(b: ByteArray, off: Int, len: Int) {
                nio.put(index(len.toLong()).toInt(), b, off, len)
            }

            override fun skipBytes(n: Int): Int {
                index += n
                return n
            }

            override fun readBoolean(): Boolean {
                return get(index(1)) == 1.toByte()
            }

            override fun readByte(): Byte {
                return get(index(1))
            }

            override fun readUnsignedByte(): Int {
                return get(index(1)).toUByte().toInt()
            }

            override fun readShort(): Short {
                return getShort(index(2))
            }

            override fun readUnsignedShort(): Int {
                return getShort(index(2)).toUShort().toInt()
            }

            override fun readChar(): Char {
                return getShort(index(2)).toInt().toChar()
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

            override fun readLine(): String {
                val builder = StringBuilder()
                var available = size - index

                loop@ do {
                    val c = readUnsignedByte().toChar()
                    --available

                    when (c) {
                        '\n' -> break@loop

                        '\r' -> {
                            if (available > 0 && get(index).toInt().toChar() == '\n') {
                                index++
                                --available
                            }
                            break@loop
                        }

                        else -> {
                            builder.append(c)
                        }
                    }
                } while (available > 0)

                return builder.toString()
            }

            override fun readUTF() = DataInputStream.readUTF(this)
        }
    }

    @JvmOverloads
    fun write(offset: Long = 0L): DataOutput {
        return object : DataOutput {
            var index = offset

            fun index(increment: Long): Long {
                val i = index
                index += increment
                return i
            }

            override fun write(b: Int) {
                set(index(1), b.toByte())
            }

            override fun write(b: ByteArray) {
                write(b, 0, b.size)
            }

            override fun write(b: ByteArray, off: Int, len: Int) {
                set(index(len.toLong()), b, off, len)
            }

            override fun writeBoolean(v: Boolean) {
                set(index(1), if (v) 1 else 0)
            }

            override fun writeByte(v: Int) {
                set(index(1), v.toByte())
            }

            override fun writeShort(v: Int) {
                set(index(2), v.toShort())
            }

            override fun writeChar(v: Int) {
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

            override fun writeBytes(s: String) {
                write(s.toByteArray())
            }

            override fun writeChars(s: String) {
                s.toCharArray().forEach { set(index(2), it.code.toShort()) }
            }

            override fun writeUTF(str: String) {
                throw UnsupportedOperationException()
            }
        }
    }

    fun asByteBuffer(): ByteBuffer = nio.duplicate()

    open class Nio(
        override val nio: ByteBuffer
    ) : NeoBuffer() {
        override val address: Long = memAddress(nio)
        override val size: Long = (nio.limit() - nio.position()).toLong()
    }

    open class Native(
        override val address: Long,
        override val size: Long
    ) : NeoBuffer(), NativeResource {
        var isFreed: Boolean = false
            protected set
        override val nio: ByteBuffer = memByteBuffer(address, size.toInt())

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