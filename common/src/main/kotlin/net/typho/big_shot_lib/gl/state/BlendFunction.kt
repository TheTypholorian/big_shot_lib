package net.typho.big_shot_lib.gl.state

import org.lwjgl.opengl.GL14.*

object BlendFunction : GlState<BlendFunction.Mode> {
    override fun default() = Mode.DEFAULT

    override fun queryValue(): Mode? {
        val src = glGetInteger(GL_BLEND_SRC_RGB)
        val dst = glGetInteger(GL_BLEND_DST_RGB)
        return Mode(
            Factor.entries.find { it.id == src }!!,
            Factor.entries.find { it.id == dst }!!
        )
    }

    override fun set(value: Mode) {
        glBlendFunc(value.src.id, value.dst.id)
    }

    class Mode(
        var src: Factor,
        var dst: Factor
    ) {
        companion object {
            val DEFAULT = Mode(Factor.SRC_ALPHA, Factor.ONE_MINUS_SRC_ALPHA)
        }
    }

    enum class Factor(var id: Int) {
        ZERO(GL_ZERO),
        ONE(GL_ONE),

        SRC_COLOR(GL_SRC_COLOR),
        ONE_MINUS_SRC_COLOR(GL_ONE_MINUS_SRC_COLOR),
        DST_COLOR(GL_DST_COLOR),
        ONE_MINUS_DST_COLOR(GL_ONE_MINUS_DST_COLOR),
        CONSTANT_COLOR(GL_CONSTANT_COLOR),
        ONE_MINUS_CONSTANT_COLOR(GL_ONE_MINUS_CONSTANT_COLOR),

        SRC_ALPHA(GL_SRC_ALPHA),
        ONE_MINUS_SRC_ALPHA(GL_ONE_MINUS_SRC_ALPHA),
        DST_ALPHA(GL_DST_ALPHA),
        ONE_MINUS_DST_ALPHA(GL_ONE_MINUS_DST_ALPHA),
        CONSTANT_ALPHA(GL_CONSTANT_ALPHA),
        ONE_MINUS_CONSTANT_ALPHA(GL_ONE_MINUS_CONSTANT_ALPHA),

        SRC_ALPHA_SATURATE(GL_SRC_ALPHA_SATURATE)
    }
}
