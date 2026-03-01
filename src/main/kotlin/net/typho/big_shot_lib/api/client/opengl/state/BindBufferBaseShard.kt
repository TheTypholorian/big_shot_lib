package net.typho.big_shot_lib.api.client.opengl.state

import net.typho.big_shot_lib.api.client.opengl.buffers.GlBuffer
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import java.util.function.Supplier

open class BindBufferBaseShard(
    @JvmField
    val buffer: Supplier<GlBuffer?>,
    @JvmField
    val index: Int
) : RenderSettingShard {
    override val type = BindBufferBaseShard

    override fun bind(pushStack: Boolean) {
        buffer.get()?.bindBase(index)
    }

    override fun unbind(popStack: Boolean) {
    }

    companion object : RenderSettingShard.Type<BindBufferBaseShard> {
        override val default = BindBufferBaseShard({ null }, 0)
        override val codec = null
        override val location = ResourceIdentifier("opengl", "bind_buffer_base")
    }
}