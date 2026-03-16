package net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBeginMode
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundResource.Companion.assertBound
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlVertexArray
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlStateStack
import org.lwjgl.opengl.GL11.glDrawArrays
import org.lwjgl.opengl.GL11.glDrawElements
import org.lwjgl.opengl.GL31.glDrawArraysInstanced
import org.lwjgl.opengl.GL31.glDrawElementsInstanced

interface GlBoundVertexArray : GlBoundResource<GlVertexArray> {
    fun drawArrays(
        mode: GlBeginMode,
        firstVertex: Int,
        numVertices: Int
    )

    fun drawArraysInstanced(
        mode: GlBeginMode,
        firstVertex: Int,
        numVertices: Int,
        instanceCount: Int
    )

    fun drawElements(
        mode: GlBeginMode,
        firstVertex: Int,
        numVertices: Int,
        indices: Long
    )

    fun drawElementsInstanced(
        mode: GlBeginMode,
        firstVertex: Int,
        numVertices: Int,
        indices: Long,
        instanceCount: Int
    )

    open class Basic(
        override val resource: GlVertexArray,
        override val handle: GlStateStack.Handle<Int>
    ) : GlBoundVertexArray {
        override fun drawArrays(
            mode: GlBeginMode,
            firstVertex: Int,
            numVertices: Int
        ) {
            assertBound {
                glDrawArrays(mode.glId, firstVertex, numVertices)
            }
        }

        override fun drawArraysInstanced(
            mode: GlBeginMode,
            firstVertex: Int,
            numVertices: Int,
            instanceCount: Int
        ) {
            assertBound {
                glDrawArraysInstanced(mode.glId, firstVertex, numVertices, instanceCount)
            }
        }

        override fun drawElements(
            mode: GlBeginMode,
            firstVertex: Int,
            numVertices: Int,
            indices: Long
        ) {
            assertBound {
                glDrawElements(mode.glId, firstVertex, numVertices, indices)
            }
        }

        override fun drawElementsInstanced(
            mode: GlBeginMode,
            firstVertex: Int,
            numVertices: Int,
            indices: Long,
            instanceCount: Int
        ) {
            assertBound {
                glDrawElementsInstanced(mode.glId, firstVertex, numVertices, indices, instanceCount)
            }
        }
    }
}