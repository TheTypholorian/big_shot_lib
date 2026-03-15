package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import org.lwjgl.opengl.GL11.*

enum class GlBlendingFactorSrc(
    override val glId: Int
) : net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlConstant {
    DST_COLOR(GL_DST_COLOR),
    ONE_MINUS_DST_COLOR(GL_ONE_MINUS_DST_COLOR),
    SRC_ALPHA_SATURATE(GL_SRC_ALPHA_SATURATE)
}