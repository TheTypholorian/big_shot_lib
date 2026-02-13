package net.typho.big_shot_lib.api.client.rendering.state

import net.typho.big_shot_lib.api.client.rendering.meshes.GlVertexArray
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

open class VertexArrayShard(
    @JvmField
    val vao: GlVertexArray
) : RenderSettingShard.Basic(
    VertexArrayShard,
    listOf(vao)
) {
    companion object : RenderSettingShard.Type<VertexArrayShard> {
        override fun getDefault() = VertexArrayShard(GlVertexArray.NULL)

        override fun codec() = null

        override fun location(): ResourceIdentifier = ResourceIdentifier("opengl", "vertex_array")
    }
}