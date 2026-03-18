package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed
import org.lwjgl.opengl.GL11

enum class GlCullFace(
    override val glId: Int
) : GlNamed {
    FRONT(GL11.GL_FRONT),
    BACK(GL11.GL_BACK),
    FRONT_AND_BACK(GL11.GL_FRONT_AND_BACK);

    companion object {
        @JvmField
        val DEFAULT = BACK
    }
}