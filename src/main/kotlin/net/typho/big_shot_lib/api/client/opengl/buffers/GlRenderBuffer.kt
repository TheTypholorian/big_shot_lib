package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.state.GlResourceType
import net.typho.big_shot_lib.api.client.opengl.state.GlStateTracker
import net.typho.big_shot_lib.api.client.opengl.state.GlStateType
import net.typho.big_shot_lib.api.client.opengl.util.BoundResource
import net.typho.big_shot_lib.api.client.opengl.util.GlResource
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import net.typho.big_shot_lib.api.client.opengl.util.TextureFormat
import net.typho.big_shot_lib.api.util.buffers.BufferUploader

open class GlRenderBuffer(
    override val format: TextureFormat,
    glId: Int = GlResourceType.RENDER_BUFFER.create()
) : GlResource(GlResourceType.RENDER_BUFFER, glId), GlFramebufferAttachment {
    companion object {
        @JvmField
        val NULL = GlRenderBuffer(TextureFormat.NULL, 0)
    }

    override fun attachToFramebuffer(attachment: Int) {
        OpenGL.INSTANCE.attachFramebufferRenderBuffer(attachment, glId)
    }

    override fun bind(tracker: GlStateTracker): GlFramebufferAttachment.Bound<*> {
        GlStateType.RENDER_BUFFER.push(glId, tracker)

        return object : Bound {
            override val renderBuffer = this@GlRenderBuffer

            override fun unbind() {
                GlStateType.RENDER_BUFFER.pop(tracker)
            }

            override fun resize(
                width: Int,
                height: Int,
                upload: (uploader: BufferUploader) -> Unit
            ) {
                OpenGL.INSTANCE.resizeRenderBuffer(format, width, height)
            }
        }
    }

    override fun toString(): String {
        return "${resourceType.name}(glId=$glId, format=$format)"
    }

    interface Bound : BoundResource, GlFramebufferAttachment.Bound<GlRenderBuffer> {
        val renderBuffer: GlRenderBuffer
    }
}