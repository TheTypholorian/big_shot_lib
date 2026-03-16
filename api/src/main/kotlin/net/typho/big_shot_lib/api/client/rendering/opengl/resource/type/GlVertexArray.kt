package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

interface GlVertexArray : GlResource {
    fun enableAttribArray(i: Int)

    fun disableAttribArray(i: Int)
}