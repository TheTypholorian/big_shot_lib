package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundRenderbuffer

interface GlRenderbuffer : GlResource {
    fun bind(): GlBoundRenderbuffer
}