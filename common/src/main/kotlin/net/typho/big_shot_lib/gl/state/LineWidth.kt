package net.typho.big_shot_lib.gl.state

import org.lwjgl.opengl.GL11.*

object LineWidth : GlState<Float> {
    override fun default() = 1f

    override fun queryValue(): Float? {
        return glGetFloat(GL_LINE_WIDTH)
    }

    override fun set(value: Float) {
        glLineWidth(value)
    }
}
