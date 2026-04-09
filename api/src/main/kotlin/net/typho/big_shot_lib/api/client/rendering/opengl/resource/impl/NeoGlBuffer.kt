package net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundBuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlBuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager
import net.typho.big_shot_lib.api.client.rendering.util.RenderingContext

open class NeoGlBuffer(glId: Int, autoFree: Boolean, context: RenderingContext = RenderingContext.get()) : NeoGlResource(GlResourceType.BUFFER, glId, autoFree), GlBuffer {
    constructor() : this(GlResourceType.BUFFER.create(), true)

    override fun bind(target: GlBufferTarget): GlBoundBuffer {
        checkUsable()
        return GlBoundBuffer.Basic(this, target, NeoGlStateManager.CURRENT.buffers[target].push(glId))
    }
}