package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.state.GlBufferType
import net.typho.big_shot_lib.api.client.opengl.state.GlResourceType
import net.typho.big_shot_lib.api.client.opengl.state.GlStateTracker
import net.typho.big_shot_lib.api.client.opengl.state.GlStateType
import net.typho.big_shot_lib.api.client.opengl.util.*
import org.lwjgl.opengl.GL11.glDrawElements

open class GlVertexArray(
    glId: Int = GlResourceType.VERTEX_ARRAY.create()
) : GlResource(GlResourceType.VERTEX_ARRAY, glId) {
    companion object {
        @JvmField
        val NULL = GlVertexArray(0)
    }

    fun bind(tracker: GlStateTracker = OpenGL.INSTANCE): Bound {
        GlStateType.VERTEX_ARRAY.push(glId, tracker)

        return object : Bound {
            override val vertexArray = this@GlVertexArray

            override fun attachIndices(ebo: GlBuffer) {
                ebo.bind(GlBufferType.ELEMENT_ARRAY_BUFFER).use {
                }
            }

            override fun drawElements(
                mode: GlShapeType,
                indices: Int,
                type: GlIndexType
            ) {
                glDrawElements(mode.glId, indices, type.glId, 0L)
            }

            override fun unbind() {
                GlStateType.VERTEX_ARRAY.pop(tracker)
            }
        }
    }

    interface Bound : BoundResource {
        val vertexArray: GlVertexArray

        fun attachIndices(ebo: GlBuffer)

        fun drawElements(mode: GlShapeType, indices: Int, type: GlIndexType)
    }
}