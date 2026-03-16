package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import org.lwjgl.opengl.GL11.*

enum class GlTextureSwizzle(
    override val glId: Int
) : GlConstant {
    RED(GL_RED),
    GREEN(GL_GREEN),
    BLUE(GL_BLUE),
    ALPHA(GL_ALPHA),
    ONE(GL_ONE)
}