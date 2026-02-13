package net.typho.big_shot_lib.api.client.rendering.textures

import net.typho.big_shot_lib.api.client.rendering.util.GlNamed
import org.lwjgl.opengl.GL11.GL_LINEAR
import org.lwjgl.opengl.GL11.GL_NEAREST

enum class InterpolationType(
    @JvmField
    val glId: Int
): GlNamed {
    NEAREST(GL_NEAREST),
    LINEAR(GL_LINEAR);

    override fun glId() = glId
}