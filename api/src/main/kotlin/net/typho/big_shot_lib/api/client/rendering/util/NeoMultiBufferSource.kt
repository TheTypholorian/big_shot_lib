package net.typho.big_shot_lib.api.client.rendering.util

fun interface NeoMultiBufferSource {
    fun getBuffer(renderType: NeoRenderType): NeoVertexConsumer
}