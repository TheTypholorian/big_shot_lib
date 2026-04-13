package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import net.typho.big_shot_lib.api.math.vec.IVec2
import net.typho.big_shot_lib.api.math.vec.IVec3
import net.typho.big_shot_lib.api.math.vec.IVec4
import org.joml.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL40.*

interface GlUniform {
    fun set(f1: Float)

    fun set(f1: Float, f2: Float)

    fun set(f1: Float, f2: Float, f3: Float)

    fun set(f1: Float, f2: Float, f3: Float, f4: Float)

    fun set(i1: Int)

    fun set(i1: Int, i2: Int)

    fun set(i1: Int, i2: Int, i3: Int)

    fun set(i1: Int, i2: Int, i3: Int, i4: Int)

    fun set(d1: Double)

    fun set(d1: Double, d2: Double)

    fun set(d1: Double, d2: Double, d3: Double)

    fun set(d1: Double, d2: Double, d3: Double, d4: Double)

    fun set(v: IVec2<*>)

    fun set(v: IVec3<*>)

    fun set(v: IVec4<*>)

    fun set(mat: Matrix2f, transpose: Boolean = false)

    fun set(mat: Matrix3f, transpose: Boolean = false)

    fun set(mat: Matrix4f, transpose: Boolean = false)

    fun set(mat: Matrix3x2f, transpose: Boolean = false)

    fun set(mat: Matrix4x3f, transpose: Boolean = false)

    open class Basic(
        @JvmField
        val location: Int
    ) : GlUniform {
        override fun set(f1: Float) {
            glUniform1f(location, f1)
        }

        override fun set(f1: Float, f2: Float) {
            glUniform2f(location, f1, f2)
        }

        override fun set(f1: Float, f2: Float, f3: Float) {
            glUniform3f(location, f1, f2, f3)
        }

        override fun set(f1: Float, f2: Float, f3: Float, f4: Float) {
            glUniform4f(location, f1, f2, f3, f4)
        }

        override fun set(i1: Int) {
            glUniform1i(location, i1)
        }

        override fun set(i1: Int, i2: Int) {
            glUniform2i(location, i1, i2)
        }

        override fun set(i1: Int, i2: Int, i3: Int) {
            glUniform3i(location, i1, i2, i3)
        }

        override fun set(i1: Int, i2: Int, i3: Int, i4: Int) {
            glUniform4i(location, i1, i2, i3, i4)
        }

        override fun set(d1: Double) {
            glUniform1d(location, d1)
        }

        override fun set(d1: Double, d2: Double) {
            glUniform2d(location, d1, d2)
        }

        override fun set(d1: Double, d2: Double, d3: Double) {
            glUniform3d(location, d1, d2, d3)
        }

        override fun set(d1: Double, d2: Double, d3: Double, d4: Double) {
            glUniform4d(location, d1, d2, d3, d4)
        }

        override fun set(v: IVec2<*>) {
            glUniform2f(location, v.x.toFloat(), v.y.toFloat())
        }

        override fun set(v: IVec3<*>) {
            glUniform3f(location, v.x.toFloat(), v.y.toFloat(), v.z.toFloat())
        }

        override fun set(v: IVec4<*>) {
            glUniform4f(location, v.x.toFloat(), v.y.toFloat(), v.z.toFloat(), v.w.toFloat())
        }

        override fun set(mat: Matrix2f, transpose: Boolean) {
            glUniformMatrix2fv(location, transpose, mat.get(FloatArray(4)))
        }

        override fun set(mat: Matrix3f, transpose: Boolean) {
            glUniformMatrix3fv(location, transpose, mat.get(FloatArray(9)))
        }

        override fun set(mat: Matrix4f, transpose: Boolean) {
            glUniformMatrix4fv(location, transpose, mat.get(FloatArray(16)))
        }

        override fun set(mat: Matrix3x2f, transpose: Boolean) {
            glUniformMatrix3x2fv(location, transpose, mat.get(FloatArray(6)))
        }

        override fun set(mat: Matrix4x3f, transpose: Boolean) {
            glUniformMatrix4x3fv(location, transpose, mat.get(FloatArray(12)))
        }
    }
}