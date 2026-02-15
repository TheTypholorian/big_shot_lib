package net.typho.big_shot_lib.api.client.rendering.textures

import net.typho.big_shot_lib.api.client.rendering.state.OpenGL
import net.typho.big_shot_lib.api.util.buffers.BufferUploader

interface GlTexture1D : GlTexture {
    fun setInterpolation(min: InterpolationType, mag: InterpolationType = min) {
        OpenGL.INSTANCE.textureInterpolation(type(), min, mag)
    }

    fun setWrapping(s: WrappingType) {
        OpenGL.INSTANCE.textureWrapping(type(), s)
    }

    fun resize(width: Int): BufferUploader
}