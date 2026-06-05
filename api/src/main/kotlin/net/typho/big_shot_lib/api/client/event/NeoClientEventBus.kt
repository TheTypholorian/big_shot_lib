package net.typho.big_shot_lib.api.client.event

import net.typho.big_shot_lib.api.client.rendering.util.RenderLevelStage
import net.typho.big_shot_lib.api.event.NeoEventBus

interface NeoClientEventBus : NeoEventBus {
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