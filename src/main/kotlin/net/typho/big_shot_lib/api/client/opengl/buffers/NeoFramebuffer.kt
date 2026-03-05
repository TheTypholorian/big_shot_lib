package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.buffers.ClearBit.Companion.initAndGetClearMask
import net.typho.big_shot_lib.api.client.opengl.state.GlStateStack
import net.typho.big_shot_lib.api.client.opengl.util.FramebufferStatus
import net.typho.big_shot_lib.api.client.opengl.util.GlResource
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import net.typho.big_shot_lib.api.errors.IllegalTextureFormatException
import net.typho.big_shot_lib.api.errors.IncompleteFramebufferException
import org.lwjgl.opengl.GL11.GL_NONE
import org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0

open class NeoFramebuffer(
    glId: Int,
    colorAttachments: List<GlFramebufferAttachment>,
    depthAttachment: GlFramebufferAttachment?,
    width: Int,
    height: Int
) : GlResource(glId, GlStateStack.framebuffer), GlFramebuffer {
    override var colorAttachments: List<GlFramebufferAttachment> = colorAttachments
        set(value) {
            field = value

            bind()

            attachColor()
            checkStatus()

            unbind()
        }
    override var depthAttachment: GlFramebufferAttachment? = depthAttachment
        set(value) {
            field = value

            bind()

            attachDepth()
            checkStatus()

            unbind()
        }
    final override var width: Int = width
        private set
    final override var height: Int = height
        private set

    constructor(
        colorAttachments: List<GlFramebufferAttachment>,
        depthAttachment: GlFramebufferAttachment?,
        width: Int,
        height: Int
    ) : this(OpenGL.INSTANCE.createFramebuffer(), colorAttachments, depthAttachment, width, height)

    init {
        bind()

        attachColor()
        attachDepth()
        checkStatus()

        unbind()
    }

    protected fun attachColor() {
        bind()

        colorAttachments.forEachIndexed { index, attachment ->
            attachment.resize(width, height)
            attachment.attachToFramebuffer(GL_COLOR_ATTACHMENT0 + index)
        }
        OpenGL.INSTANCE.drawBuffers(
            *List(colorAttachments.size) { index -> GL_COLOR_ATTACHMENT0 + index }
                .ifEmpty { listOf(GL_NONE) }
                .toIntArray()
        )

        unbind()
    }

    protected fun attachDepth() {
        bind()

        depthAttachment?.let { attachment ->
            attachment.resize(width, height)
            attachment.attachToFramebuffer(
                attachment.format.getDepthStencilAttachmentId()
                    ?: throw IllegalTextureFormatException("${attachment.format} is neither a depth nor stencil format")
            )
        }

        unbind()
    }

    protected fun checkStatus() {
        bind()

        val status = OpenGL.INSTANCE.checkFramebufferStatus()

        if (status != FramebufferStatus.COMPLETE) {
            throw IncompleteFramebufferException(status)
        }

        unbind()
    }

    override fun resize(width: Int, height: Int) {
        bind()

        colorAttachments.forEach { it.resize(width, height) }
        depthAttachment?.resize(width, height)

        unbind()

        this.width = width
        this.height = height
    }

    override fun clear(vararg bits: ClearBit) {
        bind()

        if (bits.isEmpty()) {
            throw IllegalArgumentException()
        }

        OpenGL.INSTANCE.clear(bits.initAndGetClearMask())

        unbind()
    }

    override fun viewport() {
        bind()

        OpenGL.INSTANCE.viewport(0, 0, width, height)

        unbind()
    }

    override fun free() {
        OpenGL.INSTANCE.deleteFramebuffer(glId)
    }
}