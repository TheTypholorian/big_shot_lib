package net.typho.big_shot_lib.api.client.ext

import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormats

interface VertexFormatBuilderExtension {
    @Suppress("DEPRECATION")
    fun build(location: Identifier): VertexFormat {
        val format = (this as VertexFormat.Builder).build()
        NeoVertexFormats.register(location, format)
        return format
    }
}