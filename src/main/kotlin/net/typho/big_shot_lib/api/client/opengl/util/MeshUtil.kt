package net.typho.big_shot_lib.api.client.opengl.util

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.typho.big_shot_lib.api.BigShotApi.loadService
import net.typho.big_shot_lib.api.client.opengl.buffers.BufferUsage
import net.typho.big_shot_lib.api.client.opengl.buffers.Mesh
import net.typho.big_shot_lib.api.client.opengl.buffers.NeoVertexFormat
import org.joml.Vector3f
import java.util.function.BiConsumer

interface MeshUtil {
    fun getBlockQuads(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        out: BiConsumer<Direction?, List<TexturedQuad>>
    )

    companion object {
        val SCREEN_MESH by lazy {
            val mesh = Mesh(
                NeoVertexFormat.POSITION_TEX,
                GlShapeType.QUADS,
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

        val INSTANCE = MeshUtil::class.loadService()
    }
}