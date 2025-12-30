package net.typho.big_shot_lib.gl.state

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20.glStencilOp

class StencilOp(
    var sfail: IntAction,
    var dpfail: IntAction,
    var dppass: IntAction
) : GlState<StencilOp>, GlState.Value {
    companion object {
        val DEFAULT = StencilOp(IntAction.DEFAULT, IntAction.DEFAULT, IntAction.DEFAULT)
    }

    override fun default() = DEFAULT

    override fun queryValue(): StencilOp? {
        val sfail = glGetInteger(GL_STENCIL_FAIL)
        val dpfail = glGetInteger(GL_STENCIL_PASS_DEPTH_FAIL)
        val dppass = glGetInteger(GL_STENCIL_PASS_DEPTH_PASS)
        return StencilOp(
            IntAction.entries.find { it.id == sfail }!!,
            IntAction.entries.find { it.id == dpfail }!!,
            IntAction.entries.find { it.id == dppass }!!
        )
    }

    override fun set(value: StencilOp) {
        glStencilOp(value.sfail.id, value.dpfail.id, value.dppass.id)
    }

    override fun getType() = this
}
