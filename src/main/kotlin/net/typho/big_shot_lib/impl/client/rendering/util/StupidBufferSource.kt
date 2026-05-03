package net.typho.big_shot_lib.impl.client.rendering.util

//? if <1.21.9 {
import net.minecraft.client.renderer.RenderType
//? } else {
/*import net.minecraft.client.renderer.rendertype.RenderType
*///? }

import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.MultiBufferSource
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderSettings
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexConsumer
import net.typho.big_shot_lib.api.util.WrapperUtil
import net.typho.big_shot_lib.impl.util.neo

data class StupidBufferSource(
    @JvmField
    val get: (renderType: RenderType) -> VertexConsumer,
    @JvmField
    val end: (renderType: RenderType) -> Unit
) : MultiBufferSource.BufferSource(BufferBuilder(0), mapOf()) {
    constructor(
        get: (settings: NeoRenderSettings) -> NeoVertexConsumer,
        end: (settings: NeoRenderSettings) -> Unit
    ) : this(
        { renderType: RenderType -> WrapperUtil.INSTANCE.unwrap(get(renderType.neo)) },
        { renderType: RenderType -> end(renderType.neo) }
    )

    override fun getBuffer(renderType: RenderType): VertexConsumer {
        return get(renderType)
    }

    override fun endBatch(renderType: RenderType) {
        end(renderType)
    }
}