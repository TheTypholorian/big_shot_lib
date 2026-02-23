package net.typho.big_shot_lib.impl.util

import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.typho.big_shot_lib.api.client.opengl.util.MeshUtil
import net.typho.big_shot_lib.api.client.opengl.util.TexturedQuad
import net.typho.big_shot_lib.api.util.BlockUtil
import net.typho.big_shot_lib.api.util.WrapperUtil
import java.util.function.BiConsumer

class MeshUtilImpl : MeshUtil {
    @Suppress("DEPRECATION")
    override fun getBlockQuads(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        out: BiConsumer<Direction?, List<TexturedQuad>>
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
        val parts = model.collectParts(random)

        for (direction in directions) {
            out.accept(
                direction,
                parts.flatMap { it.getQuads(direction) }
                    .map { WrapperUtil.INSTANCE.wrap(it).offset(offset) }
            )
        }
    }
}