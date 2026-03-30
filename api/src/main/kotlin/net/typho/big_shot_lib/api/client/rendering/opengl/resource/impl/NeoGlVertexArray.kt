package net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundVertexArray
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlVertexArray
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager

open class NeoGlVertexArray(glId: Int, autoFree: Boolean) : NeoGlResource(GlResourceType.VERTEX_ARRAY, glId, autoFree), GlVertexArray {
    constructor() : this(GlResourceType.VERTEX_ARRAY.create(), true)

    override fun bind(): GlBoundVertexArray {
        return GlBoundVertexArray.Basic(
            this,
            NeoGlStateManager.INSTANCE.vertexArray.push(glId)
        )
    }
}