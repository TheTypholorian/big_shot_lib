package net.typho.big_shot_lib.impl.event

import net.minecraft.core.Registry
import net.minecraft.core.WritableRegistry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import net.neoforged.bus.api.IEventBus
import net.typho.big_shot_lib.api.event.*
import net.typho.big_shot_lib.impl.RegistryBuilderImpl
import java.util.function.Consumer

class NeoEventBusImpl(
    override val loaderBusInstance: IEventBus
) : NeoEventBus {
    override fun register(event: PlayedBrewedPotionEvent) {
        loaderBusInstance.addListener { e: net.neoforged.neoforge.event.brewing.PlayerBrewedPotionEvent ->
            event(e.entity, e.stack)
        }
    }

    override fun register(event: PotionPreBrewEvent) {
        loaderBusInstance.addListener { e: net.neoforged.neoforge.event.brewing.PotionBrewEvent.Pre ->
            if (
                event(object : BrewingStandAccess {
                    override val numSlots: Int
                        get() = e.length

                    override fun get(index: Int): ItemStack {
                        return e.getItem(index)
                    }

                    override fun set(index: Int, stack: ItemStack) {
                        e.setItem(index, stack)
                    }
                })
            ) {
                e.isCanceled = true
            }
        }
    }

    override fun register(event: PotionPostBrewEvent) {
        loaderBusInstance.addListener { e: net.neoforged.neoforge.event.brewing.PotionBrewEvent.Post ->
            event(object : BrewingStandAccess {
                override val numSlots: Int
                    get() = e.length

                override fun get(index: Int): ItemStack {
                    return e.getItem(index)
                }

                override fun set(index: Int, stack: ItemStack) {
                    e.setItem(index, stack)
                }
            })
        }
    }

    override fun register(event: RegisterBrewingRecipesEvent) {
        loaderBusInstance.addListener { e: net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent ->
            event(e.builder, e.registryAccess)
        }
    }

    override fun register(event: EnchantedBlockLootEvent) {
        loaderBusInstance.addListener { e: net.neoforged.neoforge.event.enchanting.EnchantedBlockLootEvent ->
            event(e.level, e.pos, e.state, e.tool, e.enchantment, object : EnchantmentLevelAccess {
                override fun get(): Int {
                    return e.enchantmentLevel
                }

                override fun set(level: Int) {
                    e.enchantmentLevel = level
                }
            })
        }
    }

    override fun register(event: EnchantedEntityLootEvent) {
        loaderBusInstance.addListener { e: net.neoforged.neoforge.event.enchanting.EnchantedEntityLootEvent ->
            event(e.entity, e.damageSource, e.enchantment, object : EnchantmentLevelAccess {
                override fun get(): Int {
                    return e.enchantmentLevel
                }

                override fun set(level: Int) {
                    e.enchantmentLevel = level
                }
            })
        }
    }

    override fun register(event: GetEnchantmentLevelEvent) {
        loaderBusInstance.addListener { e: net.neoforged.neoforge.event.enchanting.GetEnchantmentLevelEvent ->
            event(e.stack, e.enchantments, e.lookup, e.targetEnchant)
        }
    }

    override fun register(event: ItemEntityDespawnEvent) {
        loaderBusInstance.addListener { e: net.neoforged.neoforge.event.entity.item.ItemExpireEvent ->
            event(e.entity, object : ItemEntityExtraLifespanAccess {
                override fun get(): Int {
                    return e.extraLife
                }

                override fun set(ticks: Int) {
                    e.extraLife = ticks
                }

                override fun add(ticks: Int) {
                    e.addExtraLife(ticks)
                }
            })
        }
    }

    override fun register(event: DropItemEvent) {
        loaderBusInstance.addListener { e: net.neoforged.neoforge.event.entity.item.ItemTossEvent ->
            if (event(e.entity, e.player)) {
                e.isCanceled = true
            }
        }
    }

    override fun register(event: RegisterServerReloadListenersEvent) {
        loaderBusInstance.addListener { e: net.neoforged.neoforge.event.AddServerReloadListenersEvent ->
            event { listener ->
                e.addListener(listener.location, listener)
            }
        }
    }

    override fun register(event: ChunkLoadedEvent) {
        loaderBusInstance.addListener { e: net.neoforged.neoforge.event.level.ChunkEvent.Load ->
            event(e.level, e.chunk)
        }
    }

    override fun register(event: ChunkUnloadedEvent) {
        loaderBusInstance.addListener { e: net.neoforged.neoforge.event.level.ChunkEvent.Unload ->
            event(e.level, e.chunk)
        }
    }

    override fun register(event: NewRegistryEvent) {
        loaderBusInstance.addListener { e: net.neoforged.neoforge.registries.NewRegistryEvent ->
            event(object : NewRegistryContext {
                override fun <T : Any> register(builder: RegistryBuilder<T>): Registry<T> {
                    if (builder !is RegistryBuilderImpl) {
                        throw ClassCastException("Not allowed to create a custom RegistryBuilder type ($builder), you must use RegistryBuilder.create()")
                    }

                    return e.create(builder.build())
                }

                override fun <T : Any> register(registry: WritableRegistry<T>): WritableRegistry<T> {
                    e.register(registry)
                    return registry
                }
            })
        }
    }

    override fun register(event: RegisterEvent) {
        loaderBusInstance.addListener { e: net.neoforged.neoforge.registries.RegisterEvent ->
            event(object : RegisterContext {
                override fun <T : Any> begin(key: Identifier, out: Consumer<RegistryWriter<T>>) {
                    begin(ResourceKey.createRegistryKey(key), out)
                }

                override fun <T : Any> begin(
                    key: ResourceKey<out Registry<T>>,
                    out: Consumer<RegistryWriter<T>>
                ) {
                    if (e.registryKey == key) {
                        out.accept(object : RegistryWriter<T> {
                            override val key: ResourceKey<out Registry<T>>
                                get() = key

                            override fun register(key: Identifier, value: T) {
                                e.register(this.key, key) { value }
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

    override fun register(event: BlockStateChangedEvent) {
        loaderBusInstance.addListener { e: NeoForgeBlockStateChangedEvent ->
            event(e.level, e.pos, e.oldState, e.newState)
        }
    }
}