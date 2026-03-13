package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.util.*
import net.typho.big_shot_lib.api.util.buffers.BufferUploader
import java.nio.ByteBuffer

open class NeoTextureCube(
    glId: Int,
    override val format: TextureFormat,
    defaultParams: Boolean = true,
    override val type: GlTextureResourceType = GlTextureResourceType.TEXTURE_CUBE_MAP
) : GlResource(glId, GlStateStack.textures[type]!!), GlTextureCube {
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

    override fun resize(width: Int, height: Int, upload: (uploader: BufferUploader) -> Unit) {
        upload(object : BufferUploader {
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
        })
    }

    override fun toString(): String {
        return "${type.name}(glId=$glId, format=$format)"
    }
}