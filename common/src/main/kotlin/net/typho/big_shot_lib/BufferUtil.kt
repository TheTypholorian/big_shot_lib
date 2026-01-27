package net.typho.big_shot_lib

import net.typho.big_shot_lib.shaders.mixins.ShaderMixinContext
import java.nio.*

object BufferUtil {
    @JvmStatic
    fun concat(first: ByteBuffer, second: ByteBuffer, order: ByteOrder = ShaderMixinContext.BYTE_ORDER): ByteBuffer {
        return ByteBuffer.allocate(first.capacity() + second.capacity())
            .order(order)
            .put(first)
            .put(second)
    }

    @JvmStatic
    fun concat(first: ShortBuffer, second: ShortBuffer): ShortBuffer {
        return ShortBuffer.allocate(first.capacity() + second.capacity())
            .put(first)
            .put(second)
    }

    @JvmStatic
    fun concat(first: IntBuffer, second: IntBuffer): IntBuffer {
        return IntBuffer.allocate(first.capacity() + second.capacity())
            .put(first)
            .put(second)
    }

    @JvmStatic
    fun concat(first: LongBuffer, second: LongBuffer): LongBuffer {
        return LongBuffer.allocate(first.capacity() + second.capacity())
            .put(first)
            .put(second)
    }

    @JvmStatic
    fun concat(first: FloatBuffer, second: FloatBuffer): FloatBuffer {
        return FloatBuffer.allocate(first.capacity() + second.capacity())
            .put(first)
            .put(second)
    }

    @JvmStatic
    fun concat(first: DoubleBuffer, second: DoubleBuffer): DoubleBuffer {
        return DoubleBuffer.allocate(first.capacity() + second.capacity())
            .put(first)
            .put(second)
    }

    @JvmStatic
    fun wrapDirect(bytes: ByteArray): ByteBuffer {
        return ByteBuffer.allocateDirect(bytes.size)
            .put(bytes)
            .flip()
    }
}