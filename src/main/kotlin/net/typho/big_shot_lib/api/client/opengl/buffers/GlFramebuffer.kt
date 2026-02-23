package net.typho.big_shot_lib.api.client.opengl.buffers

import net.minecraft.client.Minecraft
import net.typho.big_shot_lib.api.client.opengl.util.GlBindable
import net.typho.big_shot_lib.api.util.WrapperUtil
import org.lwjgl.system.NativeResource

interface GlFramebuffer : GlBindable, NativeResource {
    val colorAttachments: List<GlFramebufferAttachment>
    val depthAttachment: GlFramebufferAttachment?

    fun resize(width: Int, height: Int)

    fun clear(vararg bits: ClearBit)

    fun viewport()

    fun width(): Int

    fun height(): Int

    companion object {
        @JvmField
        val MAIN = WrapperUtil.INSTANCE.wrap(Minecraft.getInstance().mainRenderTarget)
    }
}