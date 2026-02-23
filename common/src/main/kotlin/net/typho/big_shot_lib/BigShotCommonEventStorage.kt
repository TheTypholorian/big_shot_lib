package net.typho.big_shot_lib

import net.typho.big_shot_lib.api.util.BigShotCommonEntrypoint
import net.typho.big_shot_lib.api.util.events.BlockChangedEvent
import net.typho.big_shot_lib.api.util.events.ChunkChangedEvent
import net.typho.big_shot_lib.api.util.events.CommonEventFactory
import java.util.*

object BigShotCommonEventStorage : CommonEventFactory {
    @JvmField
    val onChunkChanged = LinkedList<ChunkChangedEvent>()
    @JvmField
    val onBlockChanged = LinkedList<BlockChangedEvent>()

    init {
        BigShotCommonEntrypoint.registerEvents(this)
    }

    override fun onChunkChanged(event: ChunkChangedEvent) {
        onChunkChanged.add(event)
    }

    override fun onBlockChanged(event: BlockChangedEvent) {
        onBlockChanged.add(event)
    }
}