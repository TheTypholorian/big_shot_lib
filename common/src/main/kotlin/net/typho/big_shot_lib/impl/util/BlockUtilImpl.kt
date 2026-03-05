package net.typho.big_shot_lib.impl.util

import net.minecraft.client.renderer.ItemBlockRenderTypes
import net.minecraft.client.renderer.RenderType
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

    @Suppress("UNCHECKED")
    override fun getBlockRenderSettings(state: BlockState): BlockRenderSettings {
        return when (ItemBlockRenderTypes.getChunkRenderType(state)) {
            RenderType.solid() -> BlockRenderSettings.SOLID
            RenderType.cutout(), RenderType.cutoutMipped() -> BlockRenderSettings.CUTOUT
            RenderType.translucent(), RenderType.translucentMovingBlock() -> BlockRenderSettings.TRANSLUCENT
            RenderType.tripwire() -> BlockRenderSettings.TRIPWIRE
            else -> BlockRenderSettings.SOLID
        }
    }

    override fun shouldRenderFace(
        level: BlockGetter,
        pos: BlockPos,
        direction: Direction
    ): Boolean {
        return Block.shouldRenderFace(
            level.getBlockState(pos),
            level,
            pos,
            direction,
            pos.relative(direction),
        )
    }
}