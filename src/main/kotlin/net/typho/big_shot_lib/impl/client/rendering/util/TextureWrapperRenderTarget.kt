package net.typho.big_shot_lib.impl.client.rendering.util

import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlTexture2D
import org.lwjgl.opengl.GL30.GL_FRAMEBUFFER

open class TextureWrapperRenderTarget(
    override val colorTexture: GlTexture2D,
    override val depthTexture: GlTexture2D?
) : RenderTarget(depthTexture != null) {
    init {
        val width = colorTexture.width
        val height = colorTexture.height

        this.width = width
        this.height = height
        viewWidth = width
        viewHeight = height

        createBuffers(width, height)
    }

    override fun _resize(width: Int, height: Int, clearError: Boolean) {
        throw UnsupportedOperationException("Cannot resize a texture wrapper render target, you must create a new one")
    }

    override fun destroyBuffers() {
        RenderSystem.assertOnRenderThreadOrInit()
        unbindRead()
        unbindWrite()

        if (frameBufferId > -1) {
            GlStateManager._glBindFramebuffer(GL_FRAMEBUFFER, 0)
            GlStateManager._glDeleteFramebuffers(frameBufferId)
            frameBufferId = -1
        }
    }

    override fun createBuffers(width: Int, height: Int, clearError: Boolean) {
        RenderSystem.assertOnRenderThreadOrInit()

        if (this.width != width || this.height != height) {
            throw UnsupportedOperationException("Cannot resize a texture wrapper render target, you must create a new one")
        }

        frameBufferId = GlStateManager.glGenFramebuffers()

        GlStateManager._glFramebufferTexture2D(36160, 36064, 3553, colorTexture.glId, 0)
        depthTexture?.let { GlStateManager._glFramebufferTexture2D(36160, 36096, 3553, it.glId, 0) }

        checkStatus()
        clear(clearError)
        unbindRead()
    }
}