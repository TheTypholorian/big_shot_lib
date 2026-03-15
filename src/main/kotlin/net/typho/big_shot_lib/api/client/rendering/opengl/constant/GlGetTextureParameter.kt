package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import org.lwjgl.opengl.GL11.*

enum class GlGetTextureParameter(
    override val glId: Int
) : GlConstant {
    WIDTH(GL_TEXTURE_WIDTH),
    HEIGHT(GL_TEXTURE_HEIGHT),
    INTERNAL_FORMAT(GL_TEXTURE_INTERNAL_FORMAT),
    COMPONENTS(GL_TEXTURE_COMPONENTS),
    BORDER_COLOR(GL_TEXTURE_BORDER_COLOR),
    BORDER(GL_TEXTURE_BORDER)
}