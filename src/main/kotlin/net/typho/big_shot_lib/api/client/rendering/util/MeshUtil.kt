package net.typho.big_shot_lib.api.client.rendering.util

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.typho.big_shot_lib.api.BigShotApi.loadService
import net.typho.big_shot_lib.api.client.rendering.buffers.BufferUsage
import net.typho.big_shot_lib.api.client.rendering.meshes.Mesh
import net.typho.big_shot_lib.api.client.rendering.meshes.TexturedQuad
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
        @JvmField
        val SCREEN_MESH: Mesh = Mesh(
            DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS,
            BufferUsage.STATIC_DRAW
        )

        init {
            val builder = SCREEN_MESH.Builder()
            builder.quad(
                Vector3f(-1f, 1f, 0f),
                Vector3f(1f, 1f, 0f),
                Vector3f(1f, -1f, 0f),
                Vector3f(-1f, -1f, 0f),
                Vector3f(0f, 0f, -1f)
            )
            builder.end()
        }

        @JvmField
        val INSTANCE = MeshUtil::class.loadService()
    }
}