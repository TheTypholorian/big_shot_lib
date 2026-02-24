package net.typho.big_shot_lib.api.client.opengl.shaders.uniforms

import net.typho.big_shot_lib.api.client.opengl.buffers.GlTexture
import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderProgramKey
import net.typho.big_shot_lib.api.client.opengl.shaders.variables.ShaderVariableType
import net.typho.big_shot_lib.api.client.opengl.shaders.variables.ShaderVariableTypeInfo
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import net.typho.big_shot_lib.api.client.opengl.util.TextureType
import net.typho.big_shot_lib.api.util.IColor
import org.joml.*

abstract class GlUniform(
    @JvmField
    val name: String,
    @JvmField
    val location: Int,
    @JvmField
    val type: ShaderVariableType
) {
    fun assertType(vararg allowed: ShaderVariableType) {
        if (type !in allowed) {
            throw IllegalArgumentException("Uniform $name in program ${programKey()} is $type but expected ${allowed.joinToString(" or ")}")
        }
    }

    protected abstract fun pickSamplerUnit(): Int

    abstract fun programKey(): ShaderProgramKey

    fun setSampler(type: TextureType, textureId: Int, samplerId: Int? = null) {
        if (this.type.info !is ShaderVariableTypeInfo.Sampler) {
            throw UnsupportedOperationException("Uniform $name in program ${programKey()} is of type ${this.type} which isn't a sampler")
        }

        val expectedType = this.type.expectedTextureType()

        if (expectedType != type) {
            throw IllegalStateException("Expected a texture of type $expectedType, got type $type")
        }

        val unit = pickSamplerUnit()
        OpenGL.INSTANCE.activeTexture(unit)
        OpenGL.INSTANCE.bindTexture(type, textureId)
        OpenGL.INSTANCE.bindSampler(unit, samplerId)
        setValue(unit)
    }

    fun setSampler(texture: GlTexture) {
        setSampler(texture.type(), texture.glId())
    }

    fun setValue(f1: Float) {
        assertType(ShaderVariableType.FLOAT)
        OpenGL.INSTANCE.setUniformValue(location, f1)
    }

    fun setValue(f1: Float, f2: Float) {
        assertType(ShaderVariableType.FLOAT_VEC2)
        OpenGL.INSTANCE.setUniformValue(location, f1, f2)
    }

    fun setValue(f: Vector2f) {
        setValue(f.x, f.y)
    }

    fun setValue(f1: Float, f2: Float, f3: Float) {
        assertType(ShaderVariableType.FLOAT_VEC3)
        OpenGL.INSTANCE.setUniformValue(location, f1, f2, f3)
    }

    fun setValue(f: Vector3f) {
        setValue(f.x, f.y, f.z)
    }

    fun setValue(f1: Float, f2: Float, f3: Float, f4: Float) {
        assertType(ShaderVariableType.FLOAT_VEC4)
        OpenGL.INSTANCE.setUniformValue(location, f1, f2, f3, f4)
    }

    fun setValue(f: Vector4f) {
        setValue(f.x, f.y, f.z, f.w)
    }

    fun setValue(i1: Int) {
        if (type.info !is ShaderVariableTypeInfo.Sampler) {
            assertType(ShaderVariableType.INT, ShaderVariableType.UINT)
        }

        OpenGL.INSTANCE.setUniformValue(location, i1)
    }

    fun setValue(i1: Int, i2: Int) {
        assertType(ShaderVariableType.INT_VEC2, ShaderVariableType.UINT_VEC2)
        OpenGL.INSTANCE.setUniformValue(location, i1, i2)
    }

    fun setValue(i: Vector2i) {
        setValue(i.x, i.y)
    }

    fun setValue(i1: Int, i2: Int, i3: Int) {
        assertType(ShaderVariableType.INT_VEC3, ShaderVariableType.UINT_VEC3)
        OpenGL.INSTANCE.setUniformValue(location, i1, i2, i3)
    }

    fun setValue(i: Vector3i) {
        setValue(i.x, i.y, i.z)
    }

    fun setValue(i1: Int, i2: Int, i3: Int, i4: Int) {
        assertType(ShaderVariableType.INT_VEC4, ShaderVariableType.UINT_VEC4)
        OpenGL.INSTANCE.setUniformValue(location, i1, i2, i3, i4)
    }

    fun setValue(i: Vector4i) {
        setValue(i.x, i.y, i.z, i.w)
    }

    fun setValue(b1: Boolean) {
        assertType(ShaderVariableType.BOOL)
        OpenGL.INSTANCE.setUniformValue(location, b1)
    }

    fun setValue(b1: Boolean, b2: Boolean) {
        assertType(ShaderVariableType.BOOL_VEC2)
        OpenGL.INSTANCE.setUniformValue(location, b1, b2)
    }

    fun setValue(b1: Boolean, b2: Boolean, b3: Boolean) {
        assertType(ShaderVariableType.BOOL_VEC3)
        OpenGL.INSTANCE.setUniformValue(location, b1, b2, b3)
    }

    fun setValue(b1: Boolean, b2: Boolean, b3: Boolean, b4: Boolean) {
        assertType(ShaderVariableType.BOOL_VEC4)
        OpenGL.INSTANCE.setUniformValue(location, b1, b2, b3, b4)
    }

    fun setValue(d1: Double) {
        assertType(ShaderVariableType.FLOAT)
        OpenGL.INSTANCE.setUniformValue(location, d1)
    }

    fun setValue(d1: Double, d2: Double) {
        assertType(ShaderVariableType.FLOAT_VEC2)
        OpenGL.INSTANCE.setUniformValue(location, d1, d2)
    }

    fun setValue(d: Vector2d) {
        setValue(d.x, d.y)
    }

    fun setValue(d1: Double, d2: Double, d3: Double) {
        assertType(ShaderVariableType.FLOAT_VEC3)
        OpenGL.INSTANCE.setUniformValue(location, d1, d2, d3)
    }

    fun setValue(d: Vector3d) {
        setValue(d.x, d.y, d.z)
    }

    fun setValue(d1: Double, d2: Double, d3: Double, d4: Double) {
        assertType(ShaderVariableType.FLOAT_VEC4)
        OpenGL.INSTANCE.setUniformValue(location, d1, d2, d3, d4)
    }

    fun setValue(d: Vector4d) {
        setValue(d.x, d.y, d.z, d.w)
    }

    fun setValue(mat: Matrix2f, transpose: Boolean = false) {
        assertType(ShaderVariableType.MAT2)
        OpenGL.INSTANCE.setUniformValue(location, mat, transpose)
    }

    fun setValue(mat: Matrix3f, transpose: Boolean = false) {
        assertType(ShaderVariableType.MAT3)
        OpenGL.INSTANCE.setUniformValue(location, mat, transpose)
    }

    fun setValue(mat: Matrix4f, transpose: Boolean = false) {
        assertType(ShaderVariableType.MAT4)
        OpenGL.INSTANCE.setUniformValue(location, mat, transpose)
    }

    fun setValue(mat: Matrix3x2f, transpose: Boolean = false) {
        assertType(ShaderVariableType.MAT3X2)
        OpenGL.INSTANCE.setUniformValue(location, mat, transpose)
    }

    fun setValue(mat: Matrix4x3f, transpose: Boolean = false) {
        assertType(ShaderVariableType.MAT4X3)
        OpenGL.INSTANCE.setUniformValue(location, mat, transpose)
    }

    fun setValue(color: IColor) {
        when (type) {
            ShaderVariableType.UINT -> setValue(color.toRGBA())
            ShaderVariableType.FLOAT_VEC3 -> setValue(color.redF(), color.greenF(), color.blueF())
            ShaderVariableType.FLOAT_VEC4 -> setValue(color.redF(), color.greenF(), color.blueF(), color.alphaF() ?: 1f)
            ShaderVariableType.INT_VEC3, ShaderVariableType.UINT_VEC3 -> setValue(color.red(), color.green(), color.blue())
            ShaderVariableType.INT_VEC4, ShaderVariableType.UINT_VEC4 -> setValue(color.red(), color.green(), color.blue(), color.alpha() ?: 255)
            else -> assertType(
                ShaderVariableType.UINT,
                ShaderVariableType.FLOAT_VEC3,
                ShaderVariableType.FLOAT_VEC4,
                ShaderVariableType.INT_VEC3,
                ShaderVariableType.INT_VEC4,
                ShaderVariableType.UINT_VEC4,
                ShaderVariableType.UINT_VEC4
            )
        }
    }
}
