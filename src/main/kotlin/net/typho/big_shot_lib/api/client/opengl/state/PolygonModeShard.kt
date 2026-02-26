package net.typho.big_shot_lib.api.client.opengl.state

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.typho.big_shot_lib.api.client.opengl.util.GlBindable
import net.typho.big_shot_lib.api.util.resources.NeoCodecs
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

open class PolygonModeShard(
    @JvmField
    val mode: PolygonMode
) : RenderSettingShard.Basic(
    PolygonModeShard,
    listOf(GlBindable.ofStack(GlStateStack.polygonMode, mode))
) {
    companion object : RenderSettingShard.Type<PolygonModeShard> {
        override fun getDefault() = PolygonModeShard(PolygonMode.FILL)

        override fun codec(): MapCodec<PolygonModeShard> = RecordCodecBuilder.mapCodec {
            it.group(
                NeoCodecs.enumCodec<PolygonMode>().fieldOf("mode").forGetter { shard -> shard.mode }
            ).apply(it, ::PolygonModeShard)
        }

        override val location = ResourceIdentifier("opengl", "polygon_mode")
    }
}