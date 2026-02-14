package net.typho.big_shot_lib.api.client.rendering.textures

import net.typho.big_shot_lib.api.client.rendering.state.OpenGL
import net.typho.big_shot_lib.api.client.rendering.util.GlResource
import net.typho.big_shot_lib.api.util.buffers.BufferUploader
import java.nio.ByteBuffer

open class NeoTexture2D(
    glId: Int,
    @JvmField
    val format: TextureFormat,
    defaultParams: Boolean = true
) : GlResource(glId), GlTexture2D {
    companion object {
        @JvmField
        val NULL = NeoTexture2D(0, TextureFormat.NULL)
    }

    constructor(format: TextureFormat) : this(OpenGL.INSTANCE.createTexture(), format)

    init {
        if (defaultParams) {
            bind()
            setInterpolation(InterpolationType.NEAREST, InterpolationType.NEAREST)
            setWrapping(WrappingType.CLAMP_TO_EDGE, WrappingType.CLAMP_TO_EDGE)
            unbind()
        }
    }

    override fun bind(glId: Int) = OpenGL.INSTANCE.bindTexture(type().glId, glId)

    override fun free() {
        OpenGL.INSTANCE.deleteTexture(glId)
    }

    override fun type() = TextureType.TWO_D

    override fun format() = format

    override fun attachToFramebuffer(attachment: Int) {
        bind()
        OpenGL.INSTANCE.attachFramebufferTexture2D(attachment, type().glId, glId)
        unbind()
    }

    override fun resize(width: Int, height: Int): BufferUploader {
        return object : BufferUploader {
            override fun upload(buffer: ByteBuffer) {
                bind()
                OpenGL.INSTANCE.textureData2D(type().glId, format, width, height, buffer)
                unbind()
            }

            override fun uploadNull() {
                bind()
                OpenGL.INSTANCE.textureData2D(type().glId, format, width, height, 0L)
                unbind()
            }
        }
    }

    override fun toString(): String {
        return "${javaClass.simpleName}(glId=$glId, format=$format)"
    }
}