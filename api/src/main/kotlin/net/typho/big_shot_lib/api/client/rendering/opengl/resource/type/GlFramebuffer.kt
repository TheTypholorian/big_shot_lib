package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundFramebuffer

interface GlFramebuffer : GlResource {
    val width: Int
    val height: Int

    var colorAttachments:

    fun bind(viewport: Boolean): GlBoundFramebuffer
}