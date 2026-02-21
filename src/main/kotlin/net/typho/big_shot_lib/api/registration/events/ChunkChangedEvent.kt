package net.typho.big_shot_lib.api.registration.events

import net.minecraft.world.level.chunk.LevelChunk

fun interface ChunkChangedEvent {
    fun invoke(old: LevelChunk?, new: LevelChunk?)
}