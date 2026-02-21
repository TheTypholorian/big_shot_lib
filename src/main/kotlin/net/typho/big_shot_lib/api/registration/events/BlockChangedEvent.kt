package net.typho.big_shot_lib.api.registration.events

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

fun interface BlockChangedEvent {
    fun invoke(level: Level, pos: BlockPos, old: BlockState, new: BlockState)
}