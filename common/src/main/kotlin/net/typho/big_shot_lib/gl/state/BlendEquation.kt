package net.typho.big_shot_lib.gl.state

import org.lwjgl.opengl.GL14.*
import org.lwjgl.opengl.GL20.GL_BLEND_EQUATION_RGB

object BlendEquation : GlState<BlendEquation.Mode> {
    override fun default() = Mode.DEFAULT

    override fun queryValue(): Mode? {
        val id = glGetInteger(GL_BLEND_EQUATION_RGB)
        return Mode.entries.find { it.id == id }
    }

    override fun set(value: Mode) {
        glBlendEquation(value.id)
    }

    enum class Mode(var id: Int) {
        ADD(GL_FUNC_ADD),
        SUBTRACT(GL_FUNC_SUBTRACT),
        REVERSE_SUBTRACT(GL_FUNC_REVERSE_SUBTRACT),
        MIN(GL_MIN),
        MAX(GL_MAX);

        companion object {
            val DEFAULT = ADD
        }
    }
}
