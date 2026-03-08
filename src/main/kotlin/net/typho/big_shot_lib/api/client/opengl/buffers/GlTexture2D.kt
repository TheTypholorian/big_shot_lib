package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.util.InterpolationType
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import net.typho.big_shot_lib.api.client.opengl.util.WrappingType
import net.typho.big_shot_lib.api.util.buffers.BufferUploader

interface GlTexture2D : GlTexture, GlFramebufferAttachment {
    fun setInterpolation(min: InterpolationType, mag: InterpolationType = min) {
        bind()
        OpenGL.INSTANCE.textureInterpolation(type, min, mag)
        unbind()
    }

    fun setWrapping(s: WrappingType, t: WrappingType = s) {
        bind()
        OpenGL.INSTANCE.textureWrapping(type, s, t)
        unbind()
    }

    override fun resize(width: Int, height: Int, upload: (uploader: BufferUploader) -> Unit)
}