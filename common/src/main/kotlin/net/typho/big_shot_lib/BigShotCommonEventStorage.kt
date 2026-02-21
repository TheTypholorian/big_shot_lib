package net.typho.big_shot_lib

import net.typho.big_shot_lib.api.registration.BigShotCommonRegistrationEntrypoint
import net.typho.big_shot_lib.api.registration.events.BlockChangedEvent
import net.typho.big_shot_lib.api.registration.events.ChunkChangedEvent
import net.typho.big_shot_lib.api.registration.events.CommonEventFactory
import java.util.*

object BigShotCommonEventStorage : CommonEventFactory {
    @JvmField
    val onChunkChanged = LinkedList<ChunkChangedEvent>()
    @JvmField
    val onBlockChanged = LinkedList<BlockChangedEvent>()

    init {
        BigShotCommonRegistrationEntrypoint.registerEvents(this)
    }

    override fun onChunkChanged(event: ChunkChangedEvent) {
        onChunkChanged.add(event)
    }

    override fun onBlockChanged(event: BlockChangedEvent) {
        onBlockChanged.add(event)
    }
}