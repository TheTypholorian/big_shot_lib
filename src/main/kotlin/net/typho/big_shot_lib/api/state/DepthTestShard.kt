package net.typho.big_shot_lib.api.state

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.ResourceIdentifier
import net.typho.big_shot_lib.api.util.Bindable
import net.typho.big_shot_lib.api.util.NeoCodecs

open class DepthTestShard(
    @JvmField
    val enabled: Boolean,
    @JvmField
    val func: ComparisonMode
) : RenderSettingShard.Basic(
    DepthTestShard,
    if (enabled) listOf(
        GlFlag.DEPTH_TEST.bindable,
        Bindable.ofState(OpenGL.INSTANCE::depthFunc, func, ComparisonMode.LEQUAL)
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

        override fun location(): ResourceIdentifier = ResourceIdentifier.fromNamespaceAndPath("opengl", "depth_test")
    }
}