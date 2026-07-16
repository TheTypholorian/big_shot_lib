package net.typho.big_shot_lib.api.event

import net.typho.big_shot_lib.client.api.InternalClientUtil
import net.typho.big_shot_lib.client.api.event.AddAssetReloadListenersEvent
import net.typho.big_shot_lib.client.api.event.ClientEndFrameEvent
import net.typho.big_shot_lib.client.api.event.ClientEndTickEvent
import net.typho.big_shot_lib.client.api.event.ClientLevelChangedEvent
import net.typho.big_shot_lib.client.api.event.ClientStartFrameEvent
import net.typho.big_shot_lib.client.api.event.ClientStartTickEvent
import net.typho.big_shot_lib.client.api.event.InitialScreenEvent
import net.typho.big_shot_lib.client.api.event.RegisterDebugScreenEntriesEvent
import net.typho.big_shot_lib.client.api.event.RegisterMainMenuModesEvent

interface NeoClientEventBus {
    /**
     * The wrapped per-loader client event bus instance. Null on fabric, EventBus on forge and neoforge.
     */
    val loaderBusInstance: Any?

    fun register(event: AddAssetReloadListenersEvent)

    fun register(event: ClientEndFrameEvent)

    fun register(event: ClientEndTickEvent)

    fun register(event: ClientLevelChangedEvent)

    fun register(event: ClientStartFrameEvent)

    fun register(event: ClientStartTickEvent)

    fun register(event: InitialScreenEvent)

    fun register(event: RegisterDebugScreenEntriesEvent)

    fun register(event: RegisterMainMenuModesEvent)

    companion object {
        @JvmStatic
        operator fun get(modId: String) = InternalClientUtil.getEventBus(modId)
    }
}