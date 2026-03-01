package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.state.GlStateStack
import net.typho.big_shot_lib.api.client.opengl.util.*
import net.typho.big_shot_lib.api.util.buffers.BufferUploader
import java.nio.ByteBuffer

open class NeoTexture3D(
    glId: Int,
    override val format: TextureFormat,
    defaultParams: Boolean = true,
    override val type: TextureType = TextureType.TEXTURE_3D
) : GlResource(glId, GlStateStack.textures[type]!!), GlTexture3D {
    companion object {
        @JvmField
        val NULL = NeoTexture3D(0, TextureFormat.NULL)
    }

    constructor(format: TextureFormat) : this(OpenGL.INSTANCE.createTexture(), format)

    init {
        if (defaultParams) {
            setInterpolation(InterpolationType.NEAREST, InterpolationType.NEAREST)
            setWrapping(WrappingType.CLAMP_TO_EDGE, WrappingType.CLAMP_TO_EDGE, WrappingType.CLAMP_TO_EDGE)
        }
    }

    override fun free() {
        OpenGL.INSTANCE.deleteTexture(glId)
    }

    override fun resize(width: Int, height: Int, depth: Int): BufferUploader {
        return object : BufferUploader {
            override fun upload(buffer: ByteBuffer) {
                bind()
                OpenGL.INSTANCE.textureData3D(type, format, width, height, depth, buffer)
                unbind()
            }

            override fun uploadNull() {
                bind()
                OpenGL.INSTANCE.textureData3D(type, format, width, height, depth, 0L)
                unbind()
            }
        }
    }

    override fun toString(): String {
        return "${javaClass.simpleName}(glId=$glId, format=$format)"
    }
}