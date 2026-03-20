package net.typho.big_shot_lib.api.util

import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.typho.big_shot_lib.api.BigShotApi.loadService
import net.typho.big_shot_lib.api.client.util.BlockRenderSettings
import net.typho.big_shot_lib.api.math.NeoDirection
import net.typho.big_shot_lib.api.math.vec.AbstractVec3

interface BlockUtil {
    fun isSolidRender(state: BlockState, pos: AbstractVec3<Int, *>, level: Level): Boolean

    fun getOffset(state: BlockState, pos: AbstractVec3<Int, *>, level: Level): AbstractVec3<Float, *>

    fun getBlockRenderSettings(state: BlockState): BlockRenderSettings?

    fun shouldRenderFace(
        level: BlockGetter,
        pos: AbstractVec3<Int, *>,
        direction: NeoDirection,
        state: BlockState = level.getBlockState(BlockPos(pos.x, pos.y, pos.z))
    ): Boolean

    companion object {
        @JvmField
        val INSTANCE = BlockUtil::class.loadService()
    }
}