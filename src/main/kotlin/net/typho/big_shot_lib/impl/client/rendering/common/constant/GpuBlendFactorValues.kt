package net.typho.big_shot_lib.impl.client.rendering.common.constant

import com.mojang.blaze3d.platform.BlendFactor
import com.mojang.blaze3d.platform.CompareOp
import com.mojang.serialization.Codec
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuAlphaFunction
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuBlendFactor
import net.typho.big_shot_lib.api.util.Extension.Companion.castTo
import net.typho.big_shot_lib.api.util.resource.NeoCodecs

object GpuBlendFactorValues : GpuBlendFactor.Values {
    override val codec: Codec<GpuBlendFactor> = NeoCodecs.enumCodec<BlendFactor>().xmap({ it }, { it.castTo<BlendFactor>() })
    override val zero: GpuBlendFactor = BlendFactor.ZERO
    override val one: GpuBlendFactor = BlendFactor.ONE
    override val srcColor: GpuBlendFactor = BlendFactor.SRC_COLOR
    override val oneMinusSrcColor: GpuBlendFactor = BlendFactor.ONE_MINUS_SRC_COLOR
    override val dstColor: GpuBlendFactor = BlendFactor.DST_COLOR
    override val oneMinusDstColor: GpuBlendFactor = BlendFactor.ONE_MINUS_DST_COLOR
    override val srcAlpha: GpuBlendFactor = BlendFactor.SRC_ALPHA
    override val oneMinusSrcAlpha: GpuBlendFactor = BlendFactor.ONE_MINUS_SRC_ALPHA
    override val dstAlpha: GpuBlendFactor = BlendFactor.DST_ALPHA
    override val oneMinusDstAlpha: GpuBlendFactor = BlendFactor.ONE_MINUS_DST_ALPHA
    override val constantColor: GpuBlendFactor = BlendFactor.CONSTANT_COLOR
    override val oneMinusConstantColor: GpuBlendFactor = BlendFactor.ONE_MINUS_CONSTANT_COLOR
    override val constantAlpha: GpuBlendFactor = BlendFactor.CONSTANT_ALPHA
    override val oneMinusConstantAlpha: GpuBlendFactor = BlendFactor.ONE_MINUS_CONSTANT_ALPHA
    override val srcAlphaSaturate: GpuBlendFactor = BlendFactor.SRC_ALPHA_SATURATE
}