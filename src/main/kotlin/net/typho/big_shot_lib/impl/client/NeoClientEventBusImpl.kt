package net.typho.big_shot_lib.impl.client

import net.minecraft.client.Minecraft
import net.typho.big_shot_lib.api.client.event.AddAssetReloadListenersEvent
import net.typho.big_shot_lib.api.client.event.ClientCommandsEvent
import net.typho.big_shot_lib.api.client.event.ClientEndTickEvent
import net.typho.big_shot_lib.api.client.event.ClientLevelChangedEvent
import net.typho.big_shot_lib.api.client.event.ClientStartTickEvent
import net.typho.big_shot_lib.api.client.event.DisplayResizedEvent
import net.typho.big_shot_lib.api.client.event.InitialScreenEvent
import net.typho.big_shot_lib.api.client.event.RegisterDebugScreenEntriesEvent
import net.typho.big_shot_lib.api.client.event.RegisterMainMenuModesEvent
import net.typho.big_shot_lib.api.client.event.RenderGUIEvent
import net.typho.big_shot_lib.api.client.event.RenderHandEvent
import net.typho.big_shot_lib.api.client.event.RenderLevelEvent
import net.typho.big_shot_lib.api.client.event.RenderTooltipEvent
import net.typho.big_shot_lib.api.client.rendering.util.RenderLevelStage
import net.typho.big_shot_lib.api.event.AddDataReloadListenersEvent
import net.typho.big_shot_lib.api.event.BlockChangedEvent
import net.typho.big_shot_lib.api.event.BonemealEvent
import net.typho.big_shot_lib.api.event.ChatMessageEvent
import net.typho.big_shot_lib.api.event.ChunkLoadedEvent
import net.typho.big_shot_lib.api.event.ChunkUnloadedEvent
import net.typho.big_shot_lib.api.event.CommandsEvent
import net.typho.big_shot_lib.api.event.NeoEventBus
import net.typho.big_shot_lib.api.event.NewRegistryEvent
import net.typho.big_shot_lib.api.event.RegisterEvent
import net.typho.big_shot_lib.api.event.ServerEndTickEvent
import net.typho.big_shot_lib.api.event.ServerStartTickEvent
import net.typho.big_shot_lib.api.event.UseItemOnBlockEvent
import net.typho.big_shot_lib.impl.NeoEventBusImpl
import net.typho.big_shot_lib.mixin.impl.FrustumAccessor

//? fabric {
/*import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.util.profiling.ProfilerFiller
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

object NeoClientEventBusImpl : NeoClientEventBus {
    override fun register(event: AddAssetReloadListenersEvent) {
        val helper = ResourceManagerHelper.get(PackType.CLIENT_RESOURCES)
        event.registerReloadListeners { listener ->
            helper.registerReloadListener(object : IdentifiableResourceReloadListener {
                override fun getFabricId() = listener.location

                override fun reload(
                    preparationBarrier: PreparableReloadListener.PreparationBarrier,
                    resourceManager: ResourceManager,
                    preparationsProfiler: ProfilerFiller,
                    reloadProfiler: ProfilerFiller,
                    backgroundExecutor: Executor,
                    gameExecutor: Executor
                ): CompletableFuture<Void> {
                    return listener.reload(
                        preparationBarrier,
                        resourceManager,
                        preparationsProfiler,
                        reloadProfiler,
                        backgroundExecutor,
                        gameExecutor
                    )
                }
            })
        }
    }

    override fun register(event: ClientChatMessageEvent) {
        TODO("Not yet implemented")
    }

    override fun register(event: ClientCommandsEvent) {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, context -> event.registerClientCommands(dispatcher, context) }
    }

    override fun register(event: ClientEndTickEvent) {
        ClientTickEvents.END_CLIENT_TICK.register { event.onClientEndTick(it) }
    }

    override fun register(event: ClientLevelChangedEvent) {
        TODO("Not yet implemented")
    }

    override fun register(event: ClientStartTickEvent) {
        ClientTickEvents.START_CLIENT_TICK.register { event.onClientStartTick(it) }
    }

    override fun register(event: DisplayResizedEvent) {
        TODO("Not yet implemented")
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

    override fun register(event: RenderGUIEvent) {
        TODO("Not yet implemented")
    }

    override fun register(event: RenderHandEvent) {
        TODO("Not yet implemented")
    }

    override fun register(
        stage: RenderLevelStage,
        event: RenderLevelEvent
    ) {
        TODO("Not yet implemented")
        /*
        fun invoke(context: WorldRenderContext) {
            event.renderLevel(
                context.worldRenderer(),
                context.camera(),
                context.world(),
                context.projectionMatrix(),
                Matrix4f(), // TODO
                (context.frustum() as? FrustumAccessor)?.`big_shot_lib$getFrustumIntersection`(),
                context.tickCounter()
            )
        }

        when (stage) {
            RenderLevelStage.AFTER_SKY -> WorldRenderEvents.START.register { invoke(it) }
            RenderLevelStage.AFTER_SOLID_BLOCKS -> WorldRenderEvents.AFTER.register { invoke(it) }
            RenderLevelStage.AFTER_CUTOUT_MIPPED_BLOCKS -> TODO()
            RenderLevelStage.AFTER_CUTOUT_BLOCKS -> TODO()
            RenderLevelStage.AFTER_ENTITIES -> TODO()
            RenderLevelStage.AFTER_BLOCK_ENTITIES -> TODO()
            RenderLevelStage.AFTER_TRANSLUCENT_BLOCKS -> TODO()
            RenderLevelStage.AFTER_TRIPWIRE_BLOCKS -> TODO()
            RenderLevelStage.AFTER_PARTICLES -> TODO()
            RenderLevelStage.AFTER_WEATHER -> TODO()
            RenderLevelStage.AFTER_LEVEL -> TODO()
        }
         */
    }

    override fun register(event: RenderTooltipEvent) {
        TODO("Not yet implemented")
    }

    override fun register(event: AddDataReloadListenersEvent) {
        NeoEventBusImpl.register(event)
    }

    override fun register(event: BlockChangedEvent) {
        NeoEventBusImpl.register(event)
    }

    override fun register(event: BonemealEvent) {
        NeoEventBusImpl.register(event)
    }

    override fun register(event: ChatMessageEvent) {
        NeoEventBusImpl.register(event)
    }

    override fun register(event: ChunkLoadedEvent) {
        NeoEventBusImpl.register(event)
    }

    override fun register(event: ChunkUnloadedEvent) {
        NeoEventBusImpl.register(event)
    }

    override fun register(event: CommandsEvent) {
        NeoEventBusImpl.register(event)
    }

    override fun register(event: NewRegistryEvent) {
        NeoEventBusImpl.register(event)
    }

    override fun register(event: RegisterEvent) {
        NeoEventBusImpl.register(event)
    }

    override fun register(event: ServerStartTickEvent) {
        NeoEventBusImpl.register(event)
    }

    override fun register(event: ServerEndTickEvent) {
        NeoEventBusImpl.register(event)
    }

    override fun register(event: UseItemOnBlockEvent) {
        NeoEventBusImpl.register(event)
    }
}
*///? } neoforge {
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent
import net.neoforged.neoforge.client.event.RenderGuiEvent
import net.neoforged.neoforge.client.event.RenderLevelStageEvent
import net.neoforged.neoforge.event.RegisterCommandsEvent
import net.typho.big_shot_lib.api.client.event.ModelLoadingEvent
import net.typho.big_shot_lib.api.event.AddCreativeTabEntriesEvent
import net.typho.big_shot_lib.api.event.NeoClientEventBus
import net.typho.big_shot_lib.api.event.RegisterDynamicAdvancementsEvent
import net.typho.big_shot_lib.api.event.RegisterDynamicRecipesEvent
import net.typho.big_shot_lib.api.event.RegisterDynamicTagsEvent

class NeoClientEventBusImpl(
    @JvmField
    val inner: IEventBus,
    @JvmField
    val common: NeoEventBus = NeoEventBusImpl(inner)
) : NeoClientEventBus {
    companion object {
        @JvmField
        val MODEL_LOADING_EVENTS = arrayListOf<ModelLoadingEvent>()
    }

    override fun register(event: AddAssetReloadListenersEvent) {
        inner.addListener { e: RegisterClientReloadListenersEvent ->
            event.registerReloadListeners { listener ->
                e.registerReloadListener(listener)
            }
        }
    }

    override fun register(event: ModelLoadingEvent) {
        MODEL_LOADING_EVENTS.add(event)
    }

    override fun register(event: ClientCommandsEvent) {
        inner.addListener { e: RegisterCommandsEvent ->
            event.registerClientCommands(e.dispatcher, e.buildContext)
        }
    }

    override fun register(event: ClientEndTickEvent) {
        inner.addListener { e: ClientTickEvent.Post ->
            event.onClientEndTick(Minecraft.getInstance())
        }
    }

    override fun register(event: ClientLevelChangedEvent) {
        TODO("Not yet implemented")
    }

    override fun register(event: ClientStartTickEvent) {
        inner.addListener { e: ClientTickEvent.Pre ->
            event.onClientStartTick(Minecraft.getInstance())
        }
    }

    override fun register(event: DisplayResizedEvent) {
        TODO("Not yet implemented")
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

    override fun register(event: RenderTooltipEvent) {
        TODO("Not yet implemented")
    }
}
//? }