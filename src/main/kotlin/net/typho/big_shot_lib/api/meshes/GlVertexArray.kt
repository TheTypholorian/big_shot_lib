package net.typho.big_shot_lib.api.meshes

import com.mojang.blaze3d.vertex.VertexFormat
import net.typho.big_shot_lib.api.state.OpenGL
import net.typho.big_shot_lib.api.util.GlResource
import org.lwjgl.opengl.GL11.glDrawArrays
import org.lwjgl.opengl.GL11.glDrawElements

open class GlVertexArray(
    glId: Int
) : GlResource(glId) {
    companion object {
        @JvmField
        val NULL = GlVertexArray(0)
    }

    constructor() : this(OpenGL.INSTANCE.createVertexArray())

    override fun bind(glId: Int) = OpenGL.INSTANCE.bindVertexArray(glId)

    override fun free() {
        OpenGL.INSTANCE.deleteVertexArray(glId)
    }

    fun drawArrays(mode: VertexFormat.Mode, vertices: Int) {
        glDrawArrays(mode.asGLMode, 0, vertices)
    }

    fun drawElements(mode: VertexFormat.Mode, indices: Int, type: VertexFormat.IndexType) {
        glDrawElements(mode.asGLMode, indices, type.asGLType, 0L)
    }
}