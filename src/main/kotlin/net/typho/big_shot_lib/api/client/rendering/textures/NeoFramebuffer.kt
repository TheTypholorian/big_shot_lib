package net.typho.big_shot_lib.api.client.rendering.textures

import net.typho.big_shot_lib.api.client.rendering.errors.IllegalTextureFormatException
import net.typho.big_shot_lib.api.client.rendering.errors.IncompleteFramebufferException
import net.typho.big_shot_lib.api.client.rendering.state.OpenGL
import net.typho.big_shot_lib.api.client.rendering.textures.ClearBit.Companion.initAndGetClearMask
import net.typho.big_shot_lib.api.client.rendering.util.GlResource
import org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0
import org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE

open class NeoFramebuffer(
    glId: Int,
    @JvmField
    val colorAttachments: Array<GlFramebufferAttachment>,
    @JvmField
    val depthAttachment: GlFramebufferAttachment?,
    @JvmField
    protected var width: Int,
    @JvmField
    protected var height: Int
) : GlResource(glId), GlFramebuffer {
    constructor(
        colorAttachments: Array<GlFramebufferAttachment>,
        depthAttachment: GlFramebufferAttachment?,
        width: Int,
        height: Int
    ) : this(OpenGL.INSTANCE.createFramebuffer(), colorAttachments, depthAttachment, width, height)

    init {
        bind()

        colorAttachments.forEachIndexed { index, attachment ->
            attachment.resize(width, height)?.uploadNull()
            attachment.attachToFramebuffer(GL_COLOR_ATTACHMENT0 + index)
        }
        depthAttachment?.let { attachment ->
            attachment.resize(width, height)?.uploadNull()
            attachment.attachToFramebuffer(
                attachment.getFormat().getDepthStencilAttachmentId()
                    ?: throw IllegalTextureFormatException("${attachment.getFormat()} is neither a depth nor stencil format")
            )
        }

        val status = OpenGL.INSTANCE.checkFramebufferStatus()

        if (status != GL_FRAMEBUFFER_COMPLETE) {
            throw IncompleteFramebufferException("0x${status.toString(16)}")
        }

        unbind()
    }

    override fun resize(width: Int, height: Int) {
        bind()

        colorAttachments.forEach { it.resize(width, height)?.uploadNull() }
        depthAttachment?.resize(width, height)?.uploadNull()

        unbind()

        this.width = width
        this.height = height
    }

    override fun clear(vararg bits: ClearBit) {
        if (bits.isEmpty()) {
            throw IllegalArgumentException()
        }

        OpenGL.INSTANCE.clear(bits.initAndGetClearMask())
    }

    override fun viewport() {
        OpenGL.INSTANCE.viewport(0, 0, width(), height())
    }

    override fun width() = width

    override fun height() = height

    override fun bind(glId: Int) = OpenGL.INSTANCE.bindFramebuffer(glId)

    override fun free() {
        OpenGL.INSTANCE.deleteFramebuffer(glId)
    }
}