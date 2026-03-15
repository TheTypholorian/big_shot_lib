package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import org.lwjgl.opengl.GL11.GL_CLAMP
import org.lwjgl.opengl.GL11.GL_REPEAT

enum class GlTextureWrapMode(
    override val glId: Int
) : GlConstant {
    CLAMP(GL_CLAMP),
    REPEAT(GL_REPEAT)
}