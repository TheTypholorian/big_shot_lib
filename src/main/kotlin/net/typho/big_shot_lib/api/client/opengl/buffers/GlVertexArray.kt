package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.state.GlResourceType
import net.typho.big_shot_lib.api.client.opengl.util.GlIndexType
import net.typho.big_shot_lib.api.client.opengl.util.GlResource
import net.typho.big_shot_lib.api.client.opengl.util.GlShapeType
import org.lwjgl.opengl.GL11.glDrawElements

open class GlVertexArray(
    glId: Int = GlResourceType.VertexArray.create()
) : GlResource<GlResourceType.VertexArray>(GlResourceType.VertexArray, glId) {
    companion object {
        @JvmField
        val NULL = GlVertexArray(0)
    }

    fun uploadIndices(ebo: GlBuffer, )

    fun drawElements(mode: GlShapeType, indices: Int, type: GlIndexType) {
        bind()
        glDrawElements(mode.glId, indices, type.glId, 0L)
        unbind()
    }
}