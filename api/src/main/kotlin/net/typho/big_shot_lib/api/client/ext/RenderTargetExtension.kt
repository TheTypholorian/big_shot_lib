package net.typho.big_shot_lib.api.client.ext

import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.vertex.VertexConsumer
import net.typho.big_shot_lib.api.client.InternalClientUtil
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlClearBit
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlResource
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlResourceType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlTexture2D

interface RenderTargetExtension : GlResource {
    @Suppress("NOTHING_TO_INLINE")
    private inline fun self() = this as RenderTarget

    override val type: GlResourceType
        get() = GlResourceType.FRAMEBUFFER
    override val freed: Boolean
        get() = InternalClientUtil.INSTANCE.rawIsRenderTargetFreed(self())
    override val glId: Int
        get() = InternalClientUtil.INSTANCE.rawGetRenderTargetId(self())

    val colorTexture: GlTexture2D?
        get() = InternalClientUtil.INSTANCE.rawGetRenderTargetColor(self())
    val depthTexture: GlTexture2D?
        get() = InternalClientUtil.INSTANCE.rawGetRenderTargetDepth(self())

    fun resize(width: Int, height: Int) {
        InternalClientUtil.INSTANCE.rawResizeRenderTarget(self(), width, height)
    }

    fun createBuffers(width: Int, height: Int) {
        InternalClientUtil.INSTANCE.rawCreateBuffersRenderTarget(self(), width, height)
    }

    override fun close() {
        InternalClientUtil.INSTANCE.rawFreeRenderTarget(self())
    }

    companion object {
        @JvmStatic
        @JvmOverloads
        @JvmName("create")
        operator fun invoke(width: Int, height: Int, depth: Boolean = true, name: () -> String): RenderTarget = InternalClientUtil.INSTANCE.createRenderTarget(
            width,
            height,
            depth,
            name
        )

        @JvmStatic
        @JvmName("wrap")
        fun wrap(color: GlTexture2D, depth: GlTexture2D?, name: () -> String): RenderTarget = InternalClientUtil.INSTANCE.createRenderTarget(
            color,
            depth,
            name
        )
    }
}