package net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundFramebuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundResource.Companion.assertBound
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebufferAttachment
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager
import net.typho.big_shot_lib.api.math.rect.AbstractRect2
import net.typho.big_shot_lib.api.util.KeyedDelegate
import org.lwjgl.opengl.GL11.GL_NONE
import org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0
import org.lwjgl.opengl.GL30.GL_FRAMEBUFFER

open class NeoGlFramebuffer(glId: Int, autoFree: Boolean) : NeoGlResource(GlResourceType.FRAMEBUFFER, glId, autoFree), GlFramebuffer {
    constructor() : this(GlResourceType.FRAMEBUFFER.create(), true)

    private val colorAttachmentsBacking = Array<GlFramebufferAttachment?>(8) { null }
    override val colorAttachments: KeyedDelegate.ReadOnly<Int, GlFramebufferAttachment?> = KeyedDelegate.ReadOnly { key -> colorAttachmentsBacking[key] }
    override var depthAttachment: GlFramebufferAttachment? = null
        protected set

    override fun bind(viewport: AbstractRect2<Int>?): GlBoundFramebuffer {
        checkUsable()
        return object : GlBoundFramebuffer.Basic(this, viewport, NeoGlStateManager.CURRENT.framebuffer.push(glId)) {
            override val colorAttachments: KeyedDelegate<Int, GlFramebufferAttachment?> = object : KeyedDelegate<Int, GlFramebufferAttachment?> {
                override fun get(key: Int) = colorAttachmentsBacking[key]

                override fun set(
                    key: Int,
                    value: GlFramebufferAttachment?
                ) {
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
                get() = this@NeoGlFramebuffer.depthAttachment
                set(value) {
                    this@NeoGlFramebuffer.depthAttachment = value

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
    }
}