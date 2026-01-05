package net.typho.big_shot_lib

import net.typho.big_shot_lib.spirv.ShaderMixinContext
import java.nio.ByteBuffer
import java.nio.ByteOrder

object BufferUtil {
    @JvmStatic
    fun concat(first: ByteBuffer, second: ByteBuffer, order: ByteOrder = ShaderMixinContext.BYTE_ORDER): ByteBuffer {
        val buffer = ByteBuffer.allocate(first.capacity() + second.capacity())
            .order(order)
        buffer.put(first)
        buffer.put(second)
        return buffer
    }
}