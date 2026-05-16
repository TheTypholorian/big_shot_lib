package net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundBuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlBuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager

open class NeoGlBuffer(glId: Int) : NeoGlResource(GlResourceType.BUFFER, glId), GlBuffer {
    constructor() : this(GlResourceType.BUFFER.create())

    override fun bind(target: GlBufferTarget): GlBoundBuffer {
        checkUsable()
        return GlBoundBuffer.Basic(this, target, NeoGlStateManager.MAIN.buffers[target].push(glId))
    }
}