package net.typho.big_shot_lib.impl.client.rendering.util

//? if <1.21.9 {
import net.minecraft.client.renderer.RenderType
//? } else {
/*import net.minecraft.client.renderer.rendertype.RenderType
*///? }

import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.MultiBufferSource

data class StupidBufferSource(
    @JvmField
    val get: (renderType: RenderType) -> VertexConsumer,
    @JvmField
    val end: (renderType: RenderType) -> Unit
) : MultiBufferSource.BufferSource(BufferBuilder(0), mapOf()) {
    override fun getBuffer(renderType: RenderType): VertexConsumer {
        return get(renderType)
    }

    override fun endBatch(renderType: RenderType) {
        end(renderType)
    }
}