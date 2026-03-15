package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import org.lwjgl.opengl.GL11.*

enum class GlErrorCode(
    override val glId: Int
) : net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlConstant {
    NO_ERROR(GL_NO_ERROR),
    INVALID_ENUM(GL_INVALID_ENUM),
    INVALID_VALUE(GL_INVALID_VALUE),
    INVALID_OPERATION(GL_INVALID_OPERATION),
    STACK_OVERFLOW(GL_STACK_OVERFLOW),
    STACK_UNDERFLOW(GL_STACK_UNDERFLOW),
    OUT_OF_MEMORY(GL_OUT_OF_MEMORY)
}