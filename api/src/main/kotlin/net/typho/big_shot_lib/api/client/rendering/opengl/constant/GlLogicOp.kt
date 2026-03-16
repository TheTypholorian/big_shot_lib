package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import org.lwjgl.opengl.GL11.*

enum class GlLogicOp(
    override val glId: Int
) : GlConstant {
    CLEAR(GL_CLEAR),
    AND(GL_AND),
    AND_REVERSE(GL_AND_REVERSE),
    COPY(GL_COPY),
    AND_INVERTED(GL_AND_INVERTED),
    NOOP(GL_NOOP),
    XOR(GL_XOR),
    OR(GL_OR),
    NOR(GL_NOR),
    EQUIV(GL_EQUIV),
    INVERT(GL_INVERT),
    OR_REVERSE(GL_OR_REVERSE),
    COPY_INVERTED(GL_COPY_INVERTED),
    OR_INVERTED(GL_OR_INVERTED),
    NAND(GL_NAND),
    SET(GL_SET)
}