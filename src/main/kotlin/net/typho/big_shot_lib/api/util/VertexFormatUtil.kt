package net.typho.big_shot_lib.api.util

import com.google.gson.JsonElement
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.blaze3d.vertex.VertexFormatElement

object VertexFormatUtil {
    fun fromJson(json: JsonElement): VertexFormat {
        if (json.isJsonArray) {
            val array = json.getAsJsonArray()
            val builder = VertexFormat.builder()

            for (element in array) {
                val s = element.asString

                builder.add(
                    s, when (s) {
                        "Position" -> VertexFormatElement.POSITION
                        "Color" -> VertexFormatElement.COLOR
                        "UV0", "UV_0" -> VertexFormatElement.UV0
                        "UV1", "UV_1" -> VertexFormatElement.UV1
                        "UV2", "UV_2" -> VertexFormatElement.UV2
                        "Normal" -> VertexFormatElement.NORMAL
                        else -> throw IllegalStateException("Invalid vertex getFormat element $s")
                    }
                )
            }

            return builder.build()
        } else {
            val name = json.asString

            return when (name.uppercase()) {
                "POSITION_COLOR_TEXTURE_LIGHT_NORMAL", "BLOCK" -> DefaultVertexFormat.BLOCK
                "POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL", "NEW_ENTITY" -> DefaultVertexFormat.NEW_ENTITY
                "POSITION_TEXTURE_COLOR_LIGHT", "PARTICLE" -> DefaultVertexFormat.PARTICLE
                "POSITION" -> DefaultVertexFormat.POSITION
                "POSITION_COLOR" -> DefaultVertexFormat.POSITION_COLOR
                "LINES", "POSITION_COLOR_NORMAL" -> DefaultVertexFormat.POSITION_COLOR_NORMAL
                "POSITION_COLOR_LIGHT", "POSITION_COLOR_LIGHTMAP" -> DefaultVertexFormat.POSITION_COLOR_LIGHTMAP
                "POSITION_TEXTURE", "POSITION_TEX" -> DefaultVertexFormat.POSITION_TEX
                "POSITION_TEXTURE_COLOR", "POSITION_TEX_COLOR" -> DefaultVertexFormat.POSITION_TEX_COLOR
                "POSITION_COLOR_TEXTURE_LIGHT", "POSITION_COLOR_TEX_LIGHTMAP" -> DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP
                "POSITION_TEXTURE_LIGHT_COLOR", "POSITION_TEX_LIGHTMAP_COLOR" -> DefaultVertexFormat.POSITION_TEX_LIGHTMAP_COLOR
                "POSITION_TEXTURE_COLOR_NORMAL", "POSITION_TEX_COLOR_NORMAL" -> DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL
                else -> throw IllegalStateException("Invalid vertex getFormat $name")
            }
        }
    }
}