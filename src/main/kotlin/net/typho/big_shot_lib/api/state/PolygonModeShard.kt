package net.typho.big_shot_lib.api.state

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.api.Bindable
import net.typho.big_shot_lib.api.util.NeoCodecs

open class PolygonModeShard(
    @JvmField
    val mode: PolygonMode
) : RenderSettingShard.Basic(
    PolygonModeShard,
    listOf(Bindable.ofState(OpenGL.INSTANCE::polygonMode, mode, PolygonMode.FILL))
) {
    companion object : RenderSettingShard.Type<PolygonModeShard> {
        override fun getDefault() = PolygonModeShard(PolygonMode.FILL)

        override fun codec(): MapCodec<PolygonModeShard> = RecordCodecBuilder.mapCodec {
            it.group(
                NeoCodecs.enumCodec<PolygonMode>().fieldOf("mode").forGetter { shard -> shard.mode }
            ).apply(it, ::PolygonModeShard)
        }

        override fun location(): ResourceLocation = ResourceLocation.fromNamespaceAndPath("opengl", "polygon_mode")
    }
}