package net.typho.big_shot_lib.api.client.rendering.util.quad

import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.blaze3d.vertex.VertexFormatElement
import net.typho.big_shot_lib.api.util.buffer.MemoryPointer
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil.memGetByte
import org.lwjgl.system.MemoryUtil.memGetFloat
import org.lwjgl.system.MemoryUtil.memGetShort

interface SimpleVertexConsumer : VertexConsumer {
    override fun setLineWidth(width: Float): VertexConsumer {
        return this
    }

    override fun push(
        stack: MemoryStack,
        ptr: Long,
        count: Int,
        format: VertexFormat
    ) {
        var offset = 0

        repeat(count) {
            for (element in format.elements) {
                val ptr = ptr + offset + format.getOffset(element)

                when (element) {
                    VertexFormatElement.POSITION -> vertex(memGetFloat(ptr), memGetFloat(ptr + 4), memGetFloat(ptr + 8))
                    VertexFormatElement.TEXTURE_UV -> textureUV(memGetFloat(ptr), memGetFloat(ptr + 4))
                    VertexFormatElement.OVERLAY_UV -> overlayUV(memGetShort(ptr).toInt(), memGetShort(ptr + 2).toInt())
                    VertexFormatElement.LIGHT_UV -> lightUV(memGetShort(ptr).toInt(), memGetShort(ptr + 2).toInt())
                    VertexFormatElement.NORMAL -> normal(memGetByte(ptr), memGetByte(ptr + 1), memGetByte(ptr + 2))
                    else -> custom(element) { output -> MemoryPointer.wrap(ptr, element.byteSize().toLong(), free = false).read().readTo(output, element.byteSize()) }
                }
            }

            offset += format.vertexSize
        }
    }
}