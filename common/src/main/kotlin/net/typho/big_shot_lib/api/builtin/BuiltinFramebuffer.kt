package net.typho.big_shot_lib.api.builtin

import com.mojang.blaze3d.pipeline.RenderTarget
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.api.IFramebuffer
import net.typho.big_shot_lib.gl.resource.GlResourceType
import net.typho.big_shot_lib.gl.resource.TextureFormat
import org.joml.Vector4f

class BuiltinFramebuffer(val inner: RenderTarget) : IFramebuffer {
    override fun colorFormat() = TextureFormat.RGBA

    override fun depthFormat(): TextureFormat? = if (inner.useDepth) TextureFormat.DEPTH_COMPONENT else null

    override fun width() = inner.width

    override fun height() = inner.height

    override fun resize(width: Int, height: Int) {
        inner.resize(width, height)
    }

    override fun clearColor(color: Vector4f) {
        inner.setClearColor(color.x, color.y, color.z, color.w)
        inner.clear()
    }

    override fun unbind() {
        Minecraft.getInstance().mainRenderTarget.bindWrite(true)
    }

    override fun release() {
        inner.destroyBuffers()
    }

    override fun location(): ResourceLocation? = null

    override fun type() = GlResourceType.FRAMEBUFFER

    override fun id() = inner.frameBufferId
}