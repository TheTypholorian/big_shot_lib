package net.typho.big_shot_lib.api.client.opengl.state.shards

import net.typho.big_shot_lib.api.client.opengl.buffers.GlVertexArray
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

open class VertexArrayShard(
    @JvmField
    val vao: GlVertexArray
) : RenderSettingShard.Basic(
    VertexArrayShard,
    listOf(vao)
) {
    companion object : RenderSettingShard.Type<VertexArrayShard> {
        override val default = VertexArrayShard(GlVertexArray.NULL)
        override val codec = null
        override val location = ResourceIdentifier("opengl", "vertex_array")
    }
}