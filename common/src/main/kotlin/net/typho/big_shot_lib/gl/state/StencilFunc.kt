package net.typho.big_shot_lib.gl.state

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20.glStencilFuncSeparate

object StencilFunc : GlState<StencilFunc.Mode> {
    override fun default() = Mode.DEFAULT

    override fun queryValue(): Mode? {
        val func = glGetInteger(GL_STENCIL_FUNC)
        return Mode(
            ComparisonMode.entries.find { it.id == func }!!,
            glGetInteger(GL_STENCIL_REF),
            glGetInteger(GL_STENCIL_VALUE_MASK)
        )
    }

    override fun set(value: Mode) {
        glStencilFuncSeparate(value.face.id, value.func.id, value.ref, value.mask)
    }

    class Mode(
        var func: ComparisonMode,
        var ref: Int,
        var mask: Int,
        var face: Face = Face.FRONT_AND_BACK
    ) {
        companion object {
            val DEFAULT = Mode(ComparisonMode.ALWAYS, 0xFF, 0xFF)
        }
    }
}
