package net.typho.big_shot_lib.api.client.util.event

import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.world.level.chunk.LevelChunk

fun interface ChunkChangedEvent {
    fun invoke(level: ClientLevel, old: LevelChunk?, new: LevelChunk?)
}