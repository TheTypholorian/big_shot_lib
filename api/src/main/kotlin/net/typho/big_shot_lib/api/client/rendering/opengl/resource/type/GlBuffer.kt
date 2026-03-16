package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundBuffer

interface GlBuffer : GlResource {
    fun bind(target: GlBufferTarget): GlBoundBuffer
}