package net.typho.big_shot_lib.api.client.opengl.constants

import org.lwjgl.opengl.GL11.*

enum class GlTextureParameterName(
    override val glId: Int
) : GlConstant {
    TEXTURE_MAG_FILTER(GL_TEXTURE_MAG_FILTER),
    TEXTURE_MIN_FILTER(GL_TEXTURE_MIN_FILTER),
    TEXTURE_WRAP_S(GL_TEXTURE_WRAP_S),
    TEXTURE_WRAP_T(GL_TEXTURE_WRAP_T)
}