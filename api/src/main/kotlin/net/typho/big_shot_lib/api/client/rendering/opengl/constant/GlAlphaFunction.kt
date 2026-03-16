package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import org.lwjgl.opengl.GL11.*

enum class GlAlphaFunction(
    override val glId: Int
) : GlConstant {
    NEVER(GL_NEVER),
    LESS(GL_LESS),
    EQUAL(GL_EQUAL),
    LEQUAL(GL_LEQUAL),
    GREATER(GL_GREATER),
    NOTEQUAL(GL_NOTEQUAL),
    GEQUAL(GL_GEQUAL),
    ALWAYS(GL_ALWAYS)
}