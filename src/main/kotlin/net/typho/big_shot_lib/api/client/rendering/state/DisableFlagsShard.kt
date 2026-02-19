package net.typho.big_shot_lib.api.client.rendering.state

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.typho.big_shot_lib.api.client.rendering.util.GlBindable
import net.typho.big_shot_lib.api.util.resources.NeoCodecs
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

open class DisableFlagsShard(
    @JvmField
    val flags: List<GlFlag>
) : RenderSettingShard.Basic(
    DisableFlagsShard,
    flags.map { GlBindable.ofStack(it.stack, false) }
) {
    companion object : RenderSettingShard.Type<DisableFlagsShard> {
        override fun getDefault() = DisableFlagsShard(listOf())

        override fun codec(): MapCodec<DisableFlagsShard> = RecordCodecBuilder.mapCodec {
            it.group(
                NeoCodecs.enumCodec<GlFlag>().listOf().fieldOf("flags").forGetter { shard -> shard.flags },
            ).apply(it, ::DisableFlagsShard)
        }

        override fun location(): ResourceIdentifier = ResourceIdentifier("opengl", "disable_flags")
    }
}