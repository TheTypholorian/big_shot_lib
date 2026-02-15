package net.typho.big_shot_lib.api.client.rendering.textures

import net.typho.big_shot_lib.api.client.rendering.state.OpenGL
import net.typho.big_shot_lib.api.util.buffers.BufferUploader

interface GlTexture3D : GlTexture {
    fun setInterpolation(min: InterpolationType, mag: InterpolationType = min) {
        OpenGL.INSTANCE.textureInterpolation(type(), min, mag)
    }

    fun setWrapping(s: WrappingType, t: WrappingType = s, r: WrappingType = s) {
        OpenGL.INSTANCE.textureWrapping(type(), s, t, r)
    }

    fun resize(width: Int, height: Int, depth: Int): BufferUploader
}