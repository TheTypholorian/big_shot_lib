package net.typho.big_shot_lib.api.client.opengl.constants

import org.lwjgl.opengl.GL11.*

enum class GlPolygonMode(
    override val glId: Int
) : GlConstant {
    POINT(GL_POINT),
    LINE(GL_LINE),
    FILL(GL_FILL)
}