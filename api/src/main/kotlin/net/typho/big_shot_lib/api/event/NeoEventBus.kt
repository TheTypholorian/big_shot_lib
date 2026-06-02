package net.typho.big_shot_lib.api.event

interface NeoEventBus {
    fun register(event: AddDataReloadListenersEvent)

    fun register(event: BlockChangedEvent)

    fun register(event: BonemealEvent)

    fun register(event: ChatMessageEvent)

    fun register(event: ChunkLoadedEvent)

    fun register(event: ChunkUnloadedEvent)

    fun register(event: CommandsEvent)

    fun register(event: NewRegistryEvent)

    fun register(event: RegisterEvent)

    fun register(event: ServerStartTickEvent)

    fun register(event: ServerEndTickEvent)

    fun register(event: UseItemOnBlockEvent)
}