package net.typho.big_shot_lib.api.client.rendering.textures

import net.minecraft.client.Minecraft
import net.typho.big_shot_lib.api.client.rendering.util.GlBindable
import net.typho.big_shot_lib.api.services.WrapperUtil
import net.typho.big_shot_lib.api.util.WidthAndHeight
import org.lwjgl.system.NativeResource

interface GlFramebuffer : GlBindable, NativeResource, WidthAndHeight {
    val colorAttachments: List<GlFramebufferAttachment>
    val depthAttachment: GlFramebufferAttachment?

    fun resize(width: Int, height: Int)

    fun clear(vararg bits: ClearBit)

    fun viewport()

    companion object {
        @JvmField
        val MAIN = WrapperUtil.INSTANCE.wrap(Minecraft.getInstance().mainRenderTarget)
    }
}