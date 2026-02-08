package net.typho.big_shot_lib.api.state

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL14.GL_DECR_WRAP
import org.lwjgl.opengl.GL14.GL_INCR_WRAP

enum class IntAction(
    @JvmField
    val glId: Int
) {
    KEEP(GL_KEEP),
    ZERO(GL_ZERO),
    REPLACE(GL_REPLACE),
    INCR(GL_INCR),
    INCR_WRAP(GL_INCR_WRAP),
    DECR(GL_DECR),
    DECR_WRAP(GL_DECR_WRAP),
    INVERT(GL_INVERT)
}