package net.typho.big_shot_lib.api.util

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.typho.big_shot_lib.api.BigShotApi.loadService
import net.typho.big_shot_lib.api.client.util.BlockRenderSettings
import org.joml.Vector3f

interface BlockUtil {
    fun isSolidRender(state: BlockState, pos: BlockPos, level: Level): Boolean

    fun getOffset(state: BlockState, pos: BlockPos, level: Level): Vector3f

    fun getBlockRenderSettings(state: BlockState): BlockRenderSettings?

    fun shouldRenderFace(
        level: BlockGetter,
        pos: BlockPos,
        direction: Direction
    ): Boolean

    companion object {
        @JvmField
        val INSTANCE: BlockUtil = BlockUtil::class.loadService()
    }
}