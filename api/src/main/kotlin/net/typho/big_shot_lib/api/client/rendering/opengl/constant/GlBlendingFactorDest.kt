package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import org.lwjgl.opengl.GL11.*

enum class GlBlendingFactorDest(
    override val glId: Int
) : GlConstant {
    ZERO(GL_ZERO),
    ONE(GL_ONE),
    SRC_COLOR(GL_SRC_COLOR),
    ONE_MINUS_SRC_COLOR(GL_ONE_MINUS_SRC_COLOR),
    SRC_ALPHA(GL_SRC_ALPHA),
    ONE_MINUS_SRC_ALPHA(GL_ONE_MINUS_SRC_ALPHA),
    DST_ALPHA(GL_DST_ALPHA),
    ONE_MINUS_DST_ALPHA(GL_ONE_MINUS_DST_ALPHA)
}