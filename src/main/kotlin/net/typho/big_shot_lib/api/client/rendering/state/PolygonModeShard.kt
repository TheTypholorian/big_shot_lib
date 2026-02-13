package net.typho.big_shot_lib.api.client.rendering.state

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.typho.big_shot_lib.api.client.rendering.util.GlBindable
import net.typho.big_shot_lib.api.util.resources.NeoCodecs
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

open class PolygonModeShard(
    @JvmField
    val mode: PolygonMode
) : RenderSettingShard.Basic(
    PolygonModeShard,
    listOf(GlBindable.ofState(OpenGL.INSTANCE::polygonMode, mode, PolygonMode.FILL))
) {
    companion object : RenderSettingShard.Type<PolygonModeShard> {
        override fun getDefault() = PolygonModeShard(PolygonMode.FILL)

        override fun codec(): MapCodec<PolygonModeShard> = RecordCodecBuilder.mapCodec {
            it.group(
                NeoCodecs.enumCodec<PolygonMode>().fieldOf("mode").forGetter { shard -> shard.mode }
            ).apply(it, ::PolygonModeShard)
        }

        override fun location(): ResourceIdentifier = ResourceIdentifier("opengl", "polygon_mode")
    }
}