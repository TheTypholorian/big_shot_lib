package net.typho.big_shot_lib.api.client.rendering.util

import net.minecraft.client.renderer.RenderType

interface MultiBufferSourceInjection {
    fun getBuffer(settings: RenderType): NeoVertexConsumer?

    fun endBatch(settings: RenderType)
}