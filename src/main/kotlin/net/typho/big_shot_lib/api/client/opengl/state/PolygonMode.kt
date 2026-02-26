package net.typho.big_shot_lib.api.client.opengl.state

import net.typho.big_shot_lib.api.client.opengl.util.GlNamed
import org.lwjgl.opengl.GL11.*

enum class PolygonMode(
    override val glId: Int
) : GlNamed {
    POINT(GL_POINT),
    LINE(GL_LINE),
    FILL(GL_FILL)
}