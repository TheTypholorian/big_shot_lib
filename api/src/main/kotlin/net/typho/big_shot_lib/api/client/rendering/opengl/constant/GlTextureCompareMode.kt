package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import org.lwjgl.opengl.GL11.GL_NONE
import org.lwjgl.opengl.GL30.GL_COMPARE_REF_TO_TEXTURE

enum class GlTextureCompareMode(
    override val glId: Int
) : GlConstant {
    NONE(GL_NONE),
    COMPARE_REF_TO_TEXTURE(GL_COMPARE_REF_TO_TEXTURE)
}