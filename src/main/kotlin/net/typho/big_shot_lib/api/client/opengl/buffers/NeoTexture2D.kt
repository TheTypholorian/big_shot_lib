package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.state.GlStateStack
import net.typho.big_shot_lib.api.client.opengl.util.*
import net.typho.big_shot_lib.api.util.buffers.BufferUploader
import java.nio.ByteBuffer

open class NeoTexture2D(
    glId: Int,
    override val format: TextureFormat,
    defaultParams: Boolean = true,
    override val type: GlTextureResourceType = GlTextureResourceType.TEXTURE_2D
) : GlResource(glId, GlStateStack.textures[type]!!), GlTexture2D {
    companion object {
        @JvmField
        val NULL = NeoTexture2D(0, TextureFormat.NULL, false)
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
        OpenGL.INSTANCE.attachFramebufferTexture2D(attachment, type, glId)
    }

    override fun resize(width: Int, height: Int, upload: (uploader: BufferUploader) -> Unit) {
        if (type.multisample) {
            bind()
            OpenGL.INSTANCE.textureData2DMultisample(type, 4, format, width, height)
            unbind()
        } else {
            upload(object : BufferUploader {
                override fun upload(buffer: ByteBuffer) {
                    bind()
                    OpenGL.INSTANCE.textureData2D(type, format, width, height, buffer)
                    unbind()
                }

                override fun uploadNull() {
                    bind()
                    OpenGL.INSTANCE.textureData2D(type, format, width, height, 0L)
                    unbind()
                }
            })
        }
    }

    override fun toString(): String {
        return "${javaClass.simpleName}(glId=$glId, format=$format)"
    }
}