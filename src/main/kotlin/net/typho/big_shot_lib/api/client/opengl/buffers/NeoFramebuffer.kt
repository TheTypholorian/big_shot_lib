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
            colorAttachmentsBacking.forEach { it?.bind(OpenGL.INSTANCE)?.use { bound -> bound.resize(value, height) } }
            depthAttachment?.bind(OpenGL.INSTANCE)?.use { it.resize(value, height) }
        }
    override var height = height
        protected set(value) {
            field = value
            colorAttachmentsBacking.forEach { it?.bind(OpenGL.INSTANCE)?.use { bound -> bound.resize(width, value) } }
            depthAttachment?.bind(OpenGL.INSTANCE)?.use { it.resize(width, value) }
        }
    protected val colorAttachmentsBacking = arrayListOf<GlFramebufferAttachment?>()
    override val colorAttachments: KeyedDelegate.ReadOnly<Int, GlFramebufferAttachment?>
        get() = KeyedDelegate.ReadOnly { index -> if (index >= colorAttachmentsBacking.size) null else colorAttachmentsBacking[index] }
    override var depthAttachment: GlFramebufferAttachment? = null
        protected set

    override fun bind(viewport: Boolean, tracker: GlStateTracker): GlFramebuffer.Bound<NeoFramebuffer> = Bound(this, viewport, tracker)

    protected open class Bound<F : NeoFramebuffer>(
        override val framebuffer: F,
        val viewport: Boolean,
        val tracker: GlStateTracker
    ) : GlFramebuffer.Bound<F> {
        override var width: Int
            get() = framebuffer.width
            set(value) {
                framebuffer.width = value
            }
        override var height: Int
            get() = framebuffer.height
            set(value) {
                framebuffer.height = value
            }
        override val colorAttachments: KeyedDelegate<Int, GlFramebufferAttachment?> = framebuffer.colorAttachments.withSet { index, attachment ->
            framebuffer.colorAttachmentsBacking[index] = attachment
            attachment?.bind(OpenGL.INSTANCE)?.use { it.resize(width, height) }
            (attachment ?: NeoTexture2D.NULL).attachToFramebuffer(GL_COLOR_ATTACHMENT0 + index)
            OpenGL.INSTANCE.drawBuffers(
                *framebuffer.colorAttachmentsBacking.mapIndexedNotNull { index, attachment -> if (attachment == null) null else GL_COLOR_ATTACHMENT0 + index }
                    .ifEmpty { listOf(GL_NONE) }
                    .toIntArray()
            )
        }
        override var depthAttachment: GlFramebufferAttachment?
            get() = framebuffer.depthAttachment
            set(attachment) {
                framebuffer.depthAttachment = attachment
                attachment?.bind(OpenGL.INSTANCE)?.use { it.resize(width, height) }
                (attachment ?: NeoTexture2D.NULL).attachToFramebuffer(attachment?.format?.getDepthStencilAttachmentId() ?: GL_DEPTH_STENCIL_ATTACHMENT)
            }

        init {
            GlStateType.FRAMEBUFFER.push(framebuffer.glId, tracker)

            if (viewport) {
                GlStateType.VIEWPORT.push(NeoRect2i(0, 0, width, height), tracker)
            }
        }

        override fun checkStatus() = OpenGL.INSTANCE.checkFramebufferStatus()

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