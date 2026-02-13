package net.typho.big_shot_lib.api.client.rendering.textures

import net.typho.big_shot_lib.api.client.rendering.state.OpenGL
import net.typho.big_shot_lib.api.client.rendering.util.GlResource
import net.typho.big_shot_lib.api.util.buffers.BufferUploader
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import java.nio.ByteBuffer

open class GlTexture(
    glId: Int,
    @JvmField
    val format: TextureFormat,
    defaultParams: Boolean = true
) : GlResource(glId), GlFramebufferAttachment {
    companion object {
        @JvmField
        val NULL = GlTexture(0, TextureFormat.NULL)
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

    override fun bind(glId: Int) = OpenGL.INSTANCE.bindTexture(GL_TEXTURE_2D, glId)

    override fun free() {
        OpenGL.INSTANCE.deleteTexture(glId)
    }

    fun setInterpolation(min: InterpolationType, mag: InterpolationType = min) {
        OpenGL.INSTANCE.textureInterpolation(GL_TEXTURE_2D, min, mag)
    }

    fun setWrapping(s: WrappingType, t: WrappingType = s) {
        OpenGL.INSTANCE.textureWrapping(GL_TEXTURE_2D, s, t)
    }

    override fun getFormat() = format

    override fun attachToFramebuffer(attachment: Int) {
        OpenGL.INSTANCE.attachFramebufferTexture(attachment, GL_TEXTURE_2D, glId)
    }

    override fun resize(width: Int, height: Int): BufferUploader {
        return object : BufferUploader {
            override fun upload(buffer: ByteBuffer) {
                bind()
                OpenGL.INSTANCE.textureData2D(format, width, height, buffer)
                unbind()
            }

            override fun uploadNull() {
                bind()
                OpenGL.INSTANCE.textureData2D(format, width, height, 0L)
                unbind()
            }
        }
    }

    override fun toString(): String {
        return "${javaClass.simpleName}(glId=$glId, format=$format)"
    }
}