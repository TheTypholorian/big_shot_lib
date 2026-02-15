package net.typho.big_shot_lib.api.client.rendering.textures

import net.typho.big_shot_lib.api.client.rendering.state.GlStateStack
import net.typho.big_shot_lib.api.client.rendering.state.OpenGL
import net.typho.big_shot_lib.api.client.rendering.util.GlResource
import net.typho.big_shot_lib.api.util.buffers.BufferUploader
import java.nio.ByteBuffer

open class NeoTextureCube(
    glId: Int,
    @JvmField
    val format: TextureFormat,
    defaultParams: Boolean = true
) : GlResource(glId, GlStateStack.textures[TextureType.CUBE_MAP]!!), GlTextureCube {
    companion object {
        @JvmField
        val NULL = NeoTextureCube(0, TextureFormat.NULL)
    }

    constructor(format: TextureFormat) : this(OpenGL.INSTANCE.createTexture(), format)

    init {
        if (defaultParams) {
            setInterpolation(InterpolationType.NEAREST, InterpolationType.NEAREST)
            setWrapping(WrappingType.CLAMP_TO_EDGE, WrappingType.CLAMP_TO_EDGE)
        }
    }

    override fun free() {
        OpenGL.INSTANCE.deleteTexture(glId)
    }

    override fun type() = TextureType.CUBE_MAP

    override fun format() = format

    override fun attachToFramebuffer(attachment: Int) {
        OpenGL.INSTANCE.attachFramebufferTexture(attachment, glId)
    }

    override fun resize(
        face: GlTextureCube.Face,
        width: Int,
        height: Int
    ): BufferUploader {
        return object : BufferUploader {
            override fun upload(buffer: ByteBuffer) {
                bind()
                OpenGL.INSTANCE.textureData2D(face, format, width, height, buffer)
                unbind()
            }

            override fun uploadNull() {
                bind()
                OpenGL.INSTANCE.textureData2D(face, format, width, height, 0L)
                unbind()
            }
        }
    }

    override fun resize(width: Int, height: Int): BufferUploader {
        return object : BufferUploader {
            override fun upload(buffer: ByteBuffer) {
                bind()

                for (face in GlTextureCube.Face.entries) {
                    OpenGL.INSTANCE.textureData2D(face, format, width, height, buffer)
                }

                unbind()
            }

            override fun uploadNull() {
                bind()

                for (face in GlTextureCube.Face.entries) {
                    OpenGL.INSTANCE.textureData2D(face, format, width, height, 0L)
                }

                unbind()
            }
        }
    }

    override fun toString(): String {
        return "${javaClass.simpleName}(glId=$glId, format=$format)"
    }
}