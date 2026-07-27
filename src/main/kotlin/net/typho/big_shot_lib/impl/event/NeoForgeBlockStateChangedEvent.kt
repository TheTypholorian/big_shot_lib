package net.typho.big_shot_lib.impl.event

import net.minecraft.core.BlockPos
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.event.level.LevelEvent

class NeoForgeBlockStateChangedEvent(
    level: LevelAccessor,
    val pos: BlockPos,
    val oldState: BlockState,
    val newState: BlockState,
) : LevelEvent(level)
