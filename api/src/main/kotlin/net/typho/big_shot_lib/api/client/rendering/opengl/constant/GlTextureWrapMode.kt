package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import org.lwjgl.opengl.GL11.GL_CLAMP
import org.lwjgl.opengl.GL11.GL_REPEAT
import org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER

enum class GlTextureWrapMode(
    override val glId: Int
) : GlConstant {
    CLAMP(GL_CLAMP),
    CLAMP_TO_BORDER(GL_CLAMP_TO_BORDER),
    REPEAT(GL_REPEAT)
}