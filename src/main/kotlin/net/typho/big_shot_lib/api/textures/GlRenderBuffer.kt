package net.typho.big_shot_lib.api.textures

import net.typho.big_shot_lib.api.state.OpenGL
import net.typho.big_shot_lib.api.util.BufferUploader
import net.typho.big_shot_lib.api.util.GlResource

open class GlRenderBuffer(
    glId: Int,
    @JvmField
    val format: TextureFormat
) : GlResource(glId), GlFramebufferAttachment {
    companion object {
        @JvmField
        val NULL = GlRenderBuffer(0, TextureFormat.NULL)
    }

    constructor(format: TextureFormat) : this(OpenGL.INSTANCE.createRenderBuffer(), format)

    override fun bind(glId: Int) {
        OpenGL.INSTANCE.bindRenderBuffer(glId)
    }

    override fun free() {
        OpenGL.INSTANCE.deleteRenderBuffer(glId)
    }

    override fun getFormat() = format

    override fun attachToFramebuffer(attachment: Int) {
        OpenGL.INSTANCE.attachFramebufferRenderBuffer(attachment, glId)
    }

    override fun resize(width: Int, height: Int): BufferUploader? {
        bind()
        OpenGL.INSTANCE.resizeRenderBuffer(format, width, height)
        unbind()
        return null
    }

    override fun toString(): String {
        return "${javaClass.simpleName}(glId=$glId, format=$format)"
    }
}