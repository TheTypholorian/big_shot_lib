package net.typho.big_shot_lib.api.client.rendering.textures

import net.typho.big_shot_lib.api.client.rendering.errors.IllegalTextureFormatException
import net.typho.big_shot_lib.api.client.rendering.errors.IncompleteFramebufferException
import net.typho.big_shot_lib.api.client.rendering.state.OpenGL
import net.typho.big_shot_lib.api.client.rendering.util.GlResource
import org.lwjgl.opengl.GL30.*

open class GlFramebuffer(
    glId: Int,
    @JvmField
    val colorAttachments: Array<GlFramebufferAttachment>,
    @JvmField
    val depthAttachment: GlFramebufferAttachment?,
    @JvmField
    protected var width: Int,
    @JvmField
    protected var height: Int
) : GlResource(glId) {
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

            val depth = attachment.getFormat().hasDepth
            val stencil = attachment.getFormat().hasStencil

            when {
                depth && stencil -> attachment.attachToFramebuffer(GL_DEPTH_STENCIL_ATTACHMENT)
                depth && !stencil -> attachment.attachToFramebuffer(GL_DEPTH_ATTACHMENT)
                !depth && stencil -> attachment.attachToFramebuffer(GL_STENCIL_ATTACHMENT)
                else -> throw IllegalTextureFormatException("${attachment.getFormat()} is neither a depth nor stencil format")
            }
        }

        val status = OpenGL.INSTANCE.checkFramebufferStatus()

        if (status != GL_FRAMEBUFFER_COMPLETE) {
            throw IncompleteFramebufferException("0x${status.toString(16)}")
        }

        unbind()
    }

    fun resize(width: Int, height: Int) {
        bind()

        colorAttachments.forEach { it.resize(width, height)?.uploadNull() }
        depthAttachment?.resize(width, height)?.uploadNull()

        unbind()

        this.width = width
        this.height = height
    }

    fun clear(vararg bits: ClearBit) {
        if (bits.isEmpty()) {
            throw IllegalArgumentException()
        }

        var mask = 0

        for (bit in bits) {
            bit.run()
            mask = mask or bit.mask()
        }

        OpenGL.INSTANCE.clear(mask)
    }

    fun width() = width

    fun height() = height

    override fun bind(glId: Int) = OpenGL.INSTANCE.bindFramebuffer(glId)

    override fun free() {
        OpenGL.INSTANCE.deleteFramebuffer(glId)
    }
}