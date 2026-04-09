package net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundVertexArray
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlVertexArray
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager
import net.typho.big_shot_lib.api.client.rendering.util.RenderingContext

open class NeoGlVertexArray(glId: Int, autoFree: Boolean, context: RenderingContext = RenderingContext.get()) : NeoGlResource(GlResourceType.VERTEX_ARRAY, glId, autoFree, context), GlVertexArray {
    constructor() : this(GlResourceType.VERTEX_ARRAY.create(), true)

    override fun bind(): GlBoundVertexArray {
        checkUsable()
        return GlBoundVertexArray.Basic(
            this,
            NeoGlStateManager.CURRENT.vertexArray.push(glId)
        )
    }
}