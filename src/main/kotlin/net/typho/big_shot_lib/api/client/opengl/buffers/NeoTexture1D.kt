package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.state.GlStateStack
import net.typho.big_shot_lib.api.client.opengl.util.*
import net.typho.big_shot_lib.api.util.buffers.BufferUploader
import java.nio.ByteBuffer

open class NeoTexture1D(
    glId: Int,
    @JvmField
    val format: TextureFormat,
    defaultParams: Boolean = true
) : GlResource(glId, GlStateStack.textures[TextureType.TEXTURE_1D]!!), GlTexture1D {
    companion object {
        @JvmField
        val NULL = NeoTexture1D(0, TextureFormat.NULL)
    }

    constructor(format: TextureFormat) : this(OpenGL.INSTANCE.createTexture(), format)

    init {
        if (defaultParams) {
            setInterpolation(InterpolationType.NEAREST, InterpolationType.NEAREST)
            setWrapping(WrappingType.CLAMP_TO_EDGE)
        }
    }

    override fun free() {
        OpenGL.INSTANCE.deleteTexture(glId)
    }

    override fun type() = TextureType.TEXTURE_1D

    override fun format() = format

    override fun resize(width: Int): BufferUploader {
        return object : BufferUploader {
            override fun upload(buffer: ByteBuffer) {
                bind()
                OpenGL.INSTANCE.textureData1D(type(), format, width, buffer)
                unbind()
            }

            override fun uploadNull() {
                bind()
                OpenGL.INSTANCE.textureData1D(type(), format, width, 0L)
                unbind()
            }
        }
    }

    override fun toString(): String {
        return "${javaClass.simpleName}(glId=$glId, format=$format)"
    }
}