package net.typho.big_shot_lib.api.client.opengl.state

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.typho.big_shot_lib.api.client.opengl.util.GlBindable
import net.typho.big_shot_lib.api.util.resources.NeoCodecs
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

open class EnableFlagsShard(
    @JvmField
    val flags: List<GlFlag>
) : RenderSettingShard.Basic(
    EnableFlagsShard,
    flags.map { GlBindable.ofStack(it.stack, true) }
) {
    companion object : RenderSettingShard.Type<EnableFlagsShard> {
        override fun getDefault() = EnableFlagsShard(listOf())

        override fun codec(): MapCodec<EnableFlagsShard> = RecordCodecBuilder.mapCodec {
            it.group(
                NeoCodecs.enumCodec<GlFlag>().listOf().fieldOf("flags").forGetter { shard -> shard.flags },
            ).apply(it, ::EnableFlagsShard)
        }

        override fun location(): ResourceIdentifier = ResourceIdentifier("opengl", "enable_flags")
    }
}