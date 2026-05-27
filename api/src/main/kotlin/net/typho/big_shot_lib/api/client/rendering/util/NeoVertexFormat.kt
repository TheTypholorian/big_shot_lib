package net.typho.big_shot_lib.api.client.rendering.util

import com.mojang.blaze3d.vertex.VertexFormatElement

interface NeoVertexFormat : Iterable<VertexFormatElement> {
    fun setupBufferState()

    fun clearBufferState()
}