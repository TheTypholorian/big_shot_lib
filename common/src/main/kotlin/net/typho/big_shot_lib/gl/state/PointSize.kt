package net.typho.big_shot_lib.gl.state

import org.lwjgl.opengl.GL11.*

object PointSize : GlState<Float> {
    override fun default() = 1f

    override fun queryValue(): Float? {
        return glGetFloat(GL_POINT_SIZE)
    }

    override fun set(value: Float) {
        glPointSize(value)
    }
}
