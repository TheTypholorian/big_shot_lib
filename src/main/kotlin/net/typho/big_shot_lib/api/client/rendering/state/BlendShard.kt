package net.typho.big_shot_lib.api.client.rendering.state

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.typho.big_shot_lib.api.client.rendering.util.GlBindable
import net.typho.big_shot_lib.api.util.IColor
import net.typho.big_shot_lib.api.util.resources.NeoCodecs
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

open class BlendShard(
    @JvmField
    val enabled: Boolean,
    @JvmField
    val color: IColor,
    @JvmField
    val equation: BlendEquation,
    @JvmField
    val function: BlendFunction
) : RenderSettingShard.Basic(
    BlendShard,
    if (enabled) listOf(
        GlBindable.ofStack(GlFlag.BLEND.stack, true),
        GlBindable.ofStack(GlStateStack.blendColor, color),
        equation,
        function
    ) else listOf(GlBindable.ofStack(GlFlag.BLEND.stack, false))
) {
    companion object : RenderSettingShard.Type<BlendShard> {
        override fun getDefault() = BlendShard(
            false,
            IColor.FULL_OFF,
            BlendEquation.ADD,
            BlendFunction.Basic(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA)
        )

        override fun codec(): MapCodec<BlendShard> = RecordCodecBuilder.mapCodec {
            it.group(
                IColor.CODEC_ANY.fieldOf("color").forGetter { shard -> shard.color },
                NeoCodecs.enumCodec<BlendEquation>().fieldOf("equation").forGetter { shard -> shard.equation },
                BlendFunction.CODEC.fieldOf("function").forGetter { shard -> shard.function },
            ).apply(it) { color, eq, func -> BlendShard(true, color, eq, func) }
        }

        override fun location(): ResourceIdentifier = ResourceIdentifier("opengl", "blend")
    }
}