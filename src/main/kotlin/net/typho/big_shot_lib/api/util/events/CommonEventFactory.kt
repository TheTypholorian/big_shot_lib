package net.typho.big_shot_lib.api.util.events

interface CommonEventFactory {
    fun onChunkChanged(event: ChunkChangedEvent)

    fun onBlockChanged(event: BlockChangedEvent)
}