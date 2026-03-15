package net.typho.big_shot_lib.api.client.opengl.constants

import org.lwjgl.opengl.GL11.*

enum class GlAccumOp(
    override val glId: Int
) : GlConstant {
    ACCUM(GL_ACCUM),
    LOAD(GL_LOAD),
    RETURN(GL_RETURN),
    MULT(GL_MULT),
    ADD(GL_ADD)
}