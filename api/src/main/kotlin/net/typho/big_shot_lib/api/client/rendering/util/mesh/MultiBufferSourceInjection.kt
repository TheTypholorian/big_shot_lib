package net.typho.big_shot_lib.api.client.rendering.util

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.RenderType

interface MultiBufferSourceInjection {
    fun getBuffer(settings: RenderType): VertexConsumer? = null

    fun endBatch(settings: RenderType) {
    }
}