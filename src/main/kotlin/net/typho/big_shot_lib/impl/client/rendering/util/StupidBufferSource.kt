package net.typho.big_shot_lib.impl.client.rendering.util

//? if <1.21.11 {
import net.minecraft.client.renderer.RenderType
//? } else {
/*import net.minecraft.client.renderer.rendertype.RenderType
*///? }

//? if <1.21 {
import com.mojang.blaze3d.vertex.BufferBuilder
//? } else {
/*import com.mojang.blaze3d.vertex.ByteBufferBuilder
import it.unimi.dsi.fastutil.objects.Object2ObjectSortedMaps
*///? }

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.MultiBufferSource

//? if <1.21 {
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
//? } else {
/*data class StupidBufferSource(
    @JvmField
    val get: (renderType: RenderType) -> VertexConsumer,
    @JvmField
    val end: (renderType: RenderType) -> Unit
) : MultiBufferSource.BufferSource(ByteBufferBuilder(0), Object2ObjectSortedMaps.emptyMap()) {
    override fun getBuffer(renderType: RenderType): VertexConsumer {
        return get(renderType)
    }

    override fun endBatch(renderType: RenderType) {
        end(renderType)
    }
}
*///? }