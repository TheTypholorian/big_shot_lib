package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed
import org.lwjgl.opengl.GL11.GL_LINEAR
import org.lwjgl.opengl.GL11.GL_NEAREST

enum class GlTextureMinFilter(
    override val glId: Int
) : GlNamed {
    NEAREST(GL_NEAREST),
    LINEAR(GL_LINEAR),
}