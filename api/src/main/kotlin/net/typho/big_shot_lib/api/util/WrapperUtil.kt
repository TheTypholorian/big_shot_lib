package net.typho.big_shot_lib.api.util

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.block.model.BakedQuad
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexConsumer
import net.typho.big_shot_lib.api.client.rendering.util.MultiBufferSourceInjection
import net.typho.big_shot_lib.api.client.rendering.util.NeoMultiBufferSource
import net.typho.big_shot_lib.api.client.rendering.util.quad.NeoBakedQuad

interface WrapperUtil {
    fun wrap(quad: BakedQuad): NeoBakedQuad

    fun wrap(consumer: VertexConsumer): NeoVertexConsumer

    fun unwrap(consumer: NeoVertexConsumer): VertexConsumer

    fun inject(vanilla: MultiBufferSource.BufferSource, injection: MultiBufferSourceInjection): MultiBufferSource.BufferSource

    fun uninject(vanilla: MultiBufferSource.BufferSource, injection: MultiBufferSourceInjection): MultiBufferSource.BufferSource

    fun wrap(source: MultiBufferSource): NeoMultiBufferSource

    fun unwrap(source: NeoMultiBufferSource): MultiBufferSource

    companion object {
        @JvmStatic
        @get:JvmName("getInstance")
        val INSTANCE by lazy { WrapperUtil::class.loadService() }
    }
}