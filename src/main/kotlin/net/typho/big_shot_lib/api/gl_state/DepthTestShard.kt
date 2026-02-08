package net.typho.big_shot_lib.api.gl_state

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.api.Bindable
import net.typho.big_shot_lib.api.StateManager
import net.typho.big_shot_lib.api.util.NeoCodecs

open class DepthTestShard(
    @JvmField
    val enabled: Boolean,
    @JvmField
    val func: DepthFunction
) : RenderSettingShard.Basic(
    DepthTestShard,
    if (enabled) listOf(
        GlFlag.DEPTH_TEST.bindable,
        Bindable.ofState(StateManager::depthFunc, func, DepthFunction.LEQUAL)
    ) else listOf()
) {
    companion object : RenderSettingShard.Type<DepthTestShard> {
        override fun getDefault() = DepthTestShard(
            false,
            DepthFunction.LEQUAL
        )

        override fun codec(): MapCodec<DepthTestShard> = RecordCodecBuilder.mapCodec {
            it.group(
                NeoCodecs.enumCodec<DepthFunction>().fieldOf("func").forGetter { shard -> shard.func }
            ).apply(it) { func -> DepthTestShard(true, func) }
        }

        override fun location(): ResourceLocation = ResourceLocation.fromNamespaceAndPath("opengl", "depth_test")
    }
}