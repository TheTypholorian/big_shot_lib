package net.typho.big_shot_lib.api.client.rendering.opengl.util

import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL43.*

enum class GlFlag(
    override val glId: Int
) : GlNamed {
    BLEND(GL_BLEND),
    COLOR_LOGIC_OP(GL_COLOR_LOGIC_OP),
    CULL_FACE(GL_CULL_FACE),
    DEPTH_TEST(GL_DEPTH_TEST),
    POLYGON_OFFSET(GL_POLYGON_OFFSET_FILL),
    SCISSOR_TEST(GL_SCISSOR_TEST),
    STENCIL_TEST(GL_STENCIL_TEST)
}