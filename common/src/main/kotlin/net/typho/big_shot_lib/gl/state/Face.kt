package net.typho.big_shot_lib.gl.state

import org.lwjgl.opengl.GL11.*

enum class Face(var id: Int) {
    FRONT(GL_FRONT),
    BACK(GL_BACK),
    FRONT_AND_BACK(GL_FRONT_AND_BACK);

    companion object {
        val DEFAULT = BACK
    }
}