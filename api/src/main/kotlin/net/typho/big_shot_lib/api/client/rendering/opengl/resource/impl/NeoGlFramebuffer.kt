package net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundFramebuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager

class NeoGlFramebuffer(glId: Int) : NeoGlResource(GlResourceType.FRAMEBUFFER, glId), GlFramebuffer {
    constructor() : this(GlResourceType.FRAMEBUFFER.create())

    override fun bind(viewport: Boolean): GlBoundFramebuffer {
        return GlBoundFramebuffer.Basic(this, viewport, NeoGlStateManager.INSTANCE.framebuffer.push(glId))
    }
}