package net.typho.big_shot_lib.gl.state

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20.glStencilFunc

class StencilFunc(
    var func: ComparisonMode,
    var ref: Int,
    var mask: Int
) : GlState<StencilFunc>, GlState.Value {
    companion object {
        val DEFAULT = StencilFunc(ComparisonMode.ALWAYS, 0xFF, 0xFF)
    }

    override fun default() = DEFAULT

    override fun queryValue(): StencilFunc? {
        val func = glGetInteger(GL_STENCIL_FUNC)
        return StencilFunc(
            ComparisonMode.entries.find { it.id == func }!!,
            glGetInteger(GL_STENCIL_REF),
            glGetInteger(GL_STENCIL_VALUE_MASK)
        )
    }

    override fun set(value: StencilFunc) {
        glStencilFunc(value.func.id, value.ref, value.mask)
    }

    override fun getType() = this
}
