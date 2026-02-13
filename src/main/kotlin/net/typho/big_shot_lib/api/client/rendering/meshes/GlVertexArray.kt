package net.typho.big_shot_lib.api.client.rendering.meshes

import com.mojang.blaze3d.vertex.VertexFormat
import net.typho.big_shot_lib.api.client.rendering.services.GlUtil
import net.typho.big_shot_lib.api.client.rendering.state.OpenGL
import net.typho.big_shot_lib.api.client.rendering.util.GlResource
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

    fun drawElements(mode: VertexFormat.Mode, indices: Int, type: VertexFormat.IndexType) {
        glDrawElements(GlUtil.INSTANCE.toGlId(mode), indices, GlUtil.INSTANCE.toGlId(type), 0L)
    }
}