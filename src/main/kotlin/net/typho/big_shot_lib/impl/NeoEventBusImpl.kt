package net.typho.big_shot_lib.impl

import net.minecraft.core.Registry
import net.minecraft.core.WritableRegistry
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
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
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import net.neoforged.neoforge.event.ServerChatEvent
import net.neoforged.neoforge.event.level.ChunkEvent
import net.neoforged.neoforge.event.tick.ServerTickEvent
import net.typho.big_shot_lib.api.event.AddCreativeTabEntriesEvent
import net.typho.big_shot_lib.api.event.ModifyDefaultItemComponentsEvent
import net.typho.big_shot_lib.api.event.RegisterDynamicAdvancementsEvent
import net.typho.big_shot_lib.api.event.RegisterDynamicRecipesEvent
import net.typho.big_shot_lib.api.event.RegisterDynamicTagsEvent
import net.typho.big_shot_lib.api.event.RemoveAdvancementsEvent
import net.typho.big_shot_lib.api.event.RemoveRecipesEvent
import java.util.function.Predicate

class NeoEventBusImpl(
    @JvmField
    val inner: IEventBus
) : NeoEventBus {
    companion object {
        @JvmField
        val REGISTER_EVENTS = arrayListOf<RegisterEvent>()
        @JvmField
        val DYNAMIC_ADVANCEMENT_EVENTS = arrayListOf<RegisterDynamicAdvancementsEvent>()
        @JvmField
        val DYNAMIC_RECIPE_EVENTS = arrayListOf<RegisterDynamicRecipesEvent>()
        @JvmField
        val REMOVE_ADVANCEMENT_EVENTS = arrayListOf<RemoveAdvancementsEvent>()
        @JvmField
        val REMOVE_RECIPE_EVENTS = arrayListOf<RemoveRecipesEvent>()
        @JvmField
        val DYNAMIC_TAG_EVENTS = arrayListOf<RegisterDynamicTagsEvent>()
    }

    override fun register(event: AddCreativeTabEntriesEvent) {
        inner.addListener { e: BuildCreativeModeTabContentsEvent ->
            event.addEntries(object : AddCreativeTabEntriesEvent.Output {
                fun begin(out: Consumer<AddCreativeTabEntriesEvent.EntryConsumer>) {
                    out.accept(object : AddCreativeTabEntriesEvent.EntryConsumer {
                        override val flags: FeatureFlagSet = e.flags

                        override fun showOperatorItems() = e.hasPermissions()

                        override fun addFirst(visibility: CreativeModeTab.TabVisibility, stack: ItemStack) {
                            e.insertFirst(stack, visibility)
                        }

                        override fun addLast(visibility: CreativeModeTab.TabVisibility, stack: ItemStack) {
                            e.accept(stack, visibility)
                        }

                        override fun addBefore(visibility: CreativeModeTab.TabVisibility, before: ItemStack, vararg insert: ItemStack) {
                            var last = before

                            for (stack in insert.reversed()) {
                                e.insertBefore(last, stack, visibility)
                                last = stack
                            }
                        }

                        override fun addAfter(visibility: CreativeModeTab.TabVisibility, after: ItemStack, vararg insert: ItemStack) {
                            var last = after

                            for (stack in insert) {
                                e.insertAfter(last, stack, visibility)
                                last = stack
                            }
                        }

                        override fun remove(visibility: CreativeModeTab.TabVisibility, stack: ItemStack) {
                            e.remove(stack, visibility)
                        }
                    })
                }

                override fun begin(tab: CreativeModeTab, out: Consumer<AddCreativeTabEntriesEvent.EntryConsumer>) {
                    if (e.tab == tab) {
                        begin(out)
                    }
                }

                override fun begin(
                    tab: ResourceKey<CreativeModeTab>,
                    out: Consumer<AddCreativeTabEntriesEvent.EntryConsumer>
                ) {
                    if (e.tabKey == tab) {
                        begin(out)
                    }
                }
            })
        }
    }

    override fun register(event: AddDataReloadListenersEvent) {
        inner.addListener { e: AddReloadListenerEvent ->
            event.registerReloadListeners { listener ->
                e.addListener(listener)
            }
        }
    }

    override fun register(event: BlockChangedEvent) {
        TODO("Not yet implemented")
    }

    override fun register(event: BonemealEvent) {
        inner.addListener { e: net.neoforged.neoforge.event.entity.player.BonemealEvent ->
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
        inner.addListener { e: ServerChatEvent ->
            e.message = event.onChatMessage(
                e.player,
                e.username,
                e.rawText,
                e.message
            )
        }
    }

    override fun register(event: ChunkLoadedEvent) {
        inner.addListener { e: ChunkEvent.Load ->
            event.onChunkLoaded(e.level, e.chunk)
        }
    }

    override fun register(event: ChunkUnloadedEvent) {
        inner.addListener { e: ChunkEvent.Unload ->
            event.onChunkUnloaded(e.level, e.chunk)
        }
    }

    override fun register(event: CommandsEvent) {
        TODO("Not yet implemented")
    }

    override fun register(event: ModifyDefaultItemComponentsEvent) {
        inner.addListener { e: ModifyDefaultComponentsEvent ->
            event.modify(object : ModifyDefaultItemComponentsEvent.Output {
                fun begin(builder: DataComponentPatch.Builder): ModifyDefaultItemComponentsEvent.Builder {
                    return object : ModifyDefaultItemComponentsEvent.Builder {
                        override fun <T : Any> remove(type: DataComponentType<T>) {
                            builder.remove(type)
                        }

                        override fun <T : Any> set(type: DataComponentType<T>, value: T) {
                            builder.set(type, value)
                        }
                    }
                }

                override fun modify(
                    item: ItemLike,
                    out: Consumer<ModifyDefaultItemComponentsEvent.Builder>
                ) {
                    e.modify(item) { out.accept(begin(it)) }
                }

                override fun modify(
                    item: Predicate<Item>,
                    out: Consumer<ModifyDefaultItemComponentsEvent.Builder>
                ) {
                    e.modifyMatching(item) { out.accept(begin(it)) }
                }
            })
        }
    }

    override fun register(event: NewRegistryEvent) {
        inner.addListener { e: net.neoforged.neoforge.registries.NewRegistryEvent ->
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

    override fun register(event: RegisterDynamicAdvancementsEvent) {
        DYNAMIC_ADVANCEMENT_EVENTS.add(event)
    }

    override fun register(event: RegisterDynamicRecipesEvent) {
        DYNAMIC_RECIPE_EVENTS.add(event)
    }

    override fun register(event: RegisterDynamicTagsEvent) {
        DYNAMIC_TAG_EVENTS.add(event)
    }

    override fun register(event: RegisterEvent) {
        REGISTER_EVENTS.add(event)
        inner.addListener { e: net.neoforged.neoforge.registries.RegisterEvent ->
            event.register(object : RegisterEvent.Output {
                @Suppress("UNCHECKED_CAST")
                override fun <T : Any> begin(key: Identifier, out: Consumer<RegisterEvent.RegistrationConsumer<T>>) {
                    (BuiltInRegistries.REGISTRY.get(key) as? Registry<T>)?.let { begin(it, out) }
                }

                override fun <T : Any> begin(
                    key: ResourceKey<out Registry<T>>,
                    out: Consumer<RegisterEvent.RegistrationConsumer<T>>
                ) {
                    return begin(key.identifier(), out)
                }

                override fun <T : Any> begin(registry: Registry<T>, out: Consumer<RegisterEvent.RegistrationConsumer<T>>) {
                    if (e.registry == registry) {
                        out.accept(object : RegisterEvent.RegistrationConsumer<T> {
                            override fun register(key: Identifier, value: T) {
                                e.register(registry.key(), key) { value }
                            }

                            override fun register(key: ResourceKey<out T>, value: T) {
                                register(key.identifier(), value)
                            }
                        })
                    }
                }
            })
        }
    }

    override fun register(event: RemoveAdvancementsEvent) {
        REMOVE_ADVANCEMENT_EVENTS.add(event)
    }

    override fun register(event: RemoveRecipesEvent) {
        REMOVE_RECIPE_EVENTS.add(event)
    }

    override fun register(event: ServerEndTickEvent) {
        inner.addListener { e: ServerTickEvent.Post ->
            event.serverEndTick(e.server)
        }
    }

    override fun register(event: ServerStartTickEvent) {
        inner.addListener { e: ServerTickEvent.Pre ->
            event.serverStartTick(e.server)
        }
    }

    override fun register(event: UseItemOnBlockEvent) {
        inner.addListener { e: net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent ->
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