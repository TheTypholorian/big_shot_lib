package net.typho.big_shot_lib.api.state

import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.meshes.GlVertexArray

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

        override fun location(): Identifier = Identifier.fromNamespaceAndPath("opengl", "vertex_array")
    }
}