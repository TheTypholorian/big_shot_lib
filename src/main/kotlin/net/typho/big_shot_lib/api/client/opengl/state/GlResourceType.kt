package net.typho.big_shot_lib.api.client.opengl.state

import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import org.lwjgl.opengl.*
import org.lwjgl.opengl.GL11.GL_TEXTURE
import org.lwjgl.opengl.GL30.GL_FRAMEBUFFER
import org.lwjgl.opengl.GL30.GL_RENDERBUFFER
import org.lwjgl.opengl.GL43.GL_PROGRAM
import org.lwjgl.opengl.GL43.glObjectLabel

sealed class GlResourceType(glId: Int) : GlStateType<Int>(glId, 0) {
    abstract fun debugLabel(glId: Int, label: String)

    abstract fun create(): Int

    abstract fun delete(glId: Int)

    sealed class Buffer(
        glId: Int,
        @JvmField
        val bindingId: Int
    ) : GlResourceType(glId) {
        override fun debugLabel(glId: Int, label: String) {
            glObjectLabel(GL43.GL_BUFFER, glId, label)
        }

        override fun create() = OpenGL.INSTANCE.createBuffer()

        override fun delete(glId: Int) {
            OpenGL.INSTANCE.deleteBuffer(glId)
        }

        override fun rawBind(value: Int) {
            OpenGL.INSTANCE.bindBuffer(this, value)
        }

        override fun rawQueryValue() = OpenGL.INSTANCE.getBoundBuffer(this)

        class Normal(
            glId: Int,
            bindingId: Int,
            override val name: String
        ) : Buffer(glId, bindingId) {
            companion object {
                @JvmField
                val ARRAY_BUFFER = Normal(GL15.GL_ARRAY_BUFFER, GL15.GL_ARRAY_BUFFER_BINDING, "ArrayBuffer")
                @JvmField
                val PIXEL_PACK_BUFFER = Normal(GL21.GL_PIXEL_PACK_BUFFER, GL21.GL_PIXEL_PACK_BUFFER_BINDING, "PixelPackBuffer")
                @JvmField
                val PIXEL_UNPACK_BUFFER = Normal(GL21.GL_PIXEL_UNPACK_BUFFER, GL21.GL_PIXEL_UNPACK_BUFFER_BINDING, "PixelUnpackBuffer")
                @JvmField
                val TEXTURE_BUFFER = Normal(GL31.GL_TEXTURE_BUFFER, GL44.GL_TEXTURE_BUFFER_BINDING, "TextureBuffer")
                @JvmField
                val COPY_READ_BUFFER = Normal(GL31.GL_COPY_READ_BUFFER, GL42.GL_COPY_READ_BUFFER_BINDING, "CopyReadBuffer")
                @JvmField
                val COPY_WRITE_BUFFER = Normal(GL31.GL_COPY_WRITE_BUFFER, GL42.GL_COPY_WRITE_BUFFER_BINDING, "CopyWriteBuffer")
                @JvmField
                val DRAW_INDIRECT_BUFFER = Normal(GL40.GL_DRAW_INDIRECT_BUFFER, GL40.GL_DRAW_INDIRECT_BUFFER_BINDING, "DrawIndirectBuffer")
                @JvmField
                val DISPATCH_INDIRECT_BUFFER = Normal(GL43.GL_DISPATCH_INDIRECT_BUFFER, GL43.GL_DISPATCH_INDIRECT_BUFFER_BINDING, "DispatchIndirectBuffer")
                @JvmField
                val QUERY_BUFFER = Normal(GL44.GL_QUERY_BUFFER, GL44.GL_QUERY_BUFFER_BINDING, "QueryBuffer")
            }
        }

        class Indexed(
            glId: Int,
            bindingId: Int,
            override val name: String
        ) : Buffer(glId, bindingId) {
            companion object {
                @JvmField
                val TRANSFORM_FEEDBACK_BUFFER = Indexed(GL30.GL_TRANSFORM_FEEDBACK_BUFFER, GL30.GL_TRANSFORM_FEEDBACK_BUFFER_BINDING, "TransformFeedbackBuffer")
                @JvmField
                val UNIFORM_BUFFER = Indexed(GL31.GL_UNIFORM_BUFFER, GL31.GL_UNIFORM_BUFFER_BINDING, "UniformBuffer")
                @JvmField
                val ATOMIC_COUNTER_BUFFER = Indexed(GL42.GL_ATOMIC_COUNTER_BUFFER, GL42.GL_ATOMIC_COUNTER_BUFFER_BINDING, "AtomicCounterBuffer")
                @JvmField
                val SHADER_STORAGE_BUFFER = Indexed(GL43.GL_SHADER_STORAGE_BUFFER, GL43.GL_SHADER_STORAGE_BUFFER_BINDING, "ShaderStorageBuffer")
            }
        }
    }

    object Framebuffer : GlResourceType(GL_FRAMEBUFFER) {
        override val name = "Framebuffer"

        override fun debugLabel(glId: Int, label: String) {
            glObjectLabel(this.glId, glId, label)
        }

        override fun create() = OpenGL.INSTANCE.createFramebuffer()

        override fun delete(glId: Int) {
            OpenGL.INSTANCE.deleteFramebuffer(glId)
        }

        override fun rawBind(value: Int) {
            OpenGL.INSTANCE.bindFramebuffer(value)
        }

        override fun rawQueryValue() = OpenGL.INSTANCE.getBoundFramebuffer()
    }

    object RenderBuffer : GlResourceType(GL_RENDERBUFFER) {
        override val name = "RenderBuffer"

        override fun debugLabel(glId: Int, label: String) {
            glObjectLabel(this.glId, glId, label)
        }

        override fun create() = OpenGL.INSTANCE.createRenderBuffer()

        override fun delete(glId: Int) {
            OpenGL.INSTANCE.deleteRenderBuffer(glId)
        }

        override fun rawBind(value: Int) {
            OpenGL.INSTANCE.bindRenderBuffer(value)
        }

        override fun rawQueryValue() = OpenGL.INSTANCE.getBoundRenderBuffer()
    }

    object Program : GlResourceType(GL_PROGRAM) {
        override val name = "PROGRAM"

        override fun debugLabel(glId: Int, label: String) {
            glObjectLabel(GL_PROGRAM, glId, label)
        }

        override fun create() = OpenGL.INSTANCE.createShaderProgram()

        override fun delete(glId: Int) {
            OpenGL.INSTANCE.deleteShaderProgram(glId)
        }

        override fun rawBind(value: Int) {
            OpenGL.INSTANCE.bindShaderProgram(value)
        }

        override fun rawQueryValue() = OpenGL.INSTANCE.getBoundShaderProgram()
    }

    sealed class Texture(
        glId: Int,
        @JvmField
        val bindingId: Int
    ) : GlResourceType(glId) {
        override fun debugLabel(glId: Int, label: String) {
            glObjectLabel(GL_TEXTURE, glId, label)
        }

        override fun create() = OpenGL.INSTANCE.createTexture()

        override fun delete(glId: Int) {
            OpenGL.INSTANCE.deleteTexture(glId)
        }

        override fun rawBind(value: Int) {
            OpenGL.INSTANCE.bindTexture(this, value)
        }

        override fun rawQueryValue() = OpenGL.INSTANCE.getBoundTexture(this)

        class Normal(
            glId: Int,
            bindingId: Int,
            override val name: String
        ) : Texture(glId, bindingId) {
            companion object {
                @JvmField
                val TEXTURE_1D = Normal(GL11.GL_TEXTURE_1D, GL11.GL_TEXTURE_BINDING_1D, "Texture1D")
                @JvmField
                val TEXTURE_2D = Normal(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_BINDING_2D, "Texture2D")
                @JvmField
                val TEXTURE_3D = Normal(org.lwjgl.opengl.GL12.GL_TEXTURE_3D, org.lwjgl.opengl.GL12.GL_TEXTURE_BINDING_3D, "Texture3D")
                @JvmField
                val TEXTURE_1D_ARRAY = Normal(GL30.GL_TEXTURE_1D_ARRAY,GL30.GL_TEXTURE_BINDING_1D_ARRAY, "Texture1DArray")
                @JvmField
                val TEXTURE_2D_ARRAY = Normal(GL30.GL_TEXTURE_2D_ARRAY,GL30.GL_TEXTURE_BINDING_2D_ARRAY, "Texture2DArray")
                @JvmField
                val TEXTURE_RECTANGLE = Normal(GL31.GL_TEXTURE_RECTANGLE,GL31.GL_TEXTURE_BINDING_RECTANGLE, "TextureRectangle")
                @JvmField
                val TEXTURE_CUBE_MAP = Normal(org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP,org.lwjgl.opengl.GL13.GL_TEXTURE_BINDING_CUBE_MAP, "TextureCubeMap")
                @JvmField
                val TEXTURE_CUBE_MAP_ARRAY = Normal(GL40.GL_TEXTURE_CUBE_MAP_ARRAY,GL40.GL_TEXTURE_BINDING_CUBE_MAP_ARRAY, "TextureCubeMapArray")
                @JvmField
                val TEXTURE_BUFFER = Normal(GL31.GL_TEXTURE_BUFFER, GL31.GL_TEXTURE_BINDING_BUFFER, "TextureBuffer")
            }
        }

        class Multisample(
            glId: Int,
            bindingId: Int,
            override val name: String
        ) : Texture(glId, bindingId) {
            companion object {
                @JvmField
                val TEXTURE_2D_MULTISAMPLE = Multisample(org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE,org.lwjgl.opengl.GL32.GL_TEXTURE_BINDING_2D_MULTISAMPLE)
                @JvmField
                val TEXTURE_2D_MULTISAMPLE_ARRAY = Multisample(org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE_ARRAY,org.lwjgl.opengl.GL32.GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY)
            }
        }
    }

    object VertexArray : GlResourceType {
        override val name = "VERTEX_ARRAY"
        override val glId = GL11.GL_VERTEX_ARRAY
        override val stateStack = GlStateStack(
            name,
            OpenGL.Companion.INSTANCE::bindVertexArray,
            OpenGL.Companion.INSTANCE::getBoundVertexArray
        )

        override fun debugLabel(glId: Int, label: String) {
            glObjectLabel(GL11.GL_VERTEX_ARRAY, glId, label)
        }
    }
}