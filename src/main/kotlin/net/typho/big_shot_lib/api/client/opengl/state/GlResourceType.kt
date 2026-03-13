package net.typho.big_shot_lib.api.client.opengl.state

import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import org.lwjgl.opengl.GL11.GL_TEXTURE
import org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY
import org.lwjgl.opengl.GL30.GL_FRAMEBUFFER
import org.lwjgl.opengl.GL30.GL_RENDERBUFFER
import org.lwjgl.opengl.GL43.GL_BUFFER
import org.lwjgl.opengl.GL43.GL_PROGRAM

sealed interface GlResourceType {
    val name: String
    val glId: Int

    fun debugLabel(glId: Int, label: String) {
        OpenGL.INSTANCE.debugLabel(this.glId, glId, label)
    }

    fun create(): Int

    fun delete(glId: Int)

    object Buffer : GlResourceType {
        override val name = "Buffer"
        override val glId = GL_BUFFER

        override fun create() = OpenGL.INSTANCE.createBuffer()

        override fun delete(glId: Int) {
            OpenGL.INSTANCE.deleteBuffer(glId)
        }
    }

    object Framebuffer : GlResourceType {
        override val name = "Framebuffer"
        override val glId = GL_FRAMEBUFFER
        val state = GlStateType(glId, 0, name)

        override fun create() = OpenGL.INSTANCE.createFramebuffer()

        override fun delete(glId: Int) {
            OpenGL.INSTANCE.deleteFramebuffer(glId)
        }
    }

    object RenderBuffer : GlResourceType {
        override val name = "RenderBuffer"
        override val glId = GL_RENDERBUFFER
        val state = GlStateType(glId, 0, name)

        override fun create() = OpenGL.INSTANCE.createRenderBuffer()

        override fun delete(glId: Int) {
            OpenGL.INSTANCE.deleteRenderBuffer(glId)
        }
    }

    object Program : GlResourceType {
        override val name = "ShaderProgram"
        override val glId = GL_PROGRAM
        val state = GlStateType(glId, 0, name)

        override fun create() = OpenGL.INSTANCE.createShaderProgram()

        override fun delete(glId: Int) {
            OpenGL.INSTANCE.deleteShaderProgram(glId)
        }
    }

    object Texture : GlResourceType {
        override val name = "Texture"
        override val glId = GL_TEXTURE

        override fun create() = OpenGL.INSTANCE.createVertexArray()

        override fun delete(glId: Int) {
            OpenGL.INSTANCE.deleteVertexArray(glId)
        }
    }

    object VertexArray : GlResourceType {
        override val name = "VertexArray"
        override val glId = GL_VERTEX_ARRAY
        val state = GlStateType(glId, 0, name)

        override fun create() = OpenGL.INSTANCE.createVertexArray()

        override fun delete(glId: Int) {
            OpenGL.INSTANCE.deleteVertexArray(glId)
        }
    }
}