package net.typho.big_shot_lib.client.impl

import net.minecraft.client.Minecraft
import net.typho.big_shot_lib.client.api.event.AddAssetReloadListenersEvent
import net.typho.big_shot_lib.client.api.event.ClientEndTickEvent
import net.typho.big_shot_lib.client.api.event.ClientLevelChangedEvent
import net.typho.big_shot_lib.client.api.event.ClientStartTickEvent
import net.typho.big_shot_lib.client.api.event.InitialScreenEvent
import net.typho.big_shot_lib.client.api.event.RegisterDebugScreenEntriesEvent

//? fabric {
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents
import net.fabricmc.fabric.api.resource.v1.ResourceLoader
import net.minecraft.client.Minecraft
import net.minecraft.server.packs.PackType
import net.typho.big_shot_lib.client.api.event.ClientEndFrameEvent
import net.typho.big_shot_lib.client.api.event.ClientStartFrameEvent
import net.typho.big_shot_lib.api.event.NeoClientEventBus

object NeoClientEventBusImpl : NeoClientEventBus {
    @JvmField
    val CLIENT_LEVEL_CHANGED = mutableListOf<ClientLevelChangedEvent>()

    override fun register(event: AddAssetReloadListenersEvent) {
        event.registerReloadListeners { listener ->
            ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(listener.location, listener)
        }
    }

    override fun register(event: ClientEndFrameEvent) {
        LevelRenderEvents.END_MAIN.register { event.onClientEndFrame(Minecraft.getInstance()) }
    }

    override fun register(event: ClientEndTickEvent) {
        ClientTickEvents.END_CLIENT_TICK.register { event.onClientEndTick(it) }
    }

    override fun register(event: ClientLevelChangedEvent) {
        CLIENT_LEVEL_CHANGED.add(event)
    }

    override fun register(event: ClientStartFrameEvent) {
        LevelRenderEvents.START_MAIN.register { event.onClientStartFrame(Minecraft.getInstance()) }
    }

    override fun register(event: ClientStartTickEvent) {
        ClientTickEvents.START_CLIENT_TICK.register { event.onClientStartTick(it) }
    }

    override fun register(event: InitialScreenEvent) {
        TODO("Not yet implemented")
    }

    override fun register(event: RegisterDebugScreenEntriesEvent) {
        TODO("Not yet implemented")
    }

    override fun register(event: RegisterMainMenuModesEvent) {
        MainMenuModeManager.register(event)
    }
}
//? } neoforge {
/*import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.client.event.RenderLevelStageEvent
import net.typho.big_shot_lib.api.event.NeoClientEventBus
import net.typho.big_shot_lib.api.event.NeoEventBus
import net.typho.big_shot_lib.client.api.event.ClientEndFrameEvent
import net.typho.big_shot_lib.client.api.event.ClientStartFrameEvent
import net.typho.big_shot_lib.impl.event.NeoEventBusImpl
import net.typho.big_shot_lib.mixin.client.FrustumAccessor

class NeoClientEventBusImpl(
    @JvmField
    val inner: IEventBus,
    @JvmField
    val common: NeoEventBus = NeoEventBusImpl(inner)
) : NeoClientEventBus {
    override val loaderBusInstance: Any?
        get() = TODO("Not yet implemented")

    companion object {
        /*
        @JvmField
        val MODEL_LOADING_EVENTS = arrayListOf<ModelLoadingEvent>()
         */
    }

    override fun register(event: AddAssetReloadListenersEvent) {
        /*
        inner.addListener { e: RegisterClientReloadListenersEvent ->
            event.registerReloadListeners { listener ->
                e.registerReloadListener(listener)
            }
        }
         */
    }

    override fun register(event: ClientEndFrameEvent) {
        TODO("Not yet implemented")
    }

    /*
    override fun register(event: ModelLoadingEvent) {
        MODEL_LOADING_EVENTS.add(event)
    }

    override fun register(event: ClientCommandsEvent) {
        inner.addListener { e: RegisterCommandsEvent ->
            event.registerClientCommands(e.dispatcher, e.buildContext)
        }
    }
     */

    override fun register(event: ClientEndTickEvent) {
        inner.addListener { e: ClientTickEvent.Post ->
            event.onClientEndTick(Minecraft.getInstance())
        }
    }

    override fun register(event: ClientLevelChangedEvent) {
        TODO("Not yet implemented")
    }

    override fun register(event: ClientStartFrameEvent) {
        TODO("Not yet implemented")
    }

    override fun register(event: ClientStartTickEvent) {
        inner.addListener { e: ClientTickEvent.Pre ->
            event.onClientStartTick(Minecraft.getInstance())
        }
    }

    /*
    override fun register(event: DisplayResizedEvent) {
        TODO("Not yet implemented")
    }
     */

    override fun register(event: InitialScreenEvent) {
        TODO("Not yet implemented")
    }

    override fun register(event: RegisterDebugScreenEntriesEvent) {
        TODO("Not yet implemented")
    }

    //override fun register(event: RegisterMainMenuModesEvent) {
    //    MainMenuModeManager.register(event)
    //}

    /*
    override fun register(event: RenderGUIEvent) {
        inner.addListener { e: RenderGuiEvent ->
            event.renderGui(e.guiGraphics, e.partialTick)
        }
    }

    override fun register(event: RenderHandEvent) {
        inner.addListener { e: net.neoforged.neoforge.client.event.RenderHandEvent ->
            event.renderHand(
                e.hand,
                e.poseStack,
                e.multiBufferSource,
                e.packedLight,
                e.partialTick,
                e.interpolatedPitch,
                e.swingProgress,
                e.equipProgress,
                e.itemStack
            )
        }
    }
     */

    /*
    override fun register(
        stage: RenderLevelStage,
        event: RenderLevelEvent
    ) {
        inner.addListener { e: RenderLevelStageEvent ->
            val neoStage = when (e.stage) {
                RenderLevelStageEvent.Stage.AFTER_SKY -> RenderLevelStage.AFTER_SKY
                RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS -> RenderLevelStage.AFTER_SOLID_BLOCKS
                RenderLevelStageEvent.Stage.AFTER_CUTOUT_MIPPED_BLOCKS_BLOCKS -> RenderLevelStage.AFTER_CUTOUT_MIPPED_BLOCKS
                RenderLevelStageEvent.Stage.AFTER_CUTOUT_BLOCKS -> RenderLevelStage.AFTER_CUTOUT_BLOCKS
                RenderLevelStageEvent.Stage.AFTER_ENTITIES -> RenderLevelStage.AFTER_ENTITIES
                RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES -> RenderLevelStage.AFTER_BLOCK_ENTITIES
                RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS -> RenderLevelStage.AFTER_TRANSLUCENT_BLOCKS
                RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS -> RenderLevelStage.AFTER_TRIPWIRE_BLOCKS
                RenderLevelStageEvent.Stage.AFTER_PARTICLES -> RenderLevelStage.AFTER_PARTICLES
                RenderLevelStageEvent.Stage.AFTER_WEATHER -> RenderLevelStage.AFTER_WEATHER
                RenderLevelStageEvent.Stage.AFTER_LEVEL -> RenderLevelStage.AFTER_LEVEL
                else -> return@addListener
            }

            if (stage == neoStage) {
                event.renderLevel(
                    e.levelRenderer,
                    e.camera,
                    e.levelRenderer.level!!,
                    e.projectionMatrix,
                    e.modelViewMatrix,
                    (e.frustum as FrustumAccessor).`big_shot_lib$getFrustumIntersection`(),
                    // NeoGlStateManagerImpl.currentTarget ?:
                    //GlFramebuffer.MAIN, // TODO
                    e.partialTick
                )
            }
        }
    }
     */
}
*///? }