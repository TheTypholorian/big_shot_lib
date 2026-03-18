package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL14.GL_DECR_WRAP
import org.lwjgl.opengl.GL14.GL_INCR_WRAP

enum class GlStencilAction(
    override val glId: Int
) : GlConstant {
    KEEP(GL_KEEP),
    REPLACE(GL_REPLACE),
    INCR(GL_INCR),
    INCR_WRAP(GL_INCR_WRAP),
    DECR(GL_DECR),
    DECR_WRAP(GL_DECR_WRAP)
}