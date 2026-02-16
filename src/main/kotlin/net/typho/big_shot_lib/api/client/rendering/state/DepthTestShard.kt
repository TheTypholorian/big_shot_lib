package net.typho.big_shot_lib.api.client.rendering.state

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.typho.big_shot_lib.api.client.rendering.util.GlBindable
import net.typho.big_shot_lib.api.util.resources.NeoCodecs
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

open class DepthTestShard(
    @JvmField
    val enabled: Boolean,
    @JvmField
    val func: ComparisonFunc
) : RenderSettingShard.Basic(
    DepthTestShard,
    if (enabled) listOf(
        GlBindable.ofStack(GlFlag.DEPTH_TEST.stack, true),
        GlBindable.ofStack(GlStateStack.depthFunc, func)
    ) else listOf(GlBindable.ofStack(GlFlag.DEPTH_TEST.stack, false))
) {
    companion object : RenderSettingShard.Type<DepthTestShard> {
        override fun getDefault() = DepthTestShard(
            false,
            ComparisonFunc.LEQUAL
        )

        override fun codec(): MapCodec<DepthTestShard> = RecordCodecBuilder.mapCodec {
            it.group(
                NeoCodecs.enumCodec<ComparisonFunc>().fieldOf("func").forGetter { shard -> shard.func }
            ).apply(it) { func -> DepthTestShard(true, func) }
        }

        override fun location(): ResourceIdentifier = ResourceIdentifier("opengl", "depth_test")
    }
}