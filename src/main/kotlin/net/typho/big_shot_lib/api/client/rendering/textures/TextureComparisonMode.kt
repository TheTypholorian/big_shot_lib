package net.typho.big_shot_lib.api.client.rendering.textures

import net.typho.big_shot_lib.api.client.rendering.util.GlNamed
import org.lwjgl.opengl.GL11.GL_NONE
import org.lwjgl.opengl.GL30.GL_COMPARE_REF_TO_TEXTURE

enum class TextureComparisonMode(
    @JvmField
    val glId: Int
) : GlNamed {
    NONE(GL_NONE),
    COMPARE_REF_TO_TEXTURE(GL_COMPARE_REF_TO_TEXTURE);

    override fun glId() = glId
}