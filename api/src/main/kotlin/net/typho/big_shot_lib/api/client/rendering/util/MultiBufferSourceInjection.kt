package net.typho.big_shot_lib.api.client.rendering.util

interface MultiBufferSourceInjection {
    fun getBuffer(settings: NeoRenderSettings): NeoVertexConsumer?

    fun endBatch(settings: NeoRenderSettings)
}