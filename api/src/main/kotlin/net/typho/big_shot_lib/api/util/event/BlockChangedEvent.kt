package net.typho.big_shot_lib.api.util.event

import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.typho.big_shot_lib.api.math.vec.AbstractVec3

fun interface BlockChangedEvent {
    fun invoke(level: Level, pos: AbstractVec3<Int, *>, old: BlockState, new: BlockState)
}