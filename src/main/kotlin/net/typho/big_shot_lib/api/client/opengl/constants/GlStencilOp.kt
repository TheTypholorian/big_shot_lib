package net.typho.big_shot_lib.api.client.opengl.constants

import org.lwjgl.opengl.GL11.*

enum class GlStencilOp(
    override val glId: Int
) : GlConstant {
    KEEP(GL_KEEP),
    REPLACE(GL_REPLACE),
    INCR(GL_INCR),
    DECR(GL_DECR)
}