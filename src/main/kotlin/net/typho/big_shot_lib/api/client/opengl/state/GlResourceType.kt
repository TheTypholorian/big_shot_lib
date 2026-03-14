package net.typho.big_shot_lib.api.client.opengl.state

import net.typho.big_shot_lib.api.client.opengl.util.GlNamed
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import org.lwjgl.opengl.GL11.GL_TEXTURE
import org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY
import org.lwjgl.opengl.GL30.GL_FRAMEBUFFER
import org.lwjgl.opengl.GL30.GL_RENDERBUFFER
import org.lwjgl.opengl.GL43.GL_BUFFER
import org.lwjgl.opengl.GL43.GL_PROGRAM

enum class GlResourceType(
    override val glId: Int,
    @JvmField
    val create: () -> Int,
    @JvmField
    val delete: (glId: Int) -> Unit
) : GlNamed {
    BUFFER(GL_BUFFER, OpenGL.INSTANCE::createBuffer, OpenGL.INSTANCE::deleteBuffer),
    FRAMEBUFFER(GL_FRAMEBUFFER, OpenGL.INSTANCE::createFramebuffer, OpenGL.INSTANCE::deleteFramebuffer),
    RENDER_BUFFER(GL_RENDERBUFFER, OpenGL.INSTANCE::createRenderBuffer, OpenGL.INSTANCE::deleteRenderBuffer),
    SHADER_PROGRAM(GL_PROGRAM, OpenGL.INSTANCE::createShaderProgram, OpenGL.INSTANCE::deleteShaderProgram),
    TEXTURE(GL_TEXTURE, OpenGL.INSTANCE::createTexture, OpenGL.INSTANCE::deleteTexture),
    VERTEX_ARRAY(GL_VERTEX_ARRAY, OpenGL.INSTANCE::createVertexArray, OpenGL.INSTANCE::deleteVertexArray);

    fun debugLabel(glId: Int, label: String) {
        OpenGL.INSTANCE.debugLabel(this.glId, glId, label)
    }
}