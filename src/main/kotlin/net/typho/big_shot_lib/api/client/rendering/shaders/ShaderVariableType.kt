package net.typho.big_shot_lib.api.client.rendering.shaders

import net.typho.big_shot_lib.api.client.rendering.shaders.mixins.ShaderBytecodeBuffer
import net.typho.big_shot_lib.api.client.rendering.shaders.mixins.ShaderOpcode
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
    val category: ShaderVariableCategory,
    @JvmField
    val opcodeId: Int,
    @JvmField
    val expectedIdIndex: Int?,
    @JvmField
    vararg val expectedValues: Pair<Int, Int>
) : GlNamed {
    FLOAT(GL_FLOAT, ShaderVariableCategory.PRIMITIVE, ShaderOpcode.OP_TYPE_FLOAT, 0),
    FLOAT_VEC2(GL_FLOAT_VEC2, ShaderVariableCategory.VECTOR, ShaderOpcode.OP_TYPE_VECTOR, 0, 2 to 2),
    FLOAT_VEC3(GL_FLOAT_VEC3, ShaderVariableCategory.VECTOR, ShaderOpcode.OP_TYPE_VECTOR),
    FLOAT_VEC4(GL_FLOAT_VEC4, ShaderVariableCategory.VECTOR, ShaderOpcode.OP_TYPE_VECTOR),

    INT(GL_INT, ShaderVariableCategory.PRIMITIVE),
    INT_VEC2(GL_INT_VEC2, ShaderVariableCategory.VECTOR),
    INT_VEC3(GL_INT_VEC3, ShaderVariableCategory.VECTOR),
    INT_VEC4(GL_INT_VEC4, ShaderVariableCategory.VECTOR),

    UINT(GL_UNSIGNED_INT, ShaderVariableCategory.PRIMITIVE),
    UINT_VEC2(GL_UNSIGNED_INT_VEC2, ShaderVariableCategory.VECTOR),
    UINT_VEC3(GL_UNSIGNED_INT_VEC3, ShaderVariableCategory.VECTOR),
    UINT_VEC4(GL_UNSIGNED_INT_VEC4, ShaderVariableCategory.VECTOR),

    BOOL(GL_BOOL, ShaderVariableCategory.PRIMITIVE),
    BOOL_VEC2(GL_BOOL_VEC2, ShaderVariableCategory.VECTOR),
    BOOL_VEC3(GL_BOOL_VEC3, ShaderVariableCategory.VECTOR),
    BOOL_VEC4(GL_BOOL_VEC4, ShaderVariableCategory.VECTOR),

    MAT2(GL_FLOAT_MAT2, ShaderVariableCategory.MATRIX),
    MAT2X2(GL_FLOAT_MAT2x3, ShaderVariableCategory.MATRIX),
    MAT2X4(GL_FLOAT_MAT2x4, ShaderVariableCategory.MATRIX),
    MAT3(GL_FLOAT_MAT3, ShaderVariableCategory.MATRIX),
    MAT3X2(GL_FLOAT_MAT3x2, ShaderVariableCategory.MATRIX),
    MAT3X4(GL_FLOAT_MAT3x4, ShaderVariableCategory.MATRIX),
    MAT4(GL_FLOAT_MAT4, ShaderVariableCategory.MATRIX),
    MAT4X2(GL_FLOAT_MAT4x2, ShaderVariableCategory.MATRIX),
    MAT4X3(GL_FLOAT_MAT4x3, ShaderVariableCategory.MATRIX),

    SAMPLER_1D(GL_SAMPLER_1D, ShaderVariableCategory.SAMPLER),
    SAMPLER_2D(GL_SAMPLER_2D, ShaderVariableCategory.SAMPLER),
    SAMPLER_3D(GL_SAMPLER_3D, ShaderVariableCategory.SAMPLER),
    SAMPLER_2D_RECT(GL_SAMPLER_2D_RECT, ShaderVariableCategory.SAMPLER),
    SAMPLER_1D_ARRAY(GL_SAMPLER_1D_ARRAY, ShaderVariableCategory.SAMPLER),
    SAMPLER_2D_ARRAY(GL_SAMPLER_2D_ARRAY, ShaderVariableCategory.SAMPLER),
    SAMPLER_CUBE(GL_SAMPLER_CUBE, ShaderVariableCategory.SAMPLER),
    SAMPLER_BUFFER(GL_SAMPLER_BUFFER, ShaderVariableCategory.SAMPLER),
    SAMPLER_CUBE_MAP_ARRAY(GL_SAMPLER_CUBE_MAP_ARRAY, ShaderVariableCategory.SAMPLER),
    SAMPLER_2D_MULTISAMPLE(GL_SAMPLER_2D_MULTISAMPLE, ShaderVariableCategory.SAMPLER),
    SAMPLER_2D_MULTISAMPLE_ARRAY(GL_SAMPLER_2D_MULTISAMPLE_ARRAY, ShaderVariableCategory.SAMPLER),

    INT_SAMPLER_1D(GL_INT_SAMPLER_1D, ShaderVariableCategory.SAMPLER),
    INT_SAMPLER_2D(GL_INT_SAMPLER_2D, ShaderVariableCategory.SAMPLER),
    INT_SAMPLER_3D(GL_INT_SAMPLER_3D, ShaderVariableCategory.SAMPLER),
    INT_SAMPLER_2D_RECT(GL_INT_SAMPLER_2D_RECT, ShaderVariableCategory.SAMPLER),
    INT_SAMPLER_1D_ARRAY(GL_INT_SAMPLER_1D_ARRAY, ShaderVariableCategory.SAMPLER),
    INT_SAMPLER_2D_ARRAY(GL_INT_SAMPLER_2D_ARRAY, ShaderVariableCategory.SAMPLER),
    INT_SAMPLER_CUBE(GL_INT_SAMPLER_CUBE, ShaderVariableCategory.SAMPLER),
    INT_SAMPLER_BUFFER(GL_INT_SAMPLER_BUFFER, ShaderVariableCategory.SAMPLER),
    INT_SAMPLER_CUBE_MAP_ARRAY(GL_INT_SAMPLER_CUBE_MAP_ARRAY, ShaderVariableCategory.SAMPLER),
    INT_SAMPLER_2D_MULTISAMPLE(GL_INT_SAMPLER_2D_MULTISAMPLE, ShaderVariableCategory.SAMPLER),
    INT_SAMPLER_2D_MULTISAMPLE_ARRAY(GL_INT_SAMPLER_2D_MULTISAMPLE_ARRAY, ShaderVariableCategory.SAMPLER),

    UINT_SAMPLER_1D(GL_UNSIGNED_INT_SAMPLER_1D, ShaderVariableCategory.SAMPLER),
    UINT_SAMPLER_2D(GL_UNSIGNED_INT_SAMPLER_2D, ShaderVariableCategory.SAMPLER),
    UINT_SAMPLER_3D(GL_UNSIGNED_INT_SAMPLER_3D, ShaderVariableCategory.SAMPLER),
    UINT_SAMPLER_2D_RECT(GL_UNSIGNED_INT_SAMPLER_2D_RECT, ShaderVariableCategory.SAMPLER),
    UINT_SAMPLER_1D_ARRAY(GL_UNSIGNED_INT_SAMPLER_1D_ARRAY, ShaderVariableCategory.SAMPLER),
    UINT_SAMPLER_2D_ARRAY(GL_UNSIGNED_INT_SAMPLER_2D_ARRAY, ShaderVariableCategory.SAMPLER),
    UINT_SAMPLER_CUBE(GL_UNSIGNED_INT_SAMPLER_CUBE, ShaderVariableCategory.SAMPLER),
    UINT_SAMPLER_BUFFER(GL_UNSIGNED_INT_SAMPLER_BUFFER, ShaderVariableCategory.SAMPLER),
    UINT_SAMPLER_CUBE_MAP_ARRAY(GL_UNSIGNED_INT_SAMPLER_CUBE_MAP_ARRAY, ShaderVariableCategory.SAMPLER),
    UINT_SAMPLER_2D_MULTISAMPLE(GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE, ShaderVariableCategory.SAMPLER),
    UINT_SAMPLER_2D_MULTISAMPLE_ARRAY(GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY, ShaderVariableCategory.SAMPLER),

    SAMPLER_1D_SHADOW(GL_SAMPLER_1D_SHADOW, ShaderVariableCategory.SAMPLER),
    SAMPLER_2D_SHADOW(GL_SAMPLER_2D_SHADOW, ShaderVariableCategory.SAMPLER),
    SAMPLER_2D_RECT_SHADOW(GL_SAMPLER_2D_RECT_SHADOW, ShaderVariableCategory.SAMPLER),
    SAMPLER_1D_ARRAY_SHADOW(GL_SAMPLER_1D_ARRAY_SHADOW, ShaderVariableCategory.SAMPLER),
    SAMPLER_2D_ARRAY_SHADOW(GL_SAMPLER_2D_ARRAY_SHADOW, ShaderVariableCategory.SAMPLER),
    SAMPLER_CUBE_SHADOW(GL_SAMPLER_CUBE_SHADOW, ShaderVariableCategory.SAMPLER),
    SAMPLER_CUBE_MAP_ARRAY_SHADOW(GL_SAMPLER_CUBE_MAP_ARRAY_SHADOW, ShaderVariableCategory.SAMPLER),

    IMAGE_1D(GL_IMAGE_1D, ShaderVariableCategory.IMAGE),
    IMAGE_2D(GL_IMAGE_2D, ShaderVariableCategory.IMAGE),
    IMAGE_3D(GL_IMAGE_3D, ShaderVariableCategory.IMAGE),
    IMAGE_2D_RECT(GL_IMAGE_2D_RECT, ShaderVariableCategory.IMAGE),
    IMAGE_CUBE(GL_IMAGE_CUBE, ShaderVariableCategory.IMAGE),
    IMAGE_BUFFER(GL_IMAGE_BUFFER, ShaderVariableCategory.IMAGE),
    IMAGE_1D_ARRAY(GL_IMAGE_1D_ARRAY, ShaderVariableCategory.IMAGE),
    IMAGE_2D_ARRAY(GL_IMAGE_2D_ARRAY, ShaderVariableCategory.IMAGE),
    IMAGE_CUBE_MAP_ARRAY(GL_IMAGE_CUBE_MAP_ARRAY, ShaderVariableCategory.IMAGE),
    IMAGE_2D_MULTISAMPLE(GL_IMAGE_2D_MULTISAMPLE, ShaderVariableCategory.IMAGE),
    IMAGE_2D_MULTISAMPLE_ARRAY(GL_IMAGE_2D_MULTISAMPLE_ARRAY, ShaderVariableCategory.IMAGE),

    INT_IMAGE_1D(GL_INT_IMAGE_1D, ShaderVariableCategory.IMAGE),
    INT_IMAGE_2D(GL_INT_IMAGE_2D, ShaderVariableCategory.IMAGE),
    INT_IMAGE_3D(GL_INT_IMAGE_3D, ShaderVariableCategory.IMAGE),
    INT_IMAGE_2D_RECT(GL_INT_IMAGE_2D_RECT, ShaderVariableCategory.IMAGE),
    INT_IMAGE_CUBE(GL_INT_IMAGE_CUBE, ShaderVariableCategory.IMAGE),
    INT_IMAGE_BUFFER(GL_INT_IMAGE_BUFFER, ShaderVariableCategory.IMAGE),
    INT_IMAGE_1D_ARRAY(GL_INT_IMAGE_1D_ARRAY, ShaderVariableCategory.IMAGE),
    INT_IMAGE_2D_ARRAY(GL_INT_IMAGE_2D_ARRAY, ShaderVariableCategory.IMAGE),
    INT_IMAGE_CUBE_MAP_ARRAY(GL_INT_IMAGE_CUBE_MAP_ARRAY, ShaderVariableCategory.IMAGE),
    INT_IMAGE_2D_MULTISAMPLE(GL_INT_IMAGE_2D_MULTISAMPLE, ShaderVariableCategory.IMAGE),
    INT_IMAGE_2D_MULTISAMPLE_ARRAY(GL_INT_IMAGE_2D_MULTISAMPLE_ARRAY, ShaderVariableCategory.IMAGE),

    UNSIGNED_INT_IMAGE_1D(GL_UNSIGNED_INT_IMAGE_1D, ShaderVariableCategory.IMAGE),
    UNSIGNED_INT_IMAGE_2D(GL_UNSIGNED_INT_IMAGE_2D, ShaderVariableCategory.IMAGE),
    UNSIGNED_INT_IMAGE_3D(GL_UNSIGNED_INT_IMAGE_3D, ShaderVariableCategory.IMAGE),
    UNSIGNED_INT_IMAGE_2D_RECT(GL_UNSIGNED_INT_IMAGE_2D_RECT, ShaderVariableCategory.IMAGE),
    UNSIGNED_INT_IMAGE_CUBE(GL_UNSIGNED_INT_IMAGE_CUBE, ShaderVariableCategory.IMAGE),
    UNSIGNED_INT_IMAGE_BUFFER(GL_UNSIGNED_INT_IMAGE_BUFFER, ShaderVariableCategory.IMAGE),
    UNSIGNED_INT_IMAGE_1D_ARRAY(GL_UNSIGNED_INT_IMAGE_1D_ARRAY, ShaderVariableCategory.IMAGE),
    UNSIGNED_INT_IMAGE_2D_ARRAY(GL_UNSIGNED_INT_IMAGE_2D_ARRAY, ShaderVariableCategory.IMAGE),
    UNSIGNED_INT_IMAGE_CUBE_MAP_ARRAY(GL_UNSIGNED_INT_IMAGE_CUBE_MAP_ARRAY, ShaderVariableCategory.IMAGE),
    UNSIGNED_INT_IMAGE_2D_MULTISAMPLE(GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE, ShaderVariableCategory.IMAGE),
    UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY(GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY, ShaderVariableCategory.IMAGE);

    override fun glId() = glId

    fun expectedSamplerType(): Int {
        return when (this) {
            SAMPLER_1D, SAMPLER_1D_SHADOW,
            IMAGE_1D, INT_IMAGE_1D, UNSIGNED_INT_IMAGE_1D -> GL_TEXTURE_1D

            SAMPLER_1D_ARRAY, SAMPLER_1D_ARRAY_SHADOW,
            IMAGE_1D_ARRAY, INT_IMAGE_1D_ARRAY, UNSIGNED_INT_IMAGE_1D_ARRAY -> GL_TEXTURE_1D_ARRAY

            SAMPLER_2D, SAMPLER_2D_SHADOW,
            IMAGE_2D, INT_IMAGE_2D, UNSIGNED_INT_IMAGE_2D -> GL_TEXTURE_2D

            SAMPLER_2D_ARRAY, SAMPLER_2D_ARRAY_SHADOW,
            IMAGE_2D_ARRAY, INT_IMAGE_2D_ARRAY, UNSIGNED_INT_IMAGE_2D_ARRAY -> GL_TEXTURE_2D_ARRAY

            SAMPLER_2D_RECT, SAMPLER_2D_RECT_SHADOW,
            IMAGE_2D_RECT, INT_IMAGE_2D_RECT, UNSIGNED_INT_IMAGE_2D_RECT -> GL_TEXTURE_RECTANGLE

            SAMPLER_3D, INT_SAMPLER_3D, UINT_SAMPLER_3D,
            IMAGE_3D, INT_IMAGE_3D, UNSIGNED_INT_IMAGE_3D -> GL_TEXTURE_3D

            SAMPLER_CUBE, SAMPLER_CUBE_SHADOW,
            IMAGE_CUBE, INT_IMAGE_CUBE, UNSIGNED_INT_IMAGE_CUBE,
            INT_SAMPLER_CUBE, UINT_SAMPLER_CUBE -> GL_TEXTURE_CUBE_MAP

            SAMPLER_CUBE_MAP_ARRAY, SAMPLER_CUBE_MAP_ARRAY_SHADOW,
            IMAGE_CUBE_MAP_ARRAY, INT_IMAGE_CUBE_MAP_ARRAY, UNSIGNED_INT_IMAGE_CUBE_MAP_ARRAY,
            INT_SAMPLER_CUBE_MAP_ARRAY, UINT_SAMPLER_CUBE_MAP_ARRAY -> GL_TEXTURE_CUBE_MAP_ARRAY

            SAMPLER_2D_MULTISAMPLE, IMAGE_2D_MULTISAMPLE,
            INT_IMAGE_2D_MULTISAMPLE, UNSIGNED_INT_IMAGE_2D_MULTISAMPLE,
            INT_SAMPLER_2D_MULTISAMPLE, UINT_SAMPLER_2D_MULTISAMPLE -> GL_TEXTURE_2D_MULTISAMPLE

            SAMPLER_2D_MULTISAMPLE_ARRAY, IMAGE_2D_MULTISAMPLE_ARRAY,
            INT_IMAGE_2D_MULTISAMPLE_ARRAY, UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY,
            INT_SAMPLER_2D_MULTISAMPLE_ARRAY, UINT_SAMPLER_2D_MULTISAMPLE_ARRAY -> GL_TEXTURE_2D_MULTISAMPLE_ARRAY

            SAMPLER_BUFFER, INT_SAMPLER_BUFFER, UINT_SAMPLER_BUFFER,
            IMAGE_BUFFER, INT_IMAGE_BUFFER, UNSIGNED_INT_IMAGE_BUFFER -> GL_TEXTURE_BUFFER

            else -> throw UnsupportedOperationException()
        }
    }

    fun matchesBytecode(opcode: ShaderOpcode, expectedId: Int? = null): Boolean {
    }

    fun findOrInjectBytecode(buffer: ShaderBytecodeBuffer, index: Int = buffer.findOpcode(ShaderOpcode.OP_FUNCTION)!!.index): Int {
        buffer.findOpcode { matchesBytecode(it) }?.let {
            return it.getWord(0)
        }

        return injectBytecode(buffer, index)
    }

    fun injectBytecode(buffer: ShaderBytecodeBuffer, words: Int = buffer.findOpcode(ShaderOpcode.OP_FUNCTION)!!.index): Int {
        val id = buffer.bound++
        buffer.insert(words, compile(id, buffer))
        return id
    }

    fun compile(id: Int, buffer: ShaderBytecodeBuffer): IntBuffer {
    }
}