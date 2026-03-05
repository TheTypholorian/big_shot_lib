package net.typho.big_shot_lib.impl.util

import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.typho.big_shot_lib.api.client.opengl.util.MeshUtil
import net.typho.big_shot_lib.api.client.util.quads.NeoBakedQuad
import net.typho.big_shot_lib.api.util.BlockUtil
import net.typho.big_shot_lib.api.util.WrapperUtil
import org.joml.Vector3f
import java.util.function.BiConsumer

class MeshUtilImpl : MeshUtil {
    @Suppress("DEPRECATION")
    override fun getBlockQuads(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        out: BiConsumer<Direction?, List<NeoBakedQuad>>
    ) {
        val directions = arrayOf(
            Direction.DOWN,
            Direction.UP,
            Direction.NORTH,
            Direction.SOUTH,
            Direction.WEST,
            Direction.EAST,
            null
        )

        val offset = BlockUtil.INSTANCE.getOffset(state, pos, level)
        val model = Minecraft.getInstance().blockRenderer.getBlockModel(state)
        val random = RandomSource.create()
        val seed = state.getSeed(pos)

        for (direction in directions) {
            random.setSeed(seed)
            out.accept(
                direction,
                model.getQuads(state, direction, random)
                    .map {
                        if (offset.x == 0f && offset.y == 0f && offset.z == 0f)
                            WrapperUtil.INSTANCE.wrap(it)
                        else
                            WrapperUtil.INSTANCE.wrap(it).withVertices { index, vertex ->
                                vertex.withPosition { pos -> pos.add(offset, Vector3f()) }
                            }
                    }
            )
        }
    }
}