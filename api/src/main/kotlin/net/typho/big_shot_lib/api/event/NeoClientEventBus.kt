package net.typho.big_shot_lib.api.event

import net.typho.big_shot_lib.api.client.event.AddAssetReloadListenersEvent
import net.typho.big_shot_lib.api.client.event.ClientCommandsEvent
import net.typho.big_shot_lib.api.client.event.ClientEndTickEvent
import net.typho.big_shot_lib.api.client.event.ClientLevelChangedEvent
import net.typho.big_shot_lib.api.client.event.ClientStartTickEvent
import net.typho.big_shot_lib.api.client.event.DisplayResizedEvent
import net.typho.big_shot_lib.api.client.event.InitialScreenEvent
import net.typho.big_shot_lib.api.client.event.ModelLoadingEvent
import net.typho.big_shot_lib.api.client.event.RegisterDebugScreenEntriesEvent
import net.typho.big_shot_lib.api.client.event.RegisterMainMenuModesEvent
import net.typho.big_shot_lib.api.client.event.RenderGUIEvent
import net.typho.big_shot_lib.api.client.event.RenderHandEvent
import net.typho.big_shot_lib.api.client.event.RenderLevelEvent
import net.typho.big_shot_lib.api.client.event.RenderTooltipEvent
import net.typho.big_shot_lib.api.client.rendering.util.RenderLevelStage

interface NeoClientEventBus {
    fun register(event: AddAssetReloadListenersEvent)

    fun register(event: ModelLoadingEvent)

    fun register(event: ClientCommandsEvent)

    fun register(event: ClientEndTickEvent)

    fun register(event: ClientLevelChangedEvent)

    fun register(event: ClientStartTickEvent)

    fun register(event: DisplayResizedEvent)

    fun register(event: InitialScreenEvent)

    fun register(event: RegisterDebugScreenEntriesEvent)

    fun register(event: RegisterMainMenuModesEvent)

    fun register(event: RenderGUIEvent)

    fun register(event: RenderHandEvent)

    fun register(stage: RenderLevelStage, event: RenderLevelEvent)

    fun register(event: RenderTooltipEvent)
}