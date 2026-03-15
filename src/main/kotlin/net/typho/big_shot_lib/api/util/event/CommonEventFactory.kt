package net.typho.big_shot_lib.api.util.event

interface CommonEventFactory {
    fun onChunkChanged(event: ChunkChangedEvent)

    fun onBlockChanged(event: BlockChangedEvent)
}