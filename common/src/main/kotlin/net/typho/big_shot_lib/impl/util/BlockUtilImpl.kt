package net.typho.big_shot_lib.impl.util

import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.chunk.ChunkSectionLayer
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.typho.big_shot_lib.api.client.util.BlockRenderSettings
import net.typho.big_shot_lib.api.util.BlockUtil
import org.joml.Vector3f

class BlockUtilImpl : BlockUtil {
    override fun isSolidRender(
        state: BlockState,
        pos: BlockPos,
        level: Level
    ): Boolean {
        return state.isSolidRender
    }

    override fun getOffset(
        state: BlockState,
        pos: BlockPos,
        level: Level
    ): Vector3f {
        return state.getOffset(pos).toVector3f()
    }

    @Suppress("UNCHECKED", "DEPRECATION")
    override fun getBlockRenderSettings(state: BlockState): BlockRenderSettings {
        return when (ItemBlockRenderTypes.getChunkRenderType(state)) {
            ChunkSectionLayer.SOLID -> BlockRenderSettings.SOLID
            ChunkSectionLayer.CUTOUT, ChunkSectionLayer.CUTOUT_MIPPED -> BlockRenderSettings.CUTOUT
            ChunkSectionLayer.TRANSLUCENT -> BlockRenderSettings.TRANSLUCENT
            ChunkSectionLayer.TRIPWIRE -> BlockRenderSettings.TRIPWIRE
            else -> BlockRenderSettings.SOLID
        }
    }

    @Suppress("DEPRECATION")
    override fun shouldRenderFace(
        level: BlockGetter,
        pos: BlockPos,
        direction: Direction,
        state: BlockState
    ): Boolean {
        return Block.shouldRenderFace(
            state,
            level.getBlockState(pos.relative(direction)),
            direction
        )
    }
}