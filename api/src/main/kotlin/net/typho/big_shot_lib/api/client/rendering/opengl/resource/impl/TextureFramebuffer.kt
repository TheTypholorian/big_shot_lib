package net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundFramebuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundResource.Companion.assertBound
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebufferAttachment
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.TextureMipmapLevel
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager
import net.typho.big_shot_lib.api.client.rendering.util.RenderingContext
import net.typho.big_shot_lib.api.math.rect.AbstractRect2
import net.typho.big_shot_lib.api.util.KeyedDelegate
import org.lwjgl.opengl.GL11.GL_NONE
import org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0
import org.lwjgl.opengl.GL30.GL_FRAMEBUFFER

open class TextureFramebuffer(glId: Int, autoFree: Boolean, context: RenderingContext = RenderingContext.get()) : NeoGlResource(GlResourceType.FRAMEBUFFER, glId, autoFree, context), GlFramebuffer {
    constructor() : this(GlResourceType.FRAMEBUFFER.create(), true)

    private val colorAttachmentsBacking = Array<TextureMipmapLevel?>(8) { null }

    val colorTextures = KeyedDelegate.ReadOnly<Int, TextureMipmapLevel?> { colorAttachmentsBacking[it] }
    var depthTexture: TextureMipmapLevel? = null
        protected set

    override val colorAttachments: KeyedDelegate.ReadOnly<Int, out GlFramebufferAttachment?> = KeyedDelegate.ReadOnly { colorTextures[it] }
    override val depthAttachment: GlFramebufferAttachment?
        get() = depthTexture

    open inner class Bound(viewport: AbstractRect2<Int>?) : GlBoundFramebuffer.Basic(this, viewport, NeoGlStateManager.CURRENT.framebuffer.push(glId)) {
        override val colorAttachments: KeyedDelegate<Int, GlFramebufferAttachment?> = object : KeyedDelegate<Int, GlFramebufferAttachment?> {
            override fun get(key: Int) = colorAttachmentsBacking[key]

            override fun set(
                key: Int,
                value: GlFramebufferAttachment?
            ) {
                if (value != null && value !is TextureMipmapLevel) throw IllegalArgumentException("Cannot attach a $value to a TextureFramebuffer, attachments must be inheritors of TextureMipmapLevel")
                colorAttachmentsBacking[key] = value

                assertBound {
                    value?.attach(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + key)
                    drawBuffers(
                        *colorAttachmentsBacking.mapIndexedNotNull { index, attachment ->
                            if (attachment == null) null else GL_COLOR_ATTACHMENT0 + index
                        }
                            .ifEmpty { listOf(GL_NONE) }
                            .toIntArray()
                    )
                }
            }
        }
        override var depthAttachment: GlFramebufferAttachment?
            get() = depthTexture
            set(value) {
                if (value != null && value !is TextureMipmapLevel) throw IllegalArgumentException("Cannot attach a $value to a TextureFramebuffer, attachments must be inheritors of TextureMipmapLevel")
                depthTexture = value

                assertBound {
                    value?.let {
                        it.attach(
                            GL_FRAMEBUFFER,
                            (it.format ?: throw NullPointerException("Depth attachment has no texture format"))
                                .getDepthStencilAttachmentId()
                                ?: throw NullPointerException("Depth attachment has invalid format ${it.format}")
                        )
                    }
                }
            }
    }

    override fun bind(viewport: AbstractRect2<Int>?): Bound {
        checkUsable()
        return Bound(viewport)
    }
}