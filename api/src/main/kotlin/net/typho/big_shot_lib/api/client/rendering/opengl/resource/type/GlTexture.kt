package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureFormat
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundTexture
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import org.lwjgl.opengl.GL30.glFramebufferTexture2D

interface GlTexture : GlResource, GlFramebufferAttachment {
    val format: GlTextureFormat?
    val width: Int
    val height: Int?
    val depth: Int?

    fun bind(target: GlTextureTarget): GlBoundTexture

    override fun attach(target: Int, attachment: Int, width: Int, height: Int) {
        glFramebufferTexture2D(target, attachment, GL_TEXTURE_2D, glId, 0)
        bind(GlTextureTarget.TEXTURE_2D).use { it.textureDataMutable2D(width, height, format!!) }
    }
}