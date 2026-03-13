package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.state.GlResourceType
import net.typho.big_shot_lib.api.client.opengl.util.GlResource
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import net.typho.big_shot_lib.api.client.opengl.util.TextureFormat
import net.typho.big_shot_lib.api.util.buffers.BufferUploader

open class GlRenderBuffer(
    override val format: TextureFormat,
    glId: Int = GlResourceType.RenderBuffer.create()
) : GlResource<GlResourceType.RenderBuffer>(GlResourceType.RenderBuffer, glId), GlFramebufferAttachment {
    companion object {
        @JvmField
        val NULL = GlRenderBuffer(TextureFormat.NULL, 0)
    }

    override fun attachToFramebuffer(attachment: Int) {
        OpenGL.INSTANCE.attachFramebufferRenderBuffer(attachment, glId)
    }

    override fun resize(width: Int, height: Int, upload: (uploader: BufferUploader) -> Unit) {
        bind()
        OpenGL.INSTANCE.resizeRenderBuffer(format, width, height)
        unbind()
    }

    override fun toString(): String {
        return "${type.name}(glId=$glId, format=$format)"
    }
}