package net.typho.big_shot_lib.gl.state

import org.lwjgl.opengl.GL11.GL_STENCIL_WRITEMASK
import org.lwjgl.opengl.GL11.glGetInteger
import org.lwjgl.opengl.GL20.glStencilMask

object StencilMask : GlState<Int> {
    override fun default() = 0xFF

    override fun queryValue(): Int? {
        return glGetInteger(GL_STENCIL_WRITEMASK)
    }

    override fun set(value: Int) {
        glStencilMask(value)
    }
}
