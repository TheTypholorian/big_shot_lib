package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.util.InterpolationType
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import net.typho.big_shot_lib.api.client.opengl.util.WrappingType
import net.typho.big_shot_lib.api.util.buffers.BufferUploader

interface GlTexture1D : GlTexture {
    fun setInterpolation(min: InterpolationType, mag: InterpolationType = min) {
        bind()
        OpenGL.INSTANCE.textureInterpolation(type(), min, mag)
        unbind()
    }

    fun setWrapping(s: WrappingType) {
        bind()
        OpenGL.INSTANCE.textureWrapping(type(), s)
        unbind()
    }

    fun resize(width: Int): BufferUploader
}