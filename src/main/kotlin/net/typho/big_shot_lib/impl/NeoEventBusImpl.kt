package net.typho.big_shot_lib.impl

import net.minecraft.core.Registry
import net.minecraft.core.WritableRegistry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
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
import net.typho.big_shot_lib.api.event.RegistryBuilder
import net.typho.big_shot_lib.api.event.ServerEndTickEvent
import net.typho.big_shot_lib.api.event.ServerStartTickEvent
import net.typho.big_shot_lib.api.event.UseItemOnBlockEvent
import java.util.function.Consumer

//? fabric {
/*import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.util.profiling.ProfilerFiller
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

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
*///? } neoforge {
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.AddReloadListenerEvent
import net.neoforged.neoforge.event.ServerChatEvent
import net.neoforged.neoforge.event.level.ChunkEvent
import net.neoforged.neoforge.event.tick.ServerTickEvent

class NeoEventBusImpl(
    @JvmField
    val inner: IEventBus
) : NeoEventBus {
    override fun register(event: AddDataReloadListenersEvent) {
        inner.register { e: AddReloadListenerEvent ->
            event.registerReloadListeners { listener ->
                e.addListener(listener)
            }
        }
    }

    override fun register(event: BlockChangedEvent) {
        TODO("Not yet implemented")
    }

    override fun register(event: BonemealEvent) {
        inner.register { e: net.neoforged.neoforge.event.entity.player.BonemealEvent ->
            event.bonemeal(
                e.level,
                e.player,
                e.pos,
                e.state,
                e.stack,
                e.isValidBonemealTarget
            )
        }
    }

    override fun register(event: ChatMessageEvent) {
        inner.register { e: ServerChatEvent ->
            e.message = event.onChatMessage(
                e.player,
                e.username,
                e.rawText,
                e.message
            )
        }
    }

    override fun register(event: ChunkLoadedEvent) {
        inner.register { e: ChunkEvent.Load ->
            event.onChunkLoaded(e.level, e.chunk)
        }
    }

    override fun register(event: ChunkUnloadedEvent) {
        inner.register { e: ChunkEvent.Unload ->
            event.onChunkUnloaded(e.level, e.chunk)
        }
    }

    override fun register(event: CommandsEvent) {
        TODO("Not yet implemented")
    }

    override fun register(event: NewRegistryEvent) {
        inner.register { e: net.neoforged.neoforge.registries.NewRegistryEvent ->
            event.registerRegistries(object : NewRegistryEvent.Output {
                override fun <T> register(builder: RegistryBuilder<T>): Registry<T> {
                    if (builder !is RegistryBuilderImpl) {
                        throw ClassCastException("Cannot create a custom RegistryBuilder type ($builder), you must use RegistryBuilder.create()")
                    }

                    return e.create(builder.build())
                }

                override fun <T> register(registry: WritableRegistry<T>): WritableRegistry<T> {
                    e.register(registry)
                    return registry
                }
            })
        }
    }

    override fun register(event: RegisterEvent) {
        inner.register { e: net.neoforged.neoforge.registries.RegisterEvent ->
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
                    if (e.registry == registry) {
                        out.accept(object : RegisterEvent.RegistrationConsumer<T> {
                            override fun register(key: Identifier, value: T) {
                                e.register(registry.key(), key) { value }
                            }

                            override fun register(key: ResourceKey<T>, value: T) {
                                register(key.location(), value)
                            }
                        })
                    }
                }
            })
        }
    }

    override fun register(event: ServerStartTickEvent) {
        inner.register { e: ServerTickEvent.Pre ->
            event.serverStartTick(e.server)
        }
    }

    override fun register(event: ServerEndTickEvent) {
        inner.register { e: ServerTickEvent.Post ->
            event.serverEndTick(e.server)
        }
    }

    override fun register(event: UseItemOnBlockEvent) {
        inner.register { e: net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent ->
            event.useItemOnBlock(
                e.level,
                e.player,
                e.hand,
                e.itemStack,
                e.pos,
                e.face,
                e.useOnContext
            )
        }
    }
}
//? }