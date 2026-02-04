package net.typho.big_shot_lib.api.util

import java.nio.*

object BufferUtil {
    @JvmStatic
    fun ByteBuffer.concat(other: ByteBuffer, order: ByteOrder = ByteOrder.nativeOrder()): ByteBuffer {
        return ByteBuffer.allocate(capacity() + other.capacity())
            .order(order)
            .put(this)
            .put(other)
    }

    @JvmStatic
    fun ShortBuffer.concat(other: ShortBuffer): ShortBuffer {
        return ShortBuffer.allocate(capacity() + other.capacity())
            .put(this)
            .put(other)
    }

    @JvmStatic
    fun IntBuffer.concat(other: IntBuffer): IntBuffer {
        return IntBuffer.allocate(capacity() + other.capacity())
            .put(this)
            .put(other)
    }

    @JvmStatic
    fun LongBuffer.concat(other: LongBuffer): LongBuffer {
        return LongBuffer.allocate(capacity() + other.capacity())
            .put(this)
            .put(other)
    }

    @JvmStatic
    fun FloatBuffer.concat(other: FloatBuffer): FloatBuffer {
        return FloatBuffer.allocate(capacity() + other.capacity())
            .put(this)
            .put(other)
    }

    @JvmStatic
    fun DoubleBuffer.concat(other: DoubleBuffer): DoubleBuffer {
        return DoubleBuffer.allocate(capacity() + other.capacity())
            .put(this)
            .put(other)
    }

    @JvmStatic
    fun wrapDirect(bytes: ByteArray): ByteBuffer {
        return ByteBuffer.allocateDirect(bytes.size)
            .put(bytes)
            .flip()
    }
}