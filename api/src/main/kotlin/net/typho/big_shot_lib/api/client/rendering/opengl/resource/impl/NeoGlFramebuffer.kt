package net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundFramebuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager

class NeoGlFramebuffer(glId: Int) : NeoGlResource(GlResourceType.FRAMEBUFFER, glId), GlFramebuffer {
    constructor() : this(GlResourceType.FRAMEBUFFER.create())

    override var width: Int = 1
        private set(value) {
            field = value
        }
    override var height: Int = 1
        private set(value) {
            field = value
        }

    override fun bind(viewport: Boolean): GlBoundFramebuffer {
        return object : GlBoundFramebuffer.Basic(this, viewport, NeoGlStateManager.INSTANCE.framebuffer.push(glId)) {
            override var width by this@NeoGlFramebuffer::width
            override var height by this@NeoGlFramebuffer::height
        }
    }
}