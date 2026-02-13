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
    val func: ComparisonMode
) : RenderSettingShard.Basic(
    DepthTestShard,
    if (enabled) listOf(
        GlFlag.DEPTH_TEST.bindable,
        GlBindable.ofState(OpenGL.Companion.INSTANCE::depthFunc, func, ComparisonMode.LEQUAL)
    ) else listOf()
) {
    companion object : RenderSettingShard.Type<DepthTestShard> {
        override fun getDefault() = DepthTestShard(
            false,
            ComparisonMode.LEQUAL
        )

        override fun codec(): MapCodec<DepthTestShard> = RecordCodecBuilder.mapCodec {
            it.group(
                NeoCodecs.enumCodec<ComparisonMode>().fieldOf("func").forGetter { shard -> shard.func }
            ).apply(it) { func -> DepthTestShard(true, func) }
        }

        override fun location(): ResourceIdentifier = ResourceIdentifier("opengl", "depth_test")
    }
}