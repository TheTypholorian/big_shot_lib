package net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundBuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.state.NeoGlStateManager
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlBuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType

class NeoGlBuffer(glId: Int) : NeoGlResource(GlResourceType.BUFFER, glId), GlBuffer {
    constructor() : this(GlResourceType.BUFFER.create())

    override fun bind(target: GlBufferTarget): GlBoundBuffer {
        return GlBoundBuffer.Basic(this, target, NeoGlStateManager.INSTANCE.buffers[target].push(glId))
    }
}