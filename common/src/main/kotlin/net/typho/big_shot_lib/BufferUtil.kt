package net.typho.big_shot_lib

import net.typho.big_shot_lib.spirv.ShaderMixinContext
import java.nio.ByteBuffer
import java.nio.ByteOrder

object BufferUtil {
    @JvmStatic
    fun concat(first: ByteBuffer, second: ByteBuffer, order: ByteOrder = ShaderMixinContext.BYTE_ORDER): ByteBuffer {
        return ByteBuffer.allocate(first.capacity() + second.capacity())
            .order(order)
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