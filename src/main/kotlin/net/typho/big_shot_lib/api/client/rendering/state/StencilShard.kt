package net.typho.big_shot_lib.api.client.rendering.state

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.typho.big_shot_lib.api.client.rendering.util.GlBindable
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

open class StencilShard(
    @JvmField
    val enabled: Boolean,
    @JvmField
    val func: StencilFunc,
    @JvmField
    val mask: Int,
    @JvmField
    val op: StencilOp
) : RenderSettingShard.Basic(
    StencilShard,
    if (enabled) listOf(
        GlFlag.STENCIL_TEST.bindable,
        GlBindable.ofState(OpenGL.INSTANCE::stencilFunc, func),
        GlBindable.ofState(OpenGL.INSTANCE::stencilMask, mask),
        GlBindable.ofState(OpenGL.INSTANCE::stencilOp, op),
    ) else listOf()
) {
    companion object : RenderSettingShard.Type<StencilShard> {
        override fun getDefault() = StencilShard(
            false,
            StencilFunc(ComparisonFunc.ALWAYS, 0, 0),
            0,
            StencilOp(IntAction.KEEP, IntAction.KEEP, IntAction.KEEP)
        )

        override fun codec(): MapCodec<StencilShard> = RecordCodecBuilder.mapCodec {
            it.group(
                StencilFunc.CODEC.fieldOf("color").forGetter { shard -> shard.func },
                Codec.INT.fieldOf("equation").forGetter { shard -> shard.mask },
                StencilOp.CODEC.fieldOf("function").forGetter { shard -> shard.op },
            ).apply(it) { func, mask, op -> StencilShard(true, func, mask, op) }
        }

        override fun location(): ResourceIdentifier = ResourceIdentifier("opengl", "stencil")
    }
}