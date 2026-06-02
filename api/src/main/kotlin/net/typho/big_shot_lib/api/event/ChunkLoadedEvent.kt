package net.typho.big_shot_lib.api.event

import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.chunk.ChunkAccess

fun interface ChunkLoadedEvent {
    fun onChunkLoaded(
        level: LevelAccessor,
        chunk: ChunkAccess
    )
}