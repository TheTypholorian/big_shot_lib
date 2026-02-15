package net.typho.big_shot_lib.api.client.rendering.textures

import net.typho.big_shot_lib.api.client.rendering.state.OpenGL
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