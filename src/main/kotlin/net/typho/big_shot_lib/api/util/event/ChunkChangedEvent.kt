package net.typho.big_shot_lib.api.util.event

import net.minecraft.world.level.Level
import net.minecraft.world.level.chunk.LevelChunk

fun interface ChunkChangedEvent {
    fun invoke(level: Level?, old: LevelChunk?, new: LevelChunk?)
}