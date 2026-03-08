package net.typho.big_shot_lib.api.client.opengl.state.shards

import net.typho.big_shot_lib.api.client.opengl.buffers.ClearBit
import net.typho.big_shot_lib.api.client.opengl.buffers.GlFramebuffer
import net.typho.big_shot_lib.api.client.opengl.state.GlStateStack
import net.typho.big_shot_lib.api.client.opengl.state.arguments.RenderArguments
import net.typho.big_shot_lib.api.client.opengl.util.GlBindResult
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

open class FramebufferShard(
    @JvmField
    val fbo: (arguments: RenderArguments) -> Pair<GlFramebuffer?, GlBindResult>,
    @JvmField
    vararg val clearBits: ClearBit
) : RenderSettingShard {
    override val type: RenderSettingShard.Type<FramebufferShard> = FramebufferShard

    override fun bind(arguments: RenderArguments, pushStack: Boolean): GlBindResult {
        val result = fbo(arguments)

        if (!result.second.success) {
            return result.second
        }

        val fbo = result.first ?: GlFramebuffer.MAIN
        fbo.bind(pushStack)

        if (clearBits.isNotEmpty()) {
            fbo.clear(*clearBits)
        }

        return result.second
    }

    override fun unbind(popStack: Boolean) {
        if (popStack) {
            GlStateStack.framebuffer.pop()
        } else {
            GlStateStack.framebuffer.bind(0)
        }
    }

    companion object : RenderSettingShard.Type<FramebufferShard> {
        override val default = FramebufferShard({ null to GlBindResult.Success })
        override val codec = null
        override val location = ResourceIdentifier("opengl", "framebuffer")
    }
}