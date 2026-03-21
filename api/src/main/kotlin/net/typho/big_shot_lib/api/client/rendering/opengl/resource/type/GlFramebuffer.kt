package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import net.minecraft.client.Minecraft
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundFramebuffer
import net.typho.big_shot_lib.api.math.rect.AbstractRect2
import net.typho.big_shot_lib.api.util.KeyedDelegate
import net.typho.big_shot_lib.api.util.WrapperUtil

interface GlFramebuffer : GlResource {
    companion object {
        @JvmField
        val MAIN = WrapperUtil.INSTANCE.wrap(Minecraft.getInstance().mainRenderTarget)
    }

    val colorAttachments: KeyedDelegate.ReadOnly<Int, GlFramebufferAttachment?>
    val depthAttachment: GlFramebufferAttachment?

    fun bind(viewport: AbstractRect2<Int>? = null): GlBoundFramebuffer
}