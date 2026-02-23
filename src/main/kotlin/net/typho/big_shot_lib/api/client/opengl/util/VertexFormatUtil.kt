package net.typho.big_shot_lib.api.client.opengl.util

import com.google.gson.JsonElement
import net.typho.big_shot_lib.api.client.opengl.buffers.NeoVertexFormat

object VertexFormatUtil {
    fun fromJson(json: JsonElement): NeoVertexFormat {
        if (json.isJsonArray) {
            val array = json.getAsJsonArray()
            val builder = NeoVertexFormat.builder()

            for (element in array) {
                val s = element.asString

                builder.add(
                    s, when (s) {
                        "Position" -> NeoVertexFormat.Element.POSITION
                        "Color" -> NeoVertexFormat.Element.COLOR
                        "UV0", "UV_0", "TEXTURE_UV" -> NeoVertexFormat.Element.TEXTURE_UV
                        "UV1", "UV_1", "OVERLAY_UV" -> NeoVertexFormat.Element.OVERLAY_UV
                        "UV2", "UV_2", "LIGHT_UV" -> NeoVertexFormat.Element.LIGHT_UV
                        "Normal" -> NeoVertexFormat.Element.NORMAL
                        else -> throw IllegalStateException("Invalid vertex getFormat element $s")
                    }
                )
            }

            return builder.build()
        } else {
            val name = json.asString

            return when (name.uppercase()) {
                "POSITION_COLOR_TEXTURE_LIGHT_NORMAL", "BLOCK" -> NeoVertexFormat.BLOCK
                "POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL", "NEW_ENTITY" -> NeoVertexFormat.NEW_ENTITY
                "POSITION_TEXTURE_COLOR_LIGHT", "PARTICLE" -> NeoVertexFormat.PARTICLE
                "POSITION" -> NeoVertexFormat.POSITION
                "POSITION_COLOR" -> NeoVertexFormat.POSITION_COLOR
                "LINES", "POSITION_COLOR_NORMAL" -> NeoVertexFormat.POSITION_COLOR_NORMAL
                "POSITION_COLOR_LIGHT", "POSITION_COLOR_LIGHTMAP" -> NeoVertexFormat.POSITION_COLOR_LIGHTMAP
                "POSITION_TEXTURE", "POSITION_TEX" -> NeoVertexFormat.POSITION_TEX
                "POSITION_TEXTURE_COLOR", "POSITION_TEX_COLOR" -> NeoVertexFormat.POSITION_TEX_COLOR
                "POSITION_COLOR_TEXTURE_LIGHT", "POSITION_COLOR_TEX_LIGHTMAP" -> NeoVertexFormat.POSITION_COLOR_TEX_LIGHTMAP
                "POSITION_TEXTURE_LIGHT_COLOR", "POSITION_TEX_LIGHTMAP_COLOR" -> NeoVertexFormat.POSITION_TEX_LIGHTMAP_COLOR
                "POSITION_TEXTURE_COLOR_NORMAL", "POSITION_TEX_COLOR_NORMAL" -> NeoVertexFormat.POSITION_TEX_COLOR_NORMAL
                else -> throw IllegalStateException("Invalid vertex getFormat $name")
            }
        }
    }
}