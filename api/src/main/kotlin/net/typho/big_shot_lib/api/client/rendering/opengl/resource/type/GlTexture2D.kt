package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundTexture2D
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import org.lwjgl.opengl.GL30.glFramebufferTexture2D

interface GlTexture2D : GlResource, GlFramebufferAttachment {
    val width: Int
    val height: Int

    fun bind(target: GlTextureTarget): GlBoundTexture2D

    override fun attach(target: Int, attachment: Int) {
        glFramebufferTexture2D(target, attachment, GL_TEXTURE_2D, glId, 0)
    }
}