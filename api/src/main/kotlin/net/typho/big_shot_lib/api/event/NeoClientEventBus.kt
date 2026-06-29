package net.typho.big_shot_lib.api.event

import net.typho.big_shot_lib.api.client.event.AddAssetReloadListenersEvent
import net.typho.big_shot_lib.api.client.event.ClientEndFrameEvent
import net.typho.big_shot_lib.api.client.event.ClientEndTickEvent
import net.typho.big_shot_lib.api.client.event.ClientLevelChangedEvent
import net.typho.big_shot_lib.api.client.event.ClientStartFrameEvent
import net.typho.big_shot_lib.api.client.event.ClientStartTickEvent
import net.typho.big_shot_lib.api.client.event.InitialScreenEvent
import net.typho.big_shot_lib.api.client.event.RegisterDebugScreenEntriesEvent
import net.typho.big_shot_lib.api.client.event.RegisterMainMenuModesEvent

interface NeoClientEventBus {
    fun register(event: AddAssetReloadListenersEvent)

    fun register(event: ClientEndFrameEvent)

    fun register(event: ClientEndTickEvent)

    fun register(event: ClientLevelChangedEvent)

    fun register(event: ClientStartFrameEvent)

    fun register(event: ClientStartTickEvent)

    fun register(event: InitialScreenEvent)

    fun register(event: RegisterDebugScreenEntriesEvent)

    fun register(event: RegisterMainMenuModesEvent)
}