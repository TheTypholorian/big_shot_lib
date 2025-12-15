package net.typho.big_shot_lib.gl.state

import org.lwjgl.opengl.GL11.*

object CullFace : GlState<Face> {
    override fun default() = Face.DEFAULT

    override fun queryValue(): Face? {
        val face = glGetInteger(GL_CULL_FACE)
        return Face.entries.find { it.id == face }
    }

    override fun set(value: Face) {
        glCullFace(value.id)
    }
}
