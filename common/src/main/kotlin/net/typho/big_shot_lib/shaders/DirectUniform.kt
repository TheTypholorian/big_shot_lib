package net.typho.big_shot_lib.shaders

import com.mojang.blaze3d.shaders.AbstractUniform
import org.joml.Matrix3f
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector4f
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL21

open class DirectUniform(
    val location: Int
) : AbstractUniform() {
    override fun set(x: Float) {
        GL20.glUniform1f(location, x)
    }

    override fun set(x: Float, y: Float) {
        GL20.glUniform2f(location, x, y)
    }

    override fun set(x: Float, y: Float, z: Float) {
        GL20.glUniform3f(location, x, y, z)
    }

    override fun set(x: Float, y: Float, z: Float, w: Float) {
        GL20.glUniform4f(location, x, y, z, w)
    }

    override fun set(x: Int) {
        GL20.glUniform1i(location, x)
    }

    override fun set(x: Int, y: Int) {
        GL20.glUniform2i(location, x, y)
    }

    override fun set(x: Int, y: Int, z: Int) {
        GL20.glUniform3i(location, x, y, z)
    }

    override fun set(x: Int, y: Int, z: Int, w: Int) {
        GL20.glUniform4i(location, x, y, z, w)
    }

    override fun set(valueArray: FloatArray) {
        when (valueArray.size) {
            1 -> GL20.glUniform1fv(location, valueArray)
            2 -> GL20.glUniform2fv(location, valueArray)
            3 -> GL20.glUniform3fv(location, valueArray)
            4 -> GL20.glUniform4fv(location, valueArray)
            else -> GL20.glUniformMatrix4fv(location, false, valueArray)
        }
    }

    override fun set(vector: Vector3f) {
        GL20.glUniform3f(location, vector.x, vector.y, vector.z)
    }

    override fun set(vector: Vector4f) {
        GL20.glUniform4f(location, vector.x, vector.y, vector.z, vector.w)
    }

    override fun set(matrix: Matrix4f) {
        val buffer = FloatArray(16)
        matrix.get(buffer)
        GL20.glUniformMatrix4fv(location, false, buffer)
    }

    override fun set(matrix: Matrix3f) {
        val buffer = FloatArray(9)
        matrix.get(buffer)
        GL20.glUniformMatrix3fv(location, false, buffer)
    }

    override fun setSafe(x: Float, y: Float, z: Float, w: Float) {
        GL20.glUniform4f(location, x, y, z, w)
    }

    override fun setSafe(x: Int, y: Int, z: Int, w: Int) {
        GL20.glUniform4i(location, x, y, z, w)
    }

    override fun setMat2x2(m00: Float, m01: Float, m10: Float, m11: Float) {
        GL20.glUniformMatrix2fv(location, false, floatArrayOf(m00, m01, m10, m11))
    }

    override fun setMat2x3(m00: Float, m01: Float, m02: Float, m10: Float, m11: Float, m12: Float) {
        GL21.glUniformMatrix2x3fv(location, false, floatArrayOf(m00, m01, m02, m10, m11, m12))
    }

    override fun setMat2x4(
        m00: Float,
        m01: Float,
        m02: Float,
        m03: Float,
        m10: Float,
        m11: Float,
        m12: Float,
        m13: Float
    ) {
        GL21.glUniformMatrix2x4fv(location, false, floatArrayOf(m00, m01, m02, m03, m10, m11, m12, m13))
    }

    override fun setMat3x2(m00: Float, m01: Float, m10: Float, m11: Float, m20: Float, m21: Float) {
        GL21.glUniformMatrix3x2fv(location, false, floatArrayOf(m00, m01, m10, m11, m20, m21))
    }

    override fun setMat3x3(
        m00: Float,
        m01: Float,
        m02: Float,
        m10: Float,
        m11: Float,
        m12: Float,
        m20: Float,
        m21: Float,
        m22: Float
    ) {
        GL20.glUniformMatrix3fv(location, false, floatArrayOf(m00, m01, m02, m10, m11, m12, m20, m21, m22))
    }

    override fun setMat3x4(
        m00: Float,
        m01: Float,
        m02: Float,
        m03: Float,
        m10: Float,
        m11: Float,
        m12: Float,
        m13: Float,
        m20: Float,
        m21: Float,
        m22: Float,
        m23: Float
    ) {
        GL21.glUniformMatrix3x4fv(
            location,
            false,
            floatArrayOf(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23)
        )
    }

    override fun setMat4x2(
        m00: Float,
        m01: Float,
        m02: Float,
        m03: Float,
        m10: Float,
        m11: Float,
        m12: Float,
        m13: Float
    ) {
        GL21.glUniformMatrix4x2fv(location, false, floatArrayOf(m00, m01, m02, m03, m10, m11, m12, m13))
    }

    override fun setMat4x3(
        m00: Float,
        m01: Float,
        m02: Float,
        m03: Float,
        m10: Float,
        m11: Float,
        m12: Float,
        m13: Float,
        m20: Float,
        m21: Float,
        m22: Float,
        m23: Float
    ) {
        GL21.glUniformMatrix4x3fv(
            location,
            false,
            floatArrayOf(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23)
        )
    }

    override fun setMat4x4(
        m00: Float,
        m01: Float,
        m02: Float,
        m03: Float,
        m10: Float,
        m11: Float,
        m12: Float,
        m13: Float,
        m20: Float,
        m21: Float,
        m22: Float,
        m23: Float,
        m30: Float,
        m31: Float,
        m32: Float,
        m33: Float
    ) {
        GL20.glUniformMatrix4fv(
            location,
            false,
            floatArrayOf(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33)
        )
    }
}