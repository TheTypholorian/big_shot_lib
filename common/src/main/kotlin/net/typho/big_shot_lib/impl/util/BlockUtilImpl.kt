package net.typho.big_shot_lib.impl.util

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.typho.big_shot_lib.api.services.BlockUtil
import org.joml.Vector3f

class BlockUtilImpl : BlockUtil {
    override fun isSolidRender(
        state: BlockState,
        pos: BlockPos,
        level: Level
    ): Boolean {
        return state.isSolidRender(level, pos)
    }

    override fun getOffset(
        state: BlockState,
        pos: BlockPos,
        level: Level
    ): Vector3f {
        return state.getOffset(level, pos).toVector3f()
    }
}