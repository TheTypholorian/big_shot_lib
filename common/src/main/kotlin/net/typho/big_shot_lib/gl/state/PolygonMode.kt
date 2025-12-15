package net.typho.big_shot_lib.gl.state

import org.lwjgl.opengl.GL11.*

object PolygonMode : GlState<PolygonMode.Mode> {
    override fun default() = Mode.DEFAULT

    override fun queryValue(): Mode? {
        val mode = glGetInteger(GL_POLYGON_MODE)
        return Mode.entries.find { it.id == mode }
    }

    override fun set(value: Mode) {
        glPolygonMode(GL_FRONT_AND_BACK, value.id)
    }

    enum class Mode(var id: Int) {
        POINT(GL_POINT),
        LINE(GL_LINE),
        FILL(GL_FILL);

        companion object {
            val DEFAULT = FILL
        }
    }
}
