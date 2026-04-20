package net.typho.big_shot_lib.api.client.util.event

import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.chunk.ChunkAccess

fun interface ChunkChangedEvent {
    fun invoke(level: LevelAccessor, old: ChunkAccess?, new: ChunkAccess?)
}