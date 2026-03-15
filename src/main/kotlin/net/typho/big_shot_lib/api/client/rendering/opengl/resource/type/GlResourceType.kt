package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlConstant
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.glDeleteBuffers
import org.lwjgl.opengl.GL15.glGenBuffers
import org.lwjgl.opengl.GL43.GL_BUFFER

enum class GlResourceType(
    override val glId: Int,
    @JvmField
    val create: () -> Int,
    @JvmField
    val destroy: (glId: Int) -> Unit
): GlConstant {
    BUFFER(GL_BUFFER, ::glGenBuffers, ::glDeleteBuffers),
    TEXTURE(GL_TEXTURE, ::glGenTextures, ::glDeleteTextures),
}