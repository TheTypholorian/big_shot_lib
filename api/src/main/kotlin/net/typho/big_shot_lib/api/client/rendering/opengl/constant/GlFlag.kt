package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed
import org.lwjgl.opengl.GL11

enum class GlFlag(
    override val glId: Int
) : GlNamed {
    BLEND(GL11.GL_BLEND),
    COLOR_LOGIC_OP(GL11.GL_COLOR_LOGIC_OP),
    CULL_FACE(GL11.GL_CULL_FACE),
    DEPTH_TEST(GL11.GL_DEPTH_TEST),
    POLYGON_OFFSET(GL11.GL_POLYGON_OFFSET_FILL),
    SCISSOR_TEST(GL11.GL_SCISSOR_TEST),
    STENCIL_TEST(GL11.GL_STENCIL_TEST)
}