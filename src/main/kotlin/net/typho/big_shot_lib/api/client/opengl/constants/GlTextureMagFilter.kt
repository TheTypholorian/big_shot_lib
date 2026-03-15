package net.typho.big_shot_lib.api.client.opengl.constants

import org.lwjgl.opengl.GL11.*

enum class GlTextureMagFilter(
    override val glId: Int
) : GlConstant {
    NEAREST(GL_NEAREST),
    LINEAR(GL_LINEAR),
    NEAREST_MIPMAP_NEAREST(GL_NEAREST_MIPMAP_NEAREST),
    LINEAR_MIPMAP_NEAREST(GL_LINEAR_MIPMAP_NEAREST),
    NEAREST_MIPMAP_LINEAR(GL_NEAREST_MIPMAP_LINEAR),
    LINEAR_MIPMAP_LINEAR(GL_LINEAR_MIPMAP_LINEAR)
}