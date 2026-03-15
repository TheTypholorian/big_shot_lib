package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.state.GlResourceType
import net.typho.big_shot_lib.api.client.opengl.state.GlStateTracker
import net.typho.big_shot_lib.api.client.opengl.state.GlStateType
import net.typho.big_shot_lib.api.client.opengl.util.GlResource
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import net.typho.big_shot_lib.api.client.opengl.util.TextureFormat

open class GlRenderBuffer(
    override val format: TextureFormat,
    glId: Int = GlResourceType.RENDER_BUFFER.create()
) : GlResource(GlResourceType.RENDER_BUFFER, glId), GlFramebufferAttachment {
    companion object {
        @JvmField
        val NULL = GlRenderBuffer(TextureFormat.NULL, 0)
    }

    override fun attachToFramebuffer(attachment: Int, width: Int, height: Int, tracker: GlStateTracker) {
        GlStateType.RENDER_BUFFER.push(glId, tracker)

        OpenGL.INSTANCE.attachFramebufferRenderBuffer(attachment, glId)
        OpenGL.INSTANCE.resizeRenderBuffer(format, width, height)

        GlStateType.RENDER_BUFFER.pop(tracker)
    }

    override fun toString(): String {
        return "${resourceType.name}(glId=$glId, format=$format)"
    }
}