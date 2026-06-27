package net.typho.big_shot_lib.api.client.rendering.util.mesh

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.serialization.Codec
import net.minecraft.resources.Identifier

object NeoVertexFormats {
    @JvmField
    val REGISTRY: BiMap<Identifier, VertexFormat> = HashBiMap.create()
    @JvmField
    val LOOKUP_CODEC: Codec<VertexFormat> = Identifier.CODEC.xmap(
        { REGISTRY[it] },
        { REGISTRY.inverse()[it] }
    )

    @JvmStatic
    fun register(location: Identifier, format: VertexFormat) {
        REGISTRY[location] = format
    }

    init {
        register(Identifier.minecraft("block"), DefaultVertexFormat.BLOCK)
        register(Identifier.minecraft("entity"), DefaultVertexFormat.ENTITY)
        register(Identifier.minecraft("particle"), DefaultVertexFormat.PARTICLE)
        register(Identifier.minecraft("position"), DefaultVertexFormat.POSITION)
        register(Identifier.minecraft("position_color"), DefaultVertexFormat.POSITION_COLOR)
        register(Identifier.minecraft("position_color_normal"), DefaultVertexFormat.POSITION_COLOR_NORMAL)
        register(Identifier.minecraft("position_color_lightmap"), DefaultVertexFormat.POSITION_COLOR_LIGHTMAP)
        register(Identifier.minecraft("position_tex"), DefaultVertexFormat.POSITION_TEX)
        register(Identifier.minecraft("position_tex_color"), DefaultVertexFormat.POSITION_TEX_COLOR)
        register(Identifier.minecraft("position_color_tex_lightmap"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP)
        register(Identifier.minecraft("position_tex_lightmap_color"), DefaultVertexFormat.POSITION_TEX_LIGHTMAP_COLOR)
        register(Identifier.minecraft("position_tex_color_normal"), DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL)
    }
}