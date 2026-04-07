package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundVertexArray

interface GlVertexArray : GlResource.Container {
    fun bind(): GlBoundVertexArray
}