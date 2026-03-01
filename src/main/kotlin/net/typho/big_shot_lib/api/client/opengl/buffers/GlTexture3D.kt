package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.util.InterpolationType
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import net.typho.big_shot_lib.api.client.opengl.util.WrappingType
import net.typho.big_shot_lib.api.util.buffers.BufferUploader

interface GlTexture3D : GlTexture {
    fun setInterpolation(min: InterpolationType, mag: InterpolationType = min) {
        OpenGL.INSTANCE.textureInterpolation(type, min, mag)
    }

    fun setWrapping(s: WrappingType, t: WrappingType = s, r: WrappingType = s) {
        OpenGL.INSTANCE.textureWrapping(type, s, t, r)
    }

    fun resize(width: Int, height: Int, depth: Int): BufferUploader
}