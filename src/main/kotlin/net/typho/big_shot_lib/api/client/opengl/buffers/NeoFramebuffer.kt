package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.buffers.ClearBit.Companion.initAndGetClearMask
import net.typho.big_shot_lib.api.client.opengl.state.GlResourceType
import net.typho.big_shot_lib.api.client.opengl.state.GlStateTracker
import net.typho.big_shot_lib.api.client.opengl.state.GlStateType
import net.typho.big_shot_lib.api.client.opengl.util.GlResource
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import net.typho.big_shot_lib.api.math.rect.NeoRect2i
import net.typho.big_shot_lib.api.util.KeyedDelegate
import org.lwjgl.opengl.GL11.GL_NONE
import org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0
import org.lwjgl.opengl.GL30.GL_DEPTH_STENCIL_ATTACHMENT

open class NeoFramebuffer(
    width: Int,
    height: Int,
    glId: Int = GlResourceType.FRAMEBUFFER.create(),
) : GlResource(GlResourceType.FRAMEBUFFER, glId), GlFramebuffer {
    override var width = width
        protected set(value) {
            field = value
            colorAttachmentsBacking.forEachIndexed { index, attachment -> attachment?.attachToFramebuffer(GL_COLOR_ATTACHMENT0 + index, width, height) }
            depthAttachment?.attachToFramebuffer(depthAttachment?.format?.getDepthStencilAttachmentId() ?: GL_DEPTH_STENCIL_ATTACHMENT, width, height)
        }
    override var height = height
        protected set(value) {
            field = value
            colorAttachmentsBacking.forEachIndexed { index, attachment -> attachment?.attachToFramebuffer(GL_COLOR_ATTACHMENT0 + index, width, height) }
            depthAttachment?.attachToFramebuffer(depthAttachment?.format?.getDepthStencilAttachmentId() ?: GL_DEPTH_STENCIL_ATTACHMENT, width, height)
        }
    protected val colorAttachmentsBacking = arrayListOf<GlFramebufferAttachment?>()
    override val colorAttachments: KeyedDelegate.ReadOnly<Int, GlFramebufferAttachment?>
        get() = KeyedDelegate.ReadOnly { index -> if (index >= colorAttachmentsBacking.size) null else colorAttachmentsBacking[index] }
    override var depthAttachment: GlFramebufferAttachment? = null
        protected set

    override fun bind(tracker: GlStateTracker): GlFramebuffer.Bound = Bound(tracker)

    protected open inner class Bound(
        val tracker: GlStateTracker,
        var viewport: Boolean = false
    ) : GlFramebuffer.Bound {
        override var width: Int by this@NeoFramebuffer::width
        override var height: Int by this@NeoFramebuffer::height
        override val colorAttachments: KeyedDelegate<Int, GlFramebufferAttachment?> = this@NeoFramebuffer.colorAttachments.withSet { index, attachment ->
            colorAttachmentsBacking[index] = attachment
            (attachment ?: NeoTexture2D.NULL).attachToFramebuffer(GL_COLOR_ATTACHMENT0 + index, width, height)
            OpenGL.INSTANCE.drawBuffers(
                *colorAttachmentsBacking.mapIndexedNotNull { index, attachment -> if (attachment == null) null else GL_COLOR_ATTACHMENT0 + index }
                    .ifEmpty { listOf(GL_NONE) }
                    .toIntArray()
            )
        }
        override var depthAttachment: GlFramebufferAttachment?
            get() = this@NeoFramebuffer.depthAttachment
            set(attachment) {
                this@NeoFramebuffer.depthAttachment = attachment
                (attachment ?: NeoTexture2D.NULL).attachToFramebuffer(attachment?.format?.getDepthStencilAttachmentId() ?: GL_DEPTH_STENCIL_ATTACHMENT, width, height)
            }

        init {
            GlStateType.FRAMEBUFFER.push(glId, tracker)
        }

        override fun checkStatus() = OpenGL.INSTANCE.checkFramebufferStatus()

        override fun viewport() {
            if (!viewport) {
                viewport = true
                GlStateType.VIEWPORT.push(NeoRect2i(0, 0, width, height), tracker)
            }
        }

        override fun clear(vararg bits: ClearBit) {
            if (bits.isEmpty()) {
                throw IllegalArgumentException("Cleared framebuffer with no bits")
            }

            OpenGL.INSTANCE.clear(bits.initAndGetClearMask())
        }

        override fun unbind() {
            if (viewport) {
                GlStateType.VIEWPORT.pop(tracker)
            }

            GlStateType.FRAMEBUFFER.pop(tracker)
        }
    }
}