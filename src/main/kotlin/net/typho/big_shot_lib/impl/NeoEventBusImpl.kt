package net.typho.big_shot_lib.impl

import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.util.profiling.ProfilerFiller
import net.typho.big_shot_lib.api.event.AddDataReloadListenersEvent
import net.typho.big_shot_lib.api.event.BlockChangedEvent
import net.typho.big_shot_lib.api.event.BonemealEvent
import net.typho.big_shot_lib.api.event.ChatMessageEvent
import net.typho.big_shot_lib.api.event.ChunkLoadedEvent
import net.typho.big_shot_lib.api.event.ChunkUnloadedEvent
import net.typho.big_shot_lib.api.event.CommandsEvent
import net.typho.big_shot_lib.api.event.NeoEventBus
import net.typho.big_shot_lib.api.event.ServerEndTickEvent
import net.typho.big_shot_lib.api.event.ServerStartTickEvent
import net.typho.big_shot_lib.api.event.UseItemOnBlockEvent
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

//? fabric {
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.core.Registry
import net.minecraft.core.WritableRegistry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.typho.big_shot_lib.api.event.NewRegistryEvent
import net.typho.big_shot_lib.api.event.RegisterEvent
import net.typho.big_shot_lib.api.event.RegistryBuilder
import java.util.function.Consumer

object NeoEventBusImpl : NeoEventBus {
    override fun register(event: AddDataReloadListenersEvent) {
        val helper = ResourceManagerHelper.get(PackType.SERVER_DATA)
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

    override fun register(event: BlockChangedEvent) {
        TODO("Not yet implemented")
    }

    override fun register(event: BonemealEvent) {
        TODO("Not yet implemented")
    }

    override fun register(event: ChatMessageEvent) {
        TODO("Not yet implemented")
    }

    override fun register(event: ChunkLoadedEvent) {
        ClientChunkEvents.CHUNK_LOAD.register { level, chunk -> event.onChunkLoaded(level, chunk) }
    }

    override fun register(event: ChunkUnloadedEvent) {
        ClientChunkEvents.CHUNK_UNLOAD.register { level, chunk -> event.onChunkUnloaded(level, chunk) }
    }

    override fun register(event: CommandsEvent) {
        CommandRegistrationCallback.EVENT.register { dispatcher, context, selection -> event.registerCommonCommands(dispatcher, selection, context) }
    }

    override fun register(event: NewRegistryEvent) {
        event.registerRegistries(object : NewRegistryEvent.Output {
            override fun <T> register(builder: RegistryBuilder<T>): Registry<T> {
                if (builder !is RegistryBuilderImpl) {
                    throw ClassCastException("Cannot create a custom RegistryBuilder type ($builder), you must use RegistryBuilder.create()")
                }

                return builder.buildAndRegister()
            }

            override fun <T> register(registry: WritableRegistry<T>): WritableRegistry<T> {
                return FabricRegistryBuilder.from(registry).buildAndRegister()
            }
        })
    }

    override fun register(event: RegisterEvent) {
        fun <T : Any> createConsumer(registry: Registry<T>) = object : RegisterEvent.RegistrationConsumer<T> {
            override fun register(key: Identifier, value: T) {
                Registry.register(registry, key, value)
            }

            override fun register(key: ResourceKey<T>, value: T) {
                Registry.register(registry, key, value)
            }
        }

        event.register(object : RegisterEvent.Output {
            @Suppress("UNCHECKED_CAST")
            override fun <T : Any> begin(key: Identifier, out: Consumer<RegisterEvent.RegistrationConsumer<T>>) {
                begin(BuiltInRegistries.REGISTRY.get(key) as Registry<T>, out)
            }

            override fun <T : Any> begin(
                key: ResourceKey<Registry<T>>,
                out: Consumer<RegisterEvent.RegistrationConsumer<T>>
            ) {
                return begin(key.location(), out)
            }

            override fun <T : Any> begin(registry: Registry<T>, out: Consumer<RegisterEvent.RegistrationConsumer<T>>) {
               out.accept(createConsumer(registry))
            }
        })
    }

    override fun register(event: ServerStartTickEvent) {
        ServerTickEvents.START_SERVER_TICK.register { event.serverStartTick(it) }
    }

    override fun register(event: ServerEndTickEvent) {
        ServerTickEvents.END_SERVER_TICK.register { event.serverEndTick(it) }
    }

    override fun register(event: UseItemOnBlockEvent) {
        TODO("Not yet implemented")
    }
}
//? } neoforge {
//? }