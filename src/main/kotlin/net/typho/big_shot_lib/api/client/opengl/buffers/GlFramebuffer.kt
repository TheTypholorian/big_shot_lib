package net.typho.big_shot_lib.api.client.opengl.buffers

import net.minecraft.client.Minecraft
import net.typho.big_shot_lib.api.client.opengl.state.GlStateTracker
import net.typho.big_shot_lib.api.client.opengl.util.BoundResource
import net.typho.big_shot_lib.api.client.opengl.util.FramebufferStatus
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import net.typho.big_shot_lib.api.errors.IncompleteFramebufferException
import net.typho.big_shot_lib.api.util.KeyedDelegate
import net.typho.big_shot_lib.api.util.WrapperUtil
import org.lwjgl.system.NativeResource

interface GlFramebuffer : NativeResource {
    val width: Int
    val height: Int
    val colorAttachments: KeyedDelegate.ReadOnly<Int, GlFramebufferAttachment?>
    val depthAttachment: GlFramebufferAttachment?

    fun bind(viewport: Boolean, tracker: GlStateTracker = OpenGL.INSTANCE): Bound<*>

    interface Bound<F : GlFramebuffer> : BoundResource {
        val framebuffer: F
        var width: Int
        var height: Int
        val colorAttachments: KeyedDelegate<Int, GlFramebufferAttachment?>
        var depthAttachment: GlFramebufferAttachment?

        fun checkStatus(): FramebufferStatus

        fun checkStatusOrThrow() {
            val status = checkStatus()

            if (status != FramebufferStatus.COMPLETE) {
                throw IncompleteFramebufferException(status)
            }
        }

        fun clear(vararg bits: ClearBit)
    }

    companion object {
        @JvmField
        val MAIN = WrapperUtil.INSTANCE.wrap(Minecraft.getInstance().mainRenderTarget)
    }
}