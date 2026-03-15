package net.typho.big_shot_lib.api.client.opengl

import net.typho.big_shot_lib.api.client.opengl.buffers.GlBuffer
import net.typho.big_shot_lib.api.client.opengl.buffers.GlBufferUsage
import net.typho.big_shot_lib.api.client.opengl.buffers.Mesh
import net.typho.big_shot_lib.api.client.opengl.buffers.NeoVertexFormat
import net.typho.big_shot_lib.api.client.opengl.state.CullFace
import net.typho.big_shot_lib.api.client.opengl.state.GlBufferType
import net.typho.big_shot_lib.api.client.opengl.state.GlWhiteboard
import net.typho.big_shot_lib.api.client.opengl.util.GlShapeType

object Test {
    @JvmStatic
    fun main(args: Array<String>) {
        GlWhiteboard().use { whiteboard ->
            whiteboard.cullFaceEnabled = true
            whiteboard.cullFace = CullFace.FRONT

            val buffer = GlBuffer(GlBufferUsage.STATIC_DRAW)
            val mesh = Mesh(NeoVertexFormat.POSITION, GlShapeType.QUADS, GlBufferUsage.STATIC_DRAW)

            mesh.upload { builder ->
                builder.vertex(0f, 0f, 0f)
                builder.vertex(1f, 0f, 0f)
                builder.vertex(1f, 1f, 0f)
                builder.vertex(0f, 1f, 0f)
            }

            buffer.bind(GlBufferType.ARRAY_BUFFER).use { bound ->
                bound.uploadNull()
            }

            mesh.draw()
        }
    }
}