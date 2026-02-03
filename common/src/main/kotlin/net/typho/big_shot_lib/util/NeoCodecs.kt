package net.typho.big_shot_lib.util

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.blaze3d.vertex.VertexFormatElement
import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec

object NeoCodecs {
    @JvmField
    val VERTEX_FORMAT_MAP: MutableMap<String, VertexFormat> = LinkedHashMap()
    @JvmField
    val VERTEX_FORMAT_ELEMENT_MAP: BiMap<String, VertexFormatElement> = HashBiMap.create()
    @JvmField
    val VERTEX_FORMAT_CODEC: Codec<VertexFormat> = Codec.either(
        Codec.STRING.xmap(
            { key -> VERTEX_FORMAT_MAP[key]!! },
            { format -> VERTEX_FORMAT_MAP.entries.first { entry -> entry.value == format }.key }
        ),
        Codec.unboundedMap(Codec.STRING, Codec.STRING).xmap(
            { map ->
                val builder = VertexFormat.builder()

                for (entry in map) {
                    builder.add(entry.key, VERTEX_FORMAT_ELEMENT_MAP[entry.value]!!)
                }

                return@xmap builder.build()
            },
            { format ->
                format.elements.associateBy { element ->
                    format.getElementName(element)
                }.mapValues { format -> VERTEX_FORMAT_ELEMENT_MAP.inverse()[format.value] }
            }
        )
    ).xmap(
        { either -> either.map({l -> l}, {r -> r}) },
        { format -> if (VERTEX_FORMAT_MAP.containsValue(format)) Either.left(format) else Either.right(format) }
    )
    @JvmField
    val VERTEX_FORMAT_MODE_CODEC: Codec<VertexFormat.Mode> = Codec.STRING.xmap(
        { key -> enumValueOf<VertexFormat.Mode>(key) },
        { mode -> mode.name }
    )

    init {
        VERTEX_FORMAT_MAP["POSITION_COLOR_TEXTURE_LIGHT_NORMAL"] = DefaultVertexFormat.BLOCK
        VERTEX_FORMAT_MAP["POSITION_COLOR_TEX_LIGHTMAP_NORMAL"] = DefaultVertexFormat.BLOCK
        VERTEX_FORMAT_MAP["BLOCK"] = DefaultVertexFormat.BLOCK

        VERTEX_FORMAT_MAP["POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL"] = DefaultVertexFormat.NEW_ENTITY
        VERTEX_FORMAT_MAP["POSITION_COLOR_TEX_OVERLAY_LIGHTMAP_NORMAL"] = DefaultVertexFormat.NEW_ENTITY
        VERTEX_FORMAT_MAP["NEW_ENTITY"] = DefaultVertexFormat.NEW_ENTITY

        VERTEX_FORMAT_MAP["POSITION_TEXTURE_COLOR_LIGHT"] = DefaultVertexFormat.PARTICLE
        VERTEX_FORMAT_MAP["POSITION_TEX_COLOR_LIGHTMAP"] = DefaultVertexFormat.PARTICLE
        VERTEX_FORMAT_MAP["PARTICLE"] = DefaultVertexFormat.PARTICLE

        VERTEX_FORMAT_MAP["POSITION"] = DefaultVertexFormat.POSITION

        VERTEX_FORMAT_MAP["BLIT_SCREEN"] = DefaultVertexFormat.BLIT_SCREEN

        VERTEX_FORMAT_MAP["POSITION_COLOR"] = DefaultVertexFormat.POSITION_COLOR

        VERTEX_FORMAT_MAP["LINES"] = DefaultVertexFormat.POSITION_COLOR_NORMAL
        VERTEX_FORMAT_MAP["POSITION_COLOR_NORMAL"] = DefaultVertexFormat.POSITION_COLOR_NORMAL

        VERTEX_FORMAT_MAP["POSITION_COLOR_LIGHT"] = DefaultVertexFormat.POSITION_COLOR_LIGHTMAP
        VERTEX_FORMAT_MAP["POSITION_COLOR_LIGHTMAP"] = DefaultVertexFormat.POSITION_COLOR_LIGHTMAP

        VERTEX_FORMAT_MAP["POSITION_TEXTURE"] = DefaultVertexFormat.POSITION_TEX
        VERTEX_FORMAT_MAP["POSITION_TEX"] = DefaultVertexFormat.POSITION_TEX

        VERTEX_FORMAT_MAP["POSITION_TEXTURE_COLOR"] = DefaultVertexFormat.POSITION_TEX_COLOR
        VERTEX_FORMAT_MAP["POSITION_TEX_COLOR"] = DefaultVertexFormat.POSITION_TEX_COLOR

        VERTEX_FORMAT_MAP["POSITION_COLOR_TEXTURE_LIGHT"] = DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP
        VERTEX_FORMAT_MAP["POSITION_COLOR_TEX_LIGHTMAP"] = DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP

        VERTEX_FORMAT_MAP["POSITION_TEXTURE_LIGHT_COLOR"] = DefaultVertexFormat.POSITION_TEX_LIGHTMAP_COLOR
        VERTEX_FORMAT_MAP["POSITION_TEX_LIGHTMAP_COLOR"] = DefaultVertexFormat.POSITION_TEX_LIGHTMAP_COLOR

        VERTEX_FORMAT_MAP["POSITION_TEXTURE_COLOR_NORMAL"] = DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL
        VERTEX_FORMAT_MAP["POSITION_TEX_COLOR_NORMAL"] = DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL

        VERTEX_FORMAT_ELEMENT_MAP["POSITION"] = VertexFormatElement.POSITION

        VERTEX_FORMAT_ELEMENT_MAP["COLOR"] = VertexFormatElement.COLOR

        VERTEX_FORMAT_ELEMENT_MAP["UV0"] = VertexFormatElement.UV0
        VERTEX_FORMAT_ELEMENT_MAP["UV_0"] = VertexFormatElement.UV0

        VERTEX_FORMAT_ELEMENT_MAP["UV1"] = VertexFormatElement.UV1
        VERTEX_FORMAT_ELEMENT_MAP["UV_1"] = VertexFormatElement.UV1

        VERTEX_FORMAT_ELEMENT_MAP["UV2"] = VertexFormatElement.UV2
        VERTEX_FORMAT_ELEMENT_MAP["UV_2"] = VertexFormatElement.UV2

        VERTEX_FORMAT_ELEMENT_MAP["NORMAL"] = VertexFormatElement.NORMAL
    }
}