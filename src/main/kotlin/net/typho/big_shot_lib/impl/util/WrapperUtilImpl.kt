package net.typho.big_shot_lib.impl.util

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.block.model.BakedQuad
import net.typho.big_shot_lib.api.client.rendering.util.MultiBufferSourceInjection
import net.typho.big_shot_lib.api.client.rendering.util.NeoMultiBufferSource
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexConsumer
import net.typho.big_shot_lib.api.client.rendering.util.quad.NeoBakedQuad
import net.typho.big_shot_lib.api.util.WrapperUtil
import net.typho.big_shot_lib.api.util.getExtensionValue
import net.typho.big_shot_lib.impl.client.rendering.util.NeoVertexConsumerWrapper
import net.typho.big_shot_lib.impl.client.rendering.util.VertexConsumerWrapper

object WrapperUtilImpl : WrapperUtil {
    override fun wrap(
        quad: BakedQuad
    ): NeoBakedQuad {
        return quad.getExtensionValue()
    }

    override fun wrap(consumer: VertexConsumer): NeoVertexConsumer {
        return NeoVertexConsumerWrapper(consumer)
    }

    override fun unwrap(consumer: NeoVertexConsumer): VertexConsumer {
        return VertexConsumerWrapper(consumer)
    }

    override fun inject(
        vanilla: MultiBufferSource.BufferSource,
        injection: MultiBufferSourceInjection
    ): MultiBufferSource.BufferSource {
        val injections = vanilla.getExtensionValue<MutableList<MultiBufferSourceInjection>>()
        injections.add(injection)
        return vanilla
    }

    override fun uninject(
        vanilla: MultiBufferSource.BufferSource,
        injection: MultiBufferSourceInjection
    ): MultiBufferSource.BufferSource {
        val injections = vanilla.getExtensionValue<MutableList<MultiBufferSourceInjection>>()
        injections.remove(injection)
        return vanilla
    }

    override fun wrap(source: MultiBufferSource): NeoMultiBufferSource {
        return NeoMultiBufferSource { renderType -> wrap(source.getBuffer(renderType.getExtensionValue())) }
    }

    override fun unwrap(source: NeoMultiBufferSource): MultiBufferSource {
        return MultiBufferSource { renderType -> unwrap(source.getBuffer(renderType.getExtensionValue())) }
    }
}