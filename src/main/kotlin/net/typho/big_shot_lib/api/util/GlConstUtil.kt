package net.typho.big_shot_lib.api.util

import com.mojang.blaze3d.vertex.VertexFormat
import org.lwjgl.opengl.GL11.*

object GlConstUtil {
    fun toGlId(mode: VertexFormat.Mode): Int {
        return when (mode) {
            VertexFormat.Mode.LINES -> GL_TRIANGLES
            VertexFormat.Mode.DEBUG_LINES -> GL_LINES
            VertexFormat.Mode.DEBUG_LINE_STRIP -> GL_LINE_STRIP
            VertexFormat.Mode.POINTS -> GL_POINTS
            VertexFormat.Mode.TRIANGLES -> GL_TRIANGLES
            VertexFormat.Mode.TRIANGLE_STRIP -> GL_TRIANGLE_STRIP
            VertexFormat.Mode.TRIANGLE_FAN -> GL_TRIANGLE_FAN
            VertexFormat.Mode.QUADS -> GL_TRIANGLES
        }
    }

    fun toGlId(type: VertexFormat.IndexType): Int {
        return when (type) {
            VertexFormat.IndexType.SHORT -> GL_UNSIGNED_SHORT
            VertexFormat.IndexType.INT -> GL_UNSIGNED_INT
        }
    }
}