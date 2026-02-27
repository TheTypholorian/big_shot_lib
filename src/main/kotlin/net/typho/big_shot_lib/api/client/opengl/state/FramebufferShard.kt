package net.typho.big_shot_lib.api.client.opengl.state

import net.typho.big_shot_lib.api.client.opengl.buffers.ClearBit
import net.typho.big_shot_lib.api.client.opengl.buffers.GlFramebuffer
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import java.util.function.Supplier

open class FramebufferShard(
    @JvmField
    val fbo: Supplier<GlFramebuffer?>,
    @JvmField
    val viewport: Boolean,
    @JvmField
    vararg val clearBits: ClearBit
) : RenderSettingShard {
    override val type = FramebufferShard

    override fun bind(pushStack: Boolean) {
        val fbo = fbo.get() ?: GlFramebuffer.MAIN
        fbo.bind(pushStack)

        if (viewport) {
            fbo.viewport()
        }

        if (clearBits.isNotEmpty()) {
            fbo.clear(*clearBits)
        }
    }

    override fun unbind(popStack: Boolean) {
        val fbo = fbo.get() ?: GlFramebuffer.MAIN
        fbo.unbind(popStack)
    }

    companion object : RenderSettingShard.Type<FramebufferShard> {
        override val default = FramebufferShard({ null }, false)
        override val codec = null
        override val location = ResourceIdentifier("opengl", "shader")
    }
}