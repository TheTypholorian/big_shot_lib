package net.typho.big_shot_lib.api.client.rendering.shaders.variables

import net.typho.big_shot_lib.api.client.rendering.shaders.mixins.ShaderBytecodeBuffer
import net.typho.big_shot_lib.api.client.rendering.shaders.mixins.ShaderOpcode
import net.typho.big_shot_lib.api.client.rendering.textures.TextureType
import net.typho.big_shot_lib.api.client.rendering.util.GlNamed
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL21.*
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL31.*
import org.lwjgl.opengl.GL32.*
import org.lwjgl.opengl.GL40.*
import org.lwjgl.opengl.GL42.*
import java.nio.IntBuffer

enum class ShaderVariableType(
    @JvmField
    val glId: Int,
    @JvmField
    val info: ShaderVariableTypeInfo
) : GlNamed {
    FLOAT(GL_FLOAT, ShaderVariableTypeInfo.Primitive(ShaderOpcode.OP_TYPE_FLOAT, 32)),
    FLOAT_VEC2(GL_FLOAT_VEC2, ShaderVariableTypeInfo.Vector(FLOAT.info, 2)),
    FLOAT_VEC3(GL_FLOAT_VEC3, ShaderVariableTypeInfo.Vector(FLOAT.info, 3)),
    FLOAT_VEC4(GL_FLOAT_VEC4, ShaderVariableTypeInfo.Vector(FLOAT.info, 4)),

    INT(GL_INT, ShaderVariableTypeInfo.Primitive(ShaderOpcode.OP_TYPE_INT, 32, true)),
    INT_VEC2(GL_INT_VEC2, ShaderVariableTypeInfo.Vector(INT.info, 2)),
    INT_VEC3(GL_INT_VEC3, ShaderVariableTypeInfo.Vector(INT.info, 3)),
    INT_VEC4(GL_INT_VEC4, ShaderVariableTypeInfo.Vector(INT.info, 4)),

    UINT(GL_UNSIGNED_INT, ShaderVariableTypeInfo.Primitive(ShaderOpcode.OP_TYPE_INT, 32, false)),
    UINT_VEC2(GL_UNSIGNED_INT_VEC2, ShaderVariableTypeInfo.Vector(UINT.info, 2)),
    UINT_VEC3(GL_UNSIGNED_INT_VEC3, ShaderVariableTypeInfo.Vector(UINT.info, 3)),
    UINT_VEC4(GL_UNSIGNED_INT_VEC4, ShaderVariableTypeInfo.Vector(UINT.info, 4)),

    BOOL(GL_BOOL, ShaderVariableTypeInfo.Primitive(ShaderOpcode.OP_TYPE_BOOL)),
    BOOL_VEC2(GL_BOOL_VEC2, ShaderVariableTypeInfo.Vector(BOOL.info, 2)),
    BOOL_VEC3(GL_BOOL_VEC3, ShaderVariableTypeInfo.Vector(BOOL.info, 3)),
    BOOL_VEC4(GL_BOOL_VEC4, ShaderVariableTypeInfo.Vector(BOOL.info, 4)),

    MAT2(GL_FLOAT_MAT2, ShaderVariableTypeInfo.Matrix(FLOAT.info, 2)),
    MAT2X2(GL_FLOAT_MAT2x3, ShaderVariableTypeInfo.Matrix(FLOAT.info, 2)),
    MAT2X4(GL_FLOAT_MAT2x4, ShaderVariableTypeInfo.Matrix(FLOAT.info, 2)),
    MAT3(GL_FLOAT_MAT3, ShaderVariableTypeInfo.Matrix(FLOAT.info, 3)),
    MAT3X2(GL_FLOAT_MAT3x2, ShaderVariableTypeInfo.Matrix(FLOAT.info, 3)),
    MAT3X4(GL_FLOAT_MAT3x4, ShaderVariableTypeInfo.Matrix(FLOAT.info, 3)),
    MAT4(GL_FLOAT_MAT4, ShaderVariableTypeInfo.Matrix(FLOAT.info, 4)),
    MAT4X2(GL_FLOAT_MAT4x2, ShaderVariableTypeInfo.Matrix(FLOAT.info, 4)),
    MAT4X3(GL_FLOAT_MAT4x3, ShaderVariableTypeInfo.Matrix(FLOAT.info, 4)),

    SAMPLER_1D(GL_SAMPLER_1D, ShaderVariableTypeInfo.Sampler),
    SAMPLER_2D(GL_SAMPLER_2D, ShaderVariableTypeInfo.Sampler),
    SAMPLER_3D(GL_SAMPLER_3D, ShaderVariableTypeInfo.Sampler),
    SAMPLER_2D_RECT(GL_SAMPLER_2D_RECT, ShaderVariableTypeInfo.Sampler),
    SAMPLER_1D_ARRAY(GL_SAMPLER_1D_ARRAY, ShaderVariableTypeInfo.Sampler),
    SAMPLER_2D_ARRAY(GL_SAMPLER_2D_ARRAY, ShaderVariableTypeInfo.Sampler),
    SAMPLER_CUBE(GL_SAMPLER_CUBE, ShaderVariableTypeInfo.Sampler),
    SAMPLER_BUFFER(GL_SAMPLER_BUFFER, ShaderVariableTypeInfo.Sampler),
    SAMPLER_CUBE_MAP_ARRAY(GL_SAMPLER_CUBE_MAP_ARRAY, ShaderVariableTypeInfo.Sampler),
    SAMPLER_2D_MULTISAMPLE(GL_SAMPLER_2D_MULTISAMPLE, ShaderVariableTypeInfo.Sampler),
    SAMPLER_2D_MULTISAMPLE_ARRAY(GL_SAMPLER_2D_MULTISAMPLE_ARRAY, ShaderVariableTypeInfo.Sampler),

    INT_SAMPLER_1D(GL_INT_SAMPLER_1D, ShaderVariableTypeInfo.Sampler),
    INT_SAMPLER_2D(GL_INT_SAMPLER_2D, ShaderVariableTypeInfo.Sampler),
    INT_SAMPLER_3D(GL_INT_SAMPLER_3D, ShaderVariableTypeInfo.Sampler),
    INT_SAMPLER_2D_RECT(GL_INT_SAMPLER_2D_RECT, ShaderVariableTypeInfo.Sampler),
    INT_SAMPLER_1D_ARRAY(GL_INT_SAMPLER_1D_ARRAY, ShaderVariableTypeInfo.Sampler),
    INT_SAMPLER_2D_ARRAY(GL_INT_SAMPLER_2D_ARRAY, ShaderVariableTypeInfo.Sampler),
    INT_SAMPLER_CUBE(GL_INT_SAMPLER_CUBE, ShaderVariableTypeInfo.Sampler),
    INT_SAMPLER_BUFFER(GL_INT_SAMPLER_BUFFER, ShaderVariableTypeInfo.Sampler),
    INT_SAMPLER_CUBE_MAP_ARRAY(GL_INT_SAMPLER_CUBE_MAP_ARRAY, ShaderVariableTypeInfo.Sampler),
    INT_SAMPLER_2D_MULTISAMPLE(GL_INT_SAMPLER_2D_MULTISAMPLE, ShaderVariableTypeInfo.Sampler),
    INT_SAMPLER_2D_MULTISAMPLE_ARRAY(GL_INT_SAMPLER_2D_MULTISAMPLE_ARRAY, ShaderVariableTypeInfo.Sampler),

    UINT_SAMPLER_1D(GL_UNSIGNED_INT_SAMPLER_1D, ShaderVariableTypeInfo.Sampler),
    UINT_SAMPLER_2D(GL_UNSIGNED_INT_SAMPLER_2D, ShaderVariableTypeInfo.Sampler),
    UINT_SAMPLER_3D(GL_UNSIGNED_INT_SAMPLER_3D, ShaderVariableTypeInfo.Sampler),
    UINT_SAMPLER_2D_RECT(GL_UNSIGNED_INT_SAMPLER_2D_RECT, ShaderVariableTypeInfo.Sampler),
    UINT_SAMPLER_1D_ARRAY(GL_UNSIGNED_INT_SAMPLER_1D_ARRAY, ShaderVariableTypeInfo.Sampler),
    UINT_SAMPLER_2D_ARRAY(GL_UNSIGNED_INT_SAMPLER_2D_ARRAY, ShaderVariableTypeInfo.Sampler),
    UINT_SAMPLER_CUBE(GL_UNSIGNED_INT_SAMPLER_CUBE, ShaderVariableTypeInfo.Sampler),
    UINT_SAMPLER_BUFFER(GL_UNSIGNED_INT_SAMPLER_BUFFER, ShaderVariableTypeInfo.Sampler),
    UINT_SAMPLER_CUBE_MAP_ARRAY(GL_UNSIGNED_INT_SAMPLER_CUBE_MAP_ARRAY, ShaderVariableTypeInfo.Sampler),
    UINT_SAMPLER_2D_MULTISAMPLE(GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE, ShaderVariableTypeInfo.Sampler),
    UINT_SAMPLER_2D_MULTISAMPLE_ARRAY(GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY, ShaderVariableTypeInfo.Sampler),

    SAMPLER_1D_SHADOW(GL_SAMPLER_1D_SHADOW, ShaderVariableTypeInfo.Sampler),
    SAMPLER_2D_SHADOW(GL_SAMPLER_2D_SHADOW, ShaderVariableTypeInfo.Sampler),
    SAMPLER_2D_RECT_SHADOW(GL_SAMPLER_2D_RECT_SHADOW, ShaderVariableTypeInfo.Sampler),
    SAMPLER_1D_ARRAY_SHADOW(GL_SAMPLER_1D_ARRAY_SHADOW, ShaderVariableTypeInfo.Sampler),
    SAMPLER_2D_ARRAY_SHADOW(GL_SAMPLER_2D_ARRAY_SHADOW, ShaderVariableTypeInfo.Sampler),
    SAMPLER_CUBE_SHADOW(GL_SAMPLER_CUBE_SHADOW, ShaderVariableTypeInfo.Sampler),
    SAMPLER_CUBE_MAP_ARRAY_SHADOW(GL_SAMPLER_CUBE_MAP_ARRAY_SHADOW, ShaderVariableTypeInfo.Sampler),

    IMAGE_1D(GL_IMAGE_1D, ShaderVariableTypeInfo.Image),
    IMAGE_2D(GL_IMAGE_2D, ShaderVariableTypeInfo.Image),
    IMAGE_3D(GL_IMAGE_3D, ShaderVariableTypeInfo.Image),
    IMAGE_2D_RECT(GL_IMAGE_2D_RECT, ShaderVariableTypeInfo.Image),
    IMAGE_CUBE(GL_IMAGE_CUBE, ShaderVariableTypeInfo.Image),
    IMAGE_BUFFER(GL_IMAGE_BUFFER, ShaderVariableTypeInfo.Image),
    IMAGE_1D_ARRAY(GL_IMAGE_1D_ARRAY, ShaderVariableTypeInfo.Image),
    IMAGE_2D_ARRAY(GL_IMAGE_2D_ARRAY, ShaderVariableTypeInfo.Image),
    IMAGE_CUBE_MAP_ARRAY(GL_IMAGE_CUBE_MAP_ARRAY, ShaderVariableTypeInfo.Image),
    IMAGE_2D_MULTISAMPLE(GL_IMAGE_2D_MULTISAMPLE, ShaderVariableTypeInfo.Image),
    IMAGE_2D_MULTISAMPLE_ARRAY(GL_IMAGE_2D_MULTISAMPLE_ARRAY, ShaderVariableTypeInfo.Image),

    INT_IMAGE_1D(GL_INT_IMAGE_1D, ShaderVariableTypeInfo.Image),
    INT_IMAGE_2D(GL_INT_IMAGE_2D, ShaderVariableTypeInfo.Image),
    INT_IMAGE_3D(GL_INT_IMAGE_3D, ShaderVariableTypeInfo.Image),
    INT_IMAGE_2D_RECT(GL_INT_IMAGE_2D_RECT, ShaderVariableTypeInfo.Image),
    INT_IMAGE_CUBE(GL_INT_IMAGE_CUBE, ShaderVariableTypeInfo.Image),
    INT_IMAGE_BUFFER(GL_INT_IMAGE_BUFFER, ShaderVariableTypeInfo.Image),
    INT_IMAGE_1D_ARRAY(GL_INT_IMAGE_1D_ARRAY, ShaderVariableTypeInfo.Image),
    INT_IMAGE_2D_ARRAY(GL_INT_IMAGE_2D_ARRAY, ShaderVariableTypeInfo.Image),
    INT_IMAGE_CUBE_MAP_ARRAY(GL_INT_IMAGE_CUBE_MAP_ARRAY, ShaderVariableTypeInfo.Image),
    INT_IMAGE_2D_MULTISAMPLE(GL_INT_IMAGE_2D_MULTISAMPLE, ShaderVariableTypeInfo.Image),
    INT_IMAGE_2D_MULTISAMPLE_ARRAY(GL_INT_IMAGE_2D_MULTISAMPLE_ARRAY, ShaderVariableTypeInfo.Image),

    UNSIGNED_INT_IMAGE_1D(GL_UNSIGNED_INT_IMAGE_1D, ShaderVariableTypeInfo.Image),
    UNSIGNED_INT_IMAGE_2D(GL_UNSIGNED_INT_IMAGE_2D, ShaderVariableTypeInfo.Image),
    UNSIGNED_INT_IMAGE_3D(GL_UNSIGNED_INT_IMAGE_3D, ShaderVariableTypeInfo.Image),
    UNSIGNED_INT_IMAGE_2D_RECT(GL_UNSIGNED_INT_IMAGE_2D_RECT, ShaderVariableTypeInfo.Image),
    UNSIGNED_INT_IMAGE_CUBE(GL_UNSIGNED_INT_IMAGE_CUBE, ShaderVariableTypeInfo.Image),
    UNSIGNED_INT_IMAGE_BUFFER(GL_UNSIGNED_INT_IMAGE_BUFFER, ShaderVariableTypeInfo.Image),
    UNSIGNED_INT_IMAGE_1D_ARRAY(GL_UNSIGNED_INT_IMAGE_1D_ARRAY, ShaderVariableTypeInfo.Image),
    UNSIGNED_INT_IMAGE_2D_ARRAY(GL_UNSIGNED_INT_IMAGE_2D_ARRAY, ShaderVariableTypeInfo.Image),
    UNSIGNED_INT_IMAGE_CUBE_MAP_ARRAY(GL_UNSIGNED_INT_IMAGE_CUBE_MAP_ARRAY, ShaderVariableTypeInfo.Image),
    UNSIGNED_INT_IMAGE_2D_MULTISAMPLE(GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE, ShaderVariableTypeInfo.Image),
    UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY(GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY, ShaderVariableTypeInfo.Image);

    override fun glId() = glId

    fun expectedTextureType(): TextureType {
        return when (this) {
            SAMPLER_1D, INT_SAMPLER_1D, UINT_SAMPLER_1D, SAMPLER_1D_SHADOW,
            IMAGE_1D, INT_IMAGE_1D, UNSIGNED_INT_IMAGE_1D -> TextureType.TEXTURE_1D

            SAMPLER_1D_ARRAY, INT_SAMPLER_1D_ARRAY, UINT_SAMPLER_1D_ARRAY, SAMPLER_1D_ARRAY_SHADOW,
            IMAGE_1D_ARRAY, INT_IMAGE_1D_ARRAY, UNSIGNED_INT_IMAGE_1D_ARRAY -> TextureType.TEXTURE_1D_ARRAY

            SAMPLER_2D, INT_SAMPLER_2D, UINT_SAMPLER_2D, SAMPLER_2D_SHADOW,
            IMAGE_2D, INT_IMAGE_2D, UNSIGNED_INT_IMAGE_2D -> TextureType.TEXTURE_2D

            SAMPLER_2D_ARRAY, INT_SAMPLER_2D_ARRAY, UINT_SAMPLER_2D_ARRAY, SAMPLER_2D_ARRAY_SHADOW,
            IMAGE_2D_ARRAY, INT_IMAGE_2D_ARRAY, UNSIGNED_INT_IMAGE_2D_ARRAY -> TextureType.TEXTURE_2D_ARRAY

            SAMPLER_2D_RECT, INT_SAMPLER_2D_RECT, UINT_SAMPLER_2D_RECT, SAMPLER_2D_RECT_SHADOW,
            IMAGE_2D_RECT, INT_IMAGE_2D_RECT, UNSIGNED_INT_IMAGE_2D_RECT -> TextureType.TEXTURE_RECTANGLE

            SAMPLER_3D, INT_SAMPLER_3D, UINT_SAMPLER_3D,
            IMAGE_3D, INT_IMAGE_3D, UNSIGNED_INT_IMAGE_3D -> TextureType.TEXTURE_3D

            SAMPLER_CUBE, SAMPLER_CUBE_SHADOW,
            IMAGE_CUBE, INT_IMAGE_CUBE, UNSIGNED_INT_IMAGE_CUBE,
            INT_SAMPLER_CUBE, UINT_SAMPLER_CUBE -> TextureType.TEXTURE_CUBE_MAP

            SAMPLER_CUBE_MAP_ARRAY, SAMPLER_CUBE_MAP_ARRAY_SHADOW,
            IMAGE_CUBE_MAP_ARRAY, INT_IMAGE_CUBE_MAP_ARRAY, UNSIGNED_INT_IMAGE_CUBE_MAP_ARRAY,
            INT_SAMPLER_CUBE_MAP_ARRAY, UINT_SAMPLER_CUBE_MAP_ARRAY -> TextureType.TEXTURE_CUBE_MAP_ARRAY

            SAMPLER_2D_MULTISAMPLE, IMAGE_2D_MULTISAMPLE,
            INT_IMAGE_2D_MULTISAMPLE, UNSIGNED_INT_IMAGE_2D_MULTISAMPLE,
            INT_SAMPLER_2D_MULTISAMPLE, UINT_SAMPLER_2D_MULTISAMPLE -> TextureType.TEXTURE_2D_MULTISAMPLE

            SAMPLER_2D_MULTISAMPLE_ARRAY, IMAGE_2D_MULTISAMPLE_ARRAY,
            INT_IMAGE_2D_MULTISAMPLE_ARRAY, UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY,
            INT_SAMPLER_2D_MULTISAMPLE_ARRAY, UINT_SAMPLER_2D_MULTISAMPLE_ARRAY -> TextureType.TEXTURE_2D_MULTISAMPLE_ARRAY

            SAMPLER_BUFFER, INT_SAMPLER_BUFFER, UINT_SAMPLER_BUFFER,
            IMAGE_BUFFER, INT_IMAGE_BUFFER, UNSIGNED_INT_IMAGE_BUFFER -> TextureType.TEXTURE_BUFFER

            else -> throw UnsupportedOperationException()
        }
    }

    fun matchesBytecode(opcode: ShaderOpcode, expectedId: Int? = null): Boolean {
        return info.matchesBytecode(opcode, expectedId)
    }

    fun findOrInjectBytecode(buffer: ShaderBytecodeBuffer, index: Int = buffer.findOpcode(ShaderOpcode.OP_FUNCTION)!!.index): Int {
        return info.findOrInjectBytecode(buffer, index)
    }

    fun injectBytecode(buffer: ShaderBytecodeBuffer, index: Int = buffer.findOpcode(ShaderOpcode.OP_FUNCTION)!!.index): Int {
        return info.injectBytecode(buffer, index)
    }

    fun compileBytecode(id: Int, buffer: ShaderBytecodeBuffer): IntBuffer {
        return info.compileBytecode(id, buffer)
    }
}