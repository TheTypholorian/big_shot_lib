package net.typho.big_shot_lib.api.services

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.typho.big_shot_lib.api.BigShotApi.loadService

interface BlockUtil {
    fun isSolidRender(state: BlockState, pos: BlockPos, level: Level): Boolean

    companion object {
        @JvmField
        val INSTANCE: BlockUtil = BlockUtil::class.loadService()
    }
}