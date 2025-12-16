package net.typho.big_shot_lib.gl.state

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20.glStencilOp

object StencilOp : GlState<StencilOp.Mode> {
    override fun default() = Mode.DEFAULT

    override fun queryValue(): Mode? {
        val sfail = glGetInteger(GL_STENCIL_FAIL)
        val dpfail = glGetInteger(GL_STENCIL_PASS_DEPTH_FAIL)
        val dppass = glGetInteger(GL_STENCIL_PASS_DEPTH_PASS)
        return Mode(
            IntAction.entries.find { it.id == sfail }!!,
            IntAction.entries.find { it.id == dpfail }!!,
            IntAction.entries.find { it.id == dppass }!!
        )
    }

    override fun set(value: Mode) {
        glStencilOp(value.sfail.id, value.dpfail.id, value.dppass.id)
    }

    class Mode(
        var sfail: IntAction,
        var dpfail: IntAction,
        var dppass: IntAction
    ) {
        companion object {
            val DEFAULT = Mode(IntAction.DEFAULT, IntAction.DEFAULT, IntAction.DEFAULT)
        }
    }
}
