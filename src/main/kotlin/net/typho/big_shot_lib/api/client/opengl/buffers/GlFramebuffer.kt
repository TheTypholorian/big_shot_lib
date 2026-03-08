package net.typho.big_shot_lib.api.client.opengl.buffers

import net.minecraft.client.Minecraft
import net.typho.big_shot_lib.api.client.opengl.util.GlBindable
import net.typho.big_shot_lib.api.util.WrapperUtil
import org.lwjgl.system.NativeResource
import java.awt.Dimension

interface GlFramebuffer : GlBindable, NativeResource {
    val colorAttachments: List<GlFramebufferAttachment>
    val depthAttachment: GlFramebufferAttachment?
    val width: Int
    val height: Int
    val dimensions: Dimension
        get() = Dimension(width, height)

    fun resize(width: Int, height: Int)

    fun clear(vararg bits: ClearBit)

    companion object {
        @JvmField
        val MAIN = WrapperUtil.INSTANCE.wrap(Minecraft.getInstance().mainRenderTarget)
    }
}