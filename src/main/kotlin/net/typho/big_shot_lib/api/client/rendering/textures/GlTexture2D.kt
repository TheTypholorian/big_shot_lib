package net.typho.big_shot_lib.api.client.rendering.textures

import net.typho.big_shot_lib.api.client.rendering.state.OpenGL
import net.typho.big_shot_lib.api.util.buffers.BufferUploader

interface GlTexture2D : GlTexture, GlFramebufferAttachment {
    fun setInterpolation(min: InterpolationType, mag: InterpolationType = min) {
        OpenGL.INSTANCE.textureInterpolation(type(), min, mag)
    }

    fun setWrapping(s: WrappingType, t: WrappingType = s) {
        OpenGL.INSTANCE.textureWrapping(type(), s, t)
    }

    override fun resize(width: Int, height: Int): BufferUploader
}