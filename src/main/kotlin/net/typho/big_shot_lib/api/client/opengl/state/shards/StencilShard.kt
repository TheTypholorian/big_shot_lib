package net.typho.big_shot_lib.api.client.opengl.state.shards

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.typho.big_shot_lib.api.client.opengl.state.*
import net.typho.big_shot_lib.api.client.opengl.util.GlBindable
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
        GlBindable.ofStack(GlFlag.STENCIL_TEST.stack, true),
        GlBindable.ofStack(GlStateStack.stencilFunc, func),
        GlBindable.ofStack(GlStateStack.stencilMask, mask),
        GlBindable.ofStack(GlStateStack.stencilOp, op),
    ) else listOf(GlBindable.ofStack(GlFlag.STENCIL_TEST.stack, false))
) {
    companion object : RenderSettingShard.Type<StencilShard> {
        override val default = StencilShard(
            false,
            StencilFunc(ComparisonFunc.ALWAYS, 0, 0),
            0,
            StencilOp(IntAction.KEEP, IntAction.KEEP, IntAction.KEEP)
        )
        override val codec: MapCodec<StencilShard> = RecordCodecBuilder.mapCodec {
            it.group(
                StencilFunc.CODEC.fieldOf("color").forGetter { shard -> shard.func },
                Codec.INT.fieldOf("equation").forGetter { shard -> shard.mask },
                StencilOp.CODEC.fieldOf("function").forGetter { shard -> shard.op },
            ).apply(it) { func, mask, op -> StencilShard(true, func, mask, op) }
        }
        override val location = ResourceIdentifier("opengl", "stencil")
    }
}