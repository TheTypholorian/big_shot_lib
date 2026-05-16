package net.typho.big_shot_lib.api.client.rendering.util

interface MultiBufferSourceInjection {
    fun getBuffer(settings: NeoRenderType): NeoVertexConsumer?

    fun endBatch(settings: NeoRenderType)
}