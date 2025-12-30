package net.typho.big_shot_lib.gl.state

import org.lwjgl.opengl.GL11.*

enum class ComparisonMode(val id: Int) {
    NEVER(GL_NEVER),
    ALWAYS(GL_ALWAYS),
    LESS(GL_LESS),
    LEQUAL(GL_LEQUAL),
    EQUAL(GL_EQUAL),
    GREATER(GL_GREATER),
    GEQUAL(GL_GEQUAL),
    NOTEQUAL(GL_NOTEQUAL);

    companion object {
        val DEFAULT = LEQUAL
    }
}