package net.typho.big_shot_lib.api.util

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import net.typho.big_shot_lib.api.buffers.BufferUsage
import net.typho.big_shot_lib.api.meshes.Mesh
import org.joml.Vector3f

object MeshUtil {
    val SCREEN_MESH by lazy {
        val mesh = Mesh(
            DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS,
            BufferUsage.STATIC_DRAW
        )
        val builder = mesh.Builder()
        builder.quad(
            Vector3f(-1f, 1f, 0f),
            Vector3f(1f, 1f, 0f),
            Vector3f(1f, -1f, 0f),
            Vector3f(-1f, -1f, 0f),
            Vector3f(0f, 0f, -1f)
        )
        builder.end()
        return@lazy mesh
    }
}