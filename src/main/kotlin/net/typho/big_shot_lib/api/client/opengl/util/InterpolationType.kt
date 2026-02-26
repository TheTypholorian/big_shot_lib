package net.typho.big_shot_lib.api.client.opengl.util

import org.lwjgl.opengl.GL11.GL_LINEAR
import org.lwjgl.opengl.GL11.GL_NEAREST

enum class InterpolationType(
    override val glId: Int
): GlNamed {
    NEAREST(GL_NEAREST),
    LINEAR(GL_LINEAR)
}