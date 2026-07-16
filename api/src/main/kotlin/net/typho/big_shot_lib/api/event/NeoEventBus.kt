package net.typho.big_shot_lib.api.event

import net.typho.big_shot_lib.api.InternalUtil

interface NeoEventBus {
    /**
     * The wrapped per-loader event bus instance. Null on fabric, EventBus on forge and neoforge.
     */
    val loaderBusInstance: Any?

    fun register(event: AddDataReloadListenersEvent)

    fun register(event: BlockChangedEvent)

    fun register(event: ChunkLoadedEvent)

    fun register(event: ChunkUnloadedEvent)

    fun register(event: NewRegistryEvent)

    fun register(event: RegisterEvent)

    companion object {
        @JvmStatic
        operator fun get(modId: String) = InternalUtil.getEventBus(modId)
    }
}