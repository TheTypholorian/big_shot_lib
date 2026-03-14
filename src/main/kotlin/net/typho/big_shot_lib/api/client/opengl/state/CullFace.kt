package net.typho.big_shot_lib.api.client.opengl.state

import net.typho.big_shot_lib.api.client.opengl.util.GlNamed
import org.lwjgl.opengl.GL11.*

enum class CullFace(
    override val glId: Int
) : GlNamed {
    FRONT(GL_FRONT),
    BACK(GL_BACK),
    FRONT_AND_BACK(GL_FRONT_AND_BACK);

    companion object {
        @JvmField
        val DEFAULT = BACK
    }
}