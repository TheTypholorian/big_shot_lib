package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureFormat
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import org.lwjgl.opengl.GL30.glFramebufferTexture2D

interface TextureMipmapLevel : GlFramebufferAttachment {
    val mipmapLevel: Int
    val texture: GlTexture2D
    override val width: Int?
        get() = texture.widths[mipmapLevel]
    override val height: Int?
        get() = texture.heights[mipmapLevel]
    override val format: GlTextureFormat?
        get() = texture.formats[mipmapLevel]

    override fun attach(target: Int, attachment: Int) {
        glFramebufferTexture2D(target, attachment, GL_TEXTURE_2D, texture.glId, mipmapLevel)
    }
}