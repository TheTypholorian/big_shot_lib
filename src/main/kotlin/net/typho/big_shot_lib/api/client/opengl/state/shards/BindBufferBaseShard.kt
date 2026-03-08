package net.typho.big_shot_lib.api.client.opengl.state.shards

import net.typho.big_shot_lib.api.client.opengl.buffers.GlBuffer
import net.typho.big_shot_lib.api.client.opengl.state.arguments.RenderArguments
import net.typho.big_shot_lib.api.client.opengl.util.GlBindResult
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

open class BindBufferBaseShard(
    @JvmField
    val buffer: (arguments: RenderArguments) -> Pair<GlBuffer?, GlBindResult>,
    @JvmField
    val index: Int
) : RenderSettingShard {
    override val type: RenderSettingShard.Type<BindBufferBaseShard> = BindBufferBaseShard

    override fun bind(arguments: RenderArguments, pushStack: Boolean): GlBindResult {
        val buffer = buffer(arguments)
        buffer.first?.bindBase(index)
        return buffer.second
    }

    override fun unbind(popStack: Boolean) {
    }

    companion object : RenderSettingShard.Type<BindBufferBaseShard> {
        override val default = BindBufferBaseShard({ null to GlBindResult.Success }, 0)
        override val codec = null
        override val location = ResourceIdentifier("opengl", "bind_buffer_base")
    }
}