package net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundVertexArray
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlVertexArray
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager

open class NeoGlVertexArray(glId: Int, autoFree: Boolean) : NeoGlResource(GlResourceType.VERTEX_ARRAY, glId, autoFree), GlVertexArray {
    constructor() : this(GlResourceType.VERTEX_ARRAY.create(), true)

    override val thread: Thread = Thread.currentThread()

    override fun bind(): GlBoundVertexArray {
        throwIfNotExists()
        return GlBoundVertexArray.Basic(
            this,
            NeoGlStateManager.CURRENT.vertexArray.push(glId)
        )
    }
}