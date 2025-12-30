package net.typho.big_shot_lib.gl.state

import org.lwjgl.opengl.GL11.*

enum class PolygonMode(val id: Int) : GlState<PolygonMode>, GlState.Value {
    POINT(GL_POINT),
    LINE(GL_LINE),
    FILL(GL_FILL);

    companion object {
        val DEFAULT = FILL
    }

    override fun default() = DEFAULT

    override fun queryValue(): PolygonMode? {
        val mode = glGetInteger(GL_POLYGON_MODE)
        return PolygonMode.entries.find { it.id == mode }
    }

    override fun set(value: PolygonMode) {
        glPolygonMode(GL_FRONT_AND_BACK, value.id)
    }

    override fun getType() = this
}
