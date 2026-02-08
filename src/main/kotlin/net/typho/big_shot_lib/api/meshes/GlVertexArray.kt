package net.typho.big_shot_lib.api.meshes

import com.mojang.blaze3d.vertex.VertexFormat
import net.typho.big_shot_lib.api.GlResource
import net.typho.big_shot_lib.api.StateManager
import org.lwjgl.opengl.GL11.glDrawArrays

open class GlVertexArray(
    glId: Int
) : GlResource(glId) {
    constructor() : this(StateManager.INSTANCE.createVertexArray())

    override fun bind(glId: Int) = StateManager.INSTANCE.bindVertexArray(glId)

    override fun free() {
        StateManager.INSTANCE.deleteVertexArray(glId)
    }

    fun drawArrays(mode: VertexFormat.Mode, vertices: Int) {
        glDrawArrays(mode.asGLMode, 0, vertices)
    }
}