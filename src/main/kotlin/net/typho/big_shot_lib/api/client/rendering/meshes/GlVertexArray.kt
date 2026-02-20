package net.typho.big_shot_lib.api.client.rendering.meshes

import net.typho.big_shot_lib.api.client.rendering.state.GlStateStack
import net.typho.big_shot_lib.api.client.rendering.state.OpenGL
import net.typho.big_shot_lib.api.client.rendering.util.GlIndexType
import net.typho.big_shot_lib.api.client.rendering.util.GlResource
import net.typho.big_shot_lib.api.client.rendering.util.GlShapeType
import org.lwjgl.opengl.GL11.glDrawElements

open class GlVertexArray(
    glId: Int
) : GlResource(glId, GlStateStack.vertexArray) {
    companion object {
        @JvmField
        val NULL = GlVertexArray(0)
    }

    constructor() : this(OpenGL.INSTANCE.createVertexArray())

    override fun free() {
        OpenGL.INSTANCE.deleteVertexArray(glId)
    }

    fun drawElements(mode: GlShapeType, indices: Int, type: GlIndexType) {
        bind()
        glDrawElements(mode.glId, indices, type.glId, 0L)
        unbind()
    }
}