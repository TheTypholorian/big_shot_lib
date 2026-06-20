package net.typho.big_shot_lib.api.client.rendering.util

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.blaze3d.vertex.VertexFormatElement
import com.mojang.serialization.Codec
import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.client.InternalClientUtil
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlDataType
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlVertexElementReadType

object NeoVertexFormats {
    @JvmField
    val REGISTRY: BiMap<Identifier, VertexFormat> = HashBiMap.create()
    @JvmField
    val CODEC: Codec<VertexFormat> = Identifier.CODEC.xmap(
        { REGISTRY[it] },
        { REGISTRY.inverse()[it] }
    )

    @JvmField
    val ELEMENT_REGISTRY: BiMap<Identifier, VertexFormatElement> = HashBiMap.create()
    @JvmField
    val ELEMENT_CODEC: Codec<VertexFormatElement> = Identifier.CODEC.xmap(
        { ELEMENT_REGISTRY[it] },
        { ELEMENT_REGISTRY.inverse()[it] }
    )

    @JvmStatic
    fun register(location: Identifier, format: VertexFormat) {
        REGISTRY[location] = format
    }

    @JvmStatic
    fun register(location: Identifier, format: VertexFormatElement) {
        ELEMENT_REGISTRY[location] = format
    }

    @JvmStatic
    fun builder(location: Identifier): VertexFormat.Builder = InternalClientUtil.INSTANCE.createRegisteredVertexFormatBuilder(location)

    @JvmStatic
    fun element(index: Int, inType: GlDataType, outType: GlVertexElementReadType, count: Int): VertexFormatElement = InternalClientUtil.INSTANCE.createVertexFormatElement(index, inType, outType, count)

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

        register(Identifier.minecraft("position"), VertexFormatElement.POSITION)
        register(Identifier.minecraft("color"), VertexFormatElement.COLOR)
        register(Identifier.minecraft("texture_uv"), VertexFormatElement.UV0)
        register(Identifier.minecraft("overlay_uv"), VertexFormatElement.UV1)
        register(Identifier.minecraft("light_uv"), VertexFormatElement.UV2)
        register(Identifier.minecraft("normal"), VertexFormatElement.NORMAL)
    }
}