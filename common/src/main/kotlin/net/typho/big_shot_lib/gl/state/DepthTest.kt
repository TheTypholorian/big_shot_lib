package net.typho.big_shot_lib.gl.state

import org.lwjgl.opengl.GL11.*

object DepthTest : GlState<ComparisonMode> {
    override fun default() = ComparisonMode.DEFAULT

    override fun queryValue(): ComparisonMode? {
        val depth = glGetInteger(GL_DEPTH_FUNC)
        return ComparisonMode.entries.find { it.id == depth }
    }

    override fun set(value: ComparisonMode) {
        glDepthFunc(value.id)
    }
}
