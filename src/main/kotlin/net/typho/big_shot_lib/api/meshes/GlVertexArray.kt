package net.typho.big_shot_lib.api.meshes

import com.mojang.blaze3d.vertex.VertexFormat
import net.typho.big_shot_lib.api.GlResource
import net.typho.big_shot_lib.api.state.OpenGL
import org.lwjgl.opengl.GL11.glDrawArrays

open class GlVertexArray(
    glId: Int
) : GlResource(glId) {
    constructor() : this(OpenGL.INSTANCE.createVertexArray())

    override fun bind(glId: Int) = OpenGL.INSTANCE.bindVertexArray(glId)

    override fun free() {
        OpenGL.INSTANCE.deleteVertexArray(glId)
    }

    fun drawArrays(mode: VertexFormat.Mode, vertices: Int) {
        glDrawArrays(mode.asGLMode, 0, vertices)
    }
}