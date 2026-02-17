package net.typho.big_shot_lib.api.services

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import java.util.*

interface BlockUtil {
    fun isSolidRender(state: BlockState, pos: BlockPos, level: Level): Boolean

    companion object {
        @JvmField
        val INSTANCE: BlockUtil = ServiceLoader.load(BlockUtil::class.java).findFirst().orElseThrow()
    }
}