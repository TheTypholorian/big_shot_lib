package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import org.lwjgl.opengl.GL11.*

enum class GlPolygonMode(
    override val glId: Int
) : net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlConstant {
    POINT(GL_POINT),
    LINE(GL_LINE),
    FILL(GL_FILL)
}