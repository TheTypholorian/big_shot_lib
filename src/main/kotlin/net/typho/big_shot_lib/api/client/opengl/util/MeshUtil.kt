package net.typho.big_shot_lib.api.client.opengl.util

import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.typho.big_shot_lib.api.BigShotApi.loadService
import net.typho.big_shot_lib.api.client.opengl.buffers.GlBufferUsage
import net.typho.big_shot_lib.api.client.opengl.buffers.Mesh
import net.typho.big_shot_lib.api.client.opengl.buffers.NeoVertexFormat
import net.typho.big_shot_lib.api.client.util.quads.NeoBakedQuad
import net.typho.big_shot_lib.api.math.NeoDirection
import net.typho.big_shot_lib.api.math.minecraft.NeoBlockPos
import net.typho.big_shot_lib.api.math.vec.NeoVec3f

interface MeshUtil {
    fun getBlockQuads(
        state: BlockState,
        level: Level,
        pos: NeoBlockPos,
        out: (direction: NeoDirection?, quads: List<NeoBakedQuad>) -> Unit
    )

    companion object {
        val SCREEN_MESH by lazy {
            val mesh = Mesh(
                NeoVertexFormat.POSITION_TEX,
                GlShapeType.QUADS,
                GlBufferUsage.STATIC_DRAW
            )
            mesh.upload { builder ->
                builder.quad(
                    NeoVec3f(-1f, 1f, 0f),
                    NeoVec3f(1f, 1f, 0f),
                    NeoVec3f(1f, -1f, 0f),
                    NeoVec3f(-1f, -1f, 0f),
                    NeoVec3f(0f, 0f, -1f)
                )
            }
            return@lazy mesh
        }

        val INSTANCE = MeshUtil::class.loadService()
    }
}