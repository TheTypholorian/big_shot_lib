package net.typho.big_shot_lib.api.render_queue.shards

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.api.Bindable
import net.typho.big_shot_lib.api.GlManager
import net.typho.big_shot_lib.api.render_queue.RenderSettingShard
import net.typho.big_shot_lib.api.util.NeoCodecs

open class DepthShard(
    @JvmField
    val enabled: Boolean,
    @JvmField
    val func: DepthFunction
) : RenderSettingShard.Basic(
    DepthShard,
    if (enabled) listOf(
        GlFlag.DEPTH_TEST.bindable,
        Bindable.ofState(GlManager::depthFunc, func, DepthFunction.LEQUAL)
    ) else listOf()
) {
    companion object : RenderSettingShard.Type<DepthShard> {
        override fun getDefault() = DepthShard(
            false,
            DepthFunction.LEQUAL
        )

        override fun codec(): MapCodec<DepthShard> = RecordCodecBuilder.mapCodec {
            it.group(
                NeoCodecs.enumCodec<DepthFunction>().fieldOf("func").forGetter { shard -> shard.func }
            ).apply(it) { func -> DepthShard(true, func) }
        }

        override fun location(): ResourceLocation = ResourceLocation.fromNamespaceAndPath("opengl", "depth_test")
    }
}