package net.typho.big_shot_lib.api.util.events

import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.typho.big_shot_lib.api.math.minecraft.NeoBlockPos

fun interface BlockChangedEvent {
    fun invoke(level: Level, pos: NeoBlockPos, old: BlockState, new: BlockState)
}