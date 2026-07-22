package net.typho.big_shot_lib.api.event

import net.typho.big_shot_lib.api.InternalUtil

interface NeoEventBus {
    /**
     * The wrapped per-loader event bus instance. Null on fabric, EventBus on forge and neoforge.
     */
    val loaderBusInstance: Any?

    fun register(event: PlayedBrewedPotionEvent)

    fun register(event: PotionPreBrewEvent)

    fun register(event: PotionPostBrewEvent)

    fun register(event: RegisterBrewingRecipesEvent)

    fun register(event: EnchantedBlockLootEvent)

    fun register(event: EnchantedEntityLootEvent)

    fun register(event: GetEnchantmentLevelEvent)

    fun register(event: ItemEntityDespawnEvent)

    fun register(event: DropItemEvent)

    fun register(event: RegisterServerReloadListenersEvent)

    fun register(event: ChunkLoadedEvent)

    fun register(event: ChunkUnloadedEvent)

    fun register(event: NewRegistryEvent)

    fun register(event: RegisterEvent)

    fun register(event: BlockStateChangedEvent)

    companion object {
        @JvmStatic
        operator fun get(modId: String) = InternalUtil.getEventBus(modId)
    }
}