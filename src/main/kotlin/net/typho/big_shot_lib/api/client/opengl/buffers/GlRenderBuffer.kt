package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.state.GlStateStack
import net.typho.big_shot_lib.api.client.opengl.util.GlResource
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import net.typho.big_shot_lib.api.client.opengl.util.TextureFormat
import net.typho.big_shot_lib.api.util.buffers.BufferUploader
import java.util.function.Consumer

open class GlRenderBuffer(
    glId: Int,
    override val format: TextureFormat
) : GlResource(glId, GlStateStack.renderBuffer), GlFramebufferAttachment {
    companion object {
        @JvmField
        val NULL = GlRenderBuffer(0, TextureFormat.NULL)
    }

    constructor(format: TextureFormat) : this(OpenGL.INSTANCE.createRenderBuffer(), format)

    override fun free() {
        OpenGL.INSTANCE.deleteRenderBuffer(glId)
    }

    override fun attachToFramebuffer(attachment: Int) {
        OpenGL.INSTANCE.attachFramebufferRenderBuffer(attachment, glId)
    }

    override fun resize(width: Int, height: Int, upload: Consumer<BufferUploader>) {
        bind()
        OpenGL.INSTANCE.resizeRenderBuffer(format, width, height)
        unbind()
    }

    override fun toString(): String {
        return "${javaClass.simpleName}(glId=$glId, format=$format)"
    }
}