package net.typho.big_shot_lib.api.client.opengl.constants

import org.lwjgl.opengl.GL11.GL_LINEAR
import org.lwjgl.opengl.GL11.GL_NEAREST

enum class GlTextureMinFilter(
    override val glId: Int
) : GlConstant {
    NEAREST(GL_NEAREST),
    LINEAR(GL_LINEAR),
}