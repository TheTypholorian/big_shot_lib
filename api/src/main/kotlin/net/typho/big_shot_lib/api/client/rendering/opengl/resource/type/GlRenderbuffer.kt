package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundRenderbuffer
import org.lwjgl.opengl.GL30.GL_RENDERBUFFER
import org.lwjgl.opengl.GL30.glFramebufferRenderbuffer

interface GlRenderbuffer : GlResource, GlFramebufferAttachment {
    fun bind(): GlBoundRenderbuffer

    override fun attach(target: Int, attachment: Int) {
        glFramebufferRenderbuffer(target, attachment, GL_RENDERBUFFER, glId)
    }
}