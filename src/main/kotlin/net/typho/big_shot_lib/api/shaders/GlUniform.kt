package net.typho.big_shot_lib.api.shaders

import net.typho.big_shot_lib.api.state.OpenGL
import net.typho.big_shot_lib.api.textures.GlTexture
import net.typho.big_shot_lib.api.util.GlNamed
import org.joml.*
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D

class GlUniform(
    @JvmField
    val location: Int,
    @JvmField
    val parent: GlShader
) : GlNamed {
    override fun glId() = location

    fun setSampler(type: Int, textureId: Int) {
        val unit = parent.pickSamplerUnit(location)
        OpenGL.INSTANCE.activeTexture(unit)
        OpenGL.INSTANCE.bindTexture(type, textureId)
        setValue(unit)
    }

    fun setSampler(texture: GlTexture) {
        setSampler( GL_TEXTURE_2D, texture.glId)
    }

    fun setValue(f1: Float) {
        OpenGL.INSTANCE.setUniformValue(location, f1)
    }

    fun setValue(f1: Float, f2: Float) {
        OpenGL.INSTANCE.setUniformValue(location, f1, f2)
    }

    fun setValue(f1: Float, f2: Float, f3: Float) {
        OpenGL.INSTANCE.setUniformValue(location, f1, f2, f3)
    }

    fun setValue(f1: Float, f2: Float, f3: Float, f4: Float) {
        OpenGL.INSTANCE.setUniformValue(location, f1, f2, f3, f4)
    }

    fun setValue(i1: Int) {
        OpenGL.INSTANCE.setUniformValue(location, i1)
    }

    fun setValue(i1: Int, i2: Int) {
        OpenGL.INSTANCE.setUniformValue(location, i1, i2)
    }

    fun setValue(i1: Int, i2: Int, i3: Int) {
        OpenGL.INSTANCE.setUniformValue(location, i1, i2, i3)
    }

    fun setValue(i1: Int, i2: Int, i3: Int, i4: Int) {
        OpenGL.INSTANCE.setUniformValue(location, i1, i2, i3, i4)
    }

    fun setValue(d1: Double) {
        OpenGL.INSTANCE.setUniformValue(location, d1)
    }

    fun setValue(d1: Double, d2: Double) {
        OpenGL.INSTANCE.setUniformValue(location, d1, d2)
    }

    fun setValue(d1: Double, d2: Double, d3: Double) {
        OpenGL.INSTANCE.setUniformValue(location, d1, d2, d3)
    }

    fun setValue(d1: Double, d2: Double, d3: Double, d4: Double) {
        OpenGL.INSTANCE.setUniformValue(location, d1, d2, d3, d4)
    }

    fun setValue(mat: Matrix2f, transpose: Boolean = false) {
        OpenGL.INSTANCE.setUniformValue(location, mat, transpose)
    }

    fun setValue(mat: Matrix3f, transpose: Boolean = false) {
        OpenGL.INSTANCE.setUniformValue(location, mat, transpose)
    }

    fun setValue(mat: Matrix4f, transpose: Boolean = false) {
        OpenGL.INSTANCE.setUniformValue(location, mat, transpose)
    }

    fun setValue(mat: Matrix3x2f, transpose: Boolean = false) {
        OpenGL.INSTANCE.setUniformValue(location, mat, transpose)
    }

    fun setValue(mat: Matrix4x3f, transpose: Boolean = false) {
        OpenGL.INSTANCE.setUniformValue(location, mat, transpose)
    }

    fun setValue(mat: Matrix2d, transpose: Boolean = false) {
        OpenGL.INSTANCE.setUniformValue(location, mat, transpose)
    }

    fun setValue(mat: Matrix3d, transpose: Boolean = false) {
        OpenGL.INSTANCE.setUniformValue(location, mat, transpose)
    }

    fun setValue(mat: Matrix4d, transpose: Boolean = false) {
        OpenGL.INSTANCE.setUniformValue(location, mat, transpose)
    }

    fun setValue(mat: Matrix3x2d, transpose: Boolean = false) {
        OpenGL.INSTANCE.setUniformValue(location, mat, transpose)
    }

    fun setValue(mat: Matrix4x3d, transpose: Boolean = false) {
        OpenGL.INSTANCE.setUniformValue(location, mat, transpose)
    }
}
