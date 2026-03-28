package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import org.lwjgl.opengl.GL11.*

enum class GlBufferBit(
    override val glId: Int
) : GlConstant {
    COLOR(GL_COLOR_BUFFER_BIT),
    DEPTH(GL_DEPTH_BUFFER_BIT),
    STENCIL(GL_STENCIL_BUFFER_BIT)
}