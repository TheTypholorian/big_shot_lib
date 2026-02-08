package net.typho.big_shot_lib.api.state

import net.typho.big_shot_lib.api.GlNamed
import org.lwjgl.opengl.GL11.*

enum class PolygonMode(
    @JvmField
    val glId: Int
) : GlNamed {
    POINT(GL_POINT),
    LINE(GL_LINE),
    FILL(GL_FILL);

    override fun glId() = glId
}