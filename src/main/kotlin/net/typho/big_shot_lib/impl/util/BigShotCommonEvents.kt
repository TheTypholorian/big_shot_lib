package net.typho.big_shot_lib.impl.util

//? fabric {
/*import com.mojang.serialization.Lifecycle
import net.minecraft.core.DefaultedMappedRegistry
import net.minecraft.core.MappedRegistry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.typho.big_shot_lib.api.util.NeoRegistry
import net.typho.big_shot_lib.api.util.RegistrationConsumer
import net.typho.big_shot_lib.api.util.RegistrationFactory
import net.typho.big_shot_lib.api.util.RegistryFactory
import net.typho.big_shot_lib.api.util.WrapperUtil
import net.typho.big_shot_lib.impl.mojang
import net.typho.big_shot_lib.impl.neo
*///? } neoforge {

//? }

import com.mojang.blaze3d.platform.InputConstants
import com.mojang.serialization.Lifecycle
import net.minecraft.client.KeyMapping
import net.minecraft.core.DefaultedMappedRegistry
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ResourceManagerReloadListener
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.RegisterEvent
import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint
import net.typho.big_shot_lib.api.client.util.KeyMappingCategory
import net.typho.big_shot_lib.api.client.util.KeyMappingFactory
import net.typho.big_shot_lib.api.client.util.ResourceListenerFactory
import net.typho.big_shot_lib.api.client.util.resource.NeoResourceManagerReloadListener
import net.typho.big_shot_lib.api.client.util.resource.ResourceRegistry
import net.typho.big_shot_lib.api.util.BigShotCommonEntrypoint
import net.typho.big_shot_lib.api.util.NeoRegistry
import net.typho.big_shot_lib.api.util.RegisteredObject
import net.typho.big_shot_lib.api.util.RegistrationConsumer
import net.typho.big_shot_lib.api.util.RegistrationFactory
import net.typho.big_shot_lib.api.util.RegistryFactory
import net.typho.big_shot_lib.api.util.WrapperUtil
import net.typho.big_shot_lib.api.util.event.BlockChangedEvent
import net.typho.big_shot_lib.api.util.event.CommonEventFactory
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import net.typho.big_shot_lib.api.util.resource.NeoResourceKey
import net.typho.big_shot_lib.impl.mojang
import net.typho.big_shot_lib.impl.neo
import java.util.function.UnaryOperator

object BigShotCommonEvents : CommonEventFactory {
    override val blockChanged: MutableList<BlockChangedEvent> = arrayListOf()

    data class RegisteredObjectImpl<T : Any>(
        override val registry: NeoResourceKey<Registry<T>>,
        override val key: NeoIdentifier,
        @JvmField
        val value: T
    ) : RegisteredObject<T> {
        override fun get() = value

        override fun isRegistered() = true
    }

    init {
        BigShotCommonEntrypoint.registerEvents(this)

        //? fabric {
        /*BigShotCommonEntrypoint.registerRegistries(object : RegistryFactory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : Any> create(
                location: NeoIdentifier,
                lifecycle: Lifecycle,
                isIntrusive: Boolean
            ): NeoRegistry<T> {
                return WrapperUtil.INSTANCE.wrap(Registry.register(
                    BuiltInRegistries.REGISTRY as Registry<Registry<T>>,
                    location.mojang,
                    MappedRegistry(ResourceKey.createRegistryKey(location.mojang), lifecycle, isIntrusive)
                ))
            }

            @Suppress("UNCHECKED_CAST")
            override fun <T : Any> createDefaulted(
                location: NeoIdentifier,
                defaultKey: NeoIdentifier,
                lifecycle: Lifecycle,
                isIntrusive: Boolean
            ): NeoRegistry<T> {
                return WrapperUtil.INSTANCE.wrap(Registry.register(
                    BuiltInRegistries.REGISTRY as Registry<Registry<T>>,
                    location.mojang,
                    DefaultedMappedRegistry(
                        location.toString(),
                        ResourceKey.createRegistryKey(location.mojang),
                        lifecycle,
                        isIntrusive
                    )
                ))
            }
        })
        BigShotCommonEntrypoint.registerContent(object : RegistrationFactory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : Any> begin(key: NeoResourceKey<Registry<T>>): RegistrationConsumer<T, NeoIdentifier> {
                val registry = BuiltInRegistries.REGISTRY.get(key.location.mojang) as Registry<T>

                return object : RegistrationConsumer<T, NeoIdentifier> {
                    override fun <V : T> register(
                        id: NeoIdentifier,
                        value: () -> V
                    ): RegisteredObject<V> {
                        return RegisteredObjectImpl(
                            key as NeoResourceKey<Registry<V>>,
                            id,
                            Registry.register(
                                registry,
                                id.mojang,
                                value()
                            )
                        )
                    }
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun <T : Any> begin(
                key: NeoResourceKey<Registry<T>>,
                namespace: String
            ): RegistrationConsumer<T, String> {
                val registry = BuiltInRegistries.REGISTRY.get(key.location.mojang) as Registry<T>

                return object : RegistrationConsumer<T, String> {
                    override fun <V : T> register(
                        id: String,
                        value: () -> V
                    ): RegisteredObject<V> {
                        val id = NeoIdentifier(namespace, id)
                        return RegisteredObjectImpl(
                            key as NeoResourceKey<Registry<V>>,
                            id,
                            Registry.register(
                                registry,
                                id.mojang,
                                value()
                            )
                        )
                    }
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun beginBlocks(): RegistrationConsumer.Blocks<NeoIdentifier> {
                val registry = BuiltInRegistries.BLOCK

                return object : RegistrationConsumer.Blocks<NeoIdentifier> {
                    override fun <V : Block> register(
                        id: NeoIdentifier,
                        value: (properties: BlockBehaviour.Properties) -> V
                    ): RegisteredObject<V> {
                        return RegisteredObjectImpl(
                            Registries.BLOCK.neo as NeoResourceKey<Registry<V>>,
                            id,
                            Registry.register(
                                registry,
                                id.mojang,
                                //? if <1.21.2 {
                                value(BlockBehaviour.Properties.of())
                                //? } else {
                                /*value(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id.mojang)))
                                *///? }
                            )
                        )
                    }
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun beginBlocks(namespace: String): RegistrationConsumer.Blocks<String> {
                val registry = BuiltInRegistries.BLOCK

                return object : RegistrationConsumer.Blocks<String> {
                    override fun <V : Block> register(
                        id: String,
                        value: (properties: BlockBehaviour.Properties) -> V
                    ): RegisteredObject<V> {
                        val id = NeoIdentifier(namespace, id)
                        return RegisteredObjectImpl(
                            Registries.BLOCK.neo as NeoResourceKey<Registry<V>>,
                            id,
                            Registry.register(
                                registry,
                                id.mojang,
                                //? if <1.21.2 {
                                value(BlockBehaviour.Properties.of())
                                //? } else {
                                /*value(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id.mojang)))
                                *///? }
                            )
                        )
                    }
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun beginItems(): RegistrationConsumer.Items<NeoIdentifier> {
                val registry = BuiltInRegistries.ITEM

                return object : RegistrationConsumer.Items<NeoIdentifier> {
                    override fun <V : Item> register(
                        id: NeoIdentifier,
                        value: (properties: Item.Properties) -> V
                    ): RegisteredObject<V> {
                        return RegisteredObjectImpl(
                            Registries.ITEM.neo as NeoResourceKey<Registry<V>>,
                            id,
                            Registry.register(
                                registry,
                                id.mojang,
                                //? if <1.21.2 {
                                value(Item.Properties())
                                //? } else {
                                /*value(Item.Properties().setId(ResourceKey.create(Registries.ITEM, id.mojang)))
                                *///? }
                            )
                        )
                    }

                    override fun registerBlockItem(
                        id: NeoIdentifier,
                        block: () -> Block,
                        properties: (properties: Item.Properties) -> Item.Properties
                    ): RegisteredObject<BlockItem> {
                        return register(id) { BlockItem(block(), properties(it)) }
                    }
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun beginItems(namespace: String): RegistrationConsumer.Items<String> {
                val registry = BuiltInRegistries.ITEM

                return object : RegistrationConsumer.Items<String> {
                    override fun <V : Item> register(
                        id: String,
                        value: (properties: Item.Properties) -> V
                    ): RegisteredObject<V> {
                        val id = NeoIdentifier(namespace, id)
                        return RegisteredObjectImpl(
                            Registries.ITEM.neo as NeoResourceKey<Registry<V>>,
                            id,
                            Registry.register(
                                registry,
                                id.mojang,
                                //? if <1.21.2 {
                                value(Item.Properties())
                                //? } else {
                                /*value(Item.Properties().setId(ResourceKey.create(Registries.ITEM, id.mojang)))
                                *///? }
                            )
                        )
                    }

                    override fun registerBlockItem(
                        id: String,
                        block: () -> Block,
                        properties: (properties: Item.Properties) -> Item.Properties
                    ): RegisteredObject<BlockItem> {
                        return register(id) { BlockItem(block(), properties(it)) }
                    }
                }
            }
        })
        *///? } neoforge {
        NeoForge.EVENT_BUS.addListener { event: NewRegistryEvent ->
            BigShotCommonEntrypoint.registerRegistries(object : RegistryFactory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : Any> create(
                    location: NeoIdentifier,
                    lifecycle: Lifecycle,
                    isIntrusive: Boolean
                ): NeoRegistry<T> {
                    val registry = MappedRegistry<T>(ResourceKey.createRegistryKey(location.mojang), lifecycle)
                    event.register(registry)
                    return WrapperUtil.INSTANCE.wrap(registry)
                }

                @Suppress("UNCHECKED_CAST")
                override fun <T : Any> createDefaulted(
                    location: NeoIdentifier,
                    defaultKey: NeoIdentifier,
                    lifecycle: Lifecycle,
                    isIntrusive: Boolean
                ): NeoRegistry<T> {
                    val registry = DefaultedMappedRegistry<T>(
                        location.toString(),
                        ResourceKey.createRegistryKey(location.mojang),
                        lifecycle,
                        false
                    )
                    event.register(registry)
                    return WrapperUtil.INSTANCE.wrap(registry)
                }
            })
        }
        NeoForge.EVENT_BUS.addListener { event: RegisterEvent ->
            BigShotCommonEntrypoint.registerContent(object : RegistrationFactory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : Any> begin(key: NeoResourceKey<Registry<T>>): RegistrationConsumer<T, NeoIdentifier> {
                    val registry = BuiltInRegistries.REGISTRY.get(key.location.mojang) as Registry<T>

                    return object : RegistrationConsumer<T, NeoIdentifier> {
                        override fun <V : T> register(
                            id: NeoIdentifier,
                            value: () -> V
                        ): RegisteredObject<V> {
                            return RegisteredObjectImpl(
                                key as NeoResourceKey<Registry<V>>,
                                id,
                                Registry.register(
                                    registry,
                                    id.mojang,
                                    value()
                                )
                            )
                        }
                    }
                }

                @Suppress("UNCHECKED_CAST")
                override fun <T : Any> begin(
                    key: NeoResourceKey<Registry<T>>,
                    namespace: String
                ): RegistrationConsumer<T, String> {
                    val registry = BuiltInRegistries.REGISTRY.get(key.location.mojang) as Registry<T>

                    return object : RegistrationConsumer<T, String> {
                        override fun <V : T> register(
                            id: String,
                            value: () -> V
                        ): RegisteredObject<V> {
                            val id = NeoIdentifier(namespace, id)
                            return RegisteredObjectImpl(
                                key as NeoResourceKey<Registry<V>>,
                                id,
                                Registry.register(
                                    registry,
                                    id.mojang,
                                    value()
                                )
                            )
                        }
                    }
                }

                @Suppress("UNCHECKED_CAST")
                override fun beginBlocks(): RegistrationConsumer.Blocks<NeoIdentifier> {
                    val registry = BuiltInRegistries.BLOCK

                    return object : RegistrationConsumer.Blocks<NeoIdentifier> {
                        override fun <V : Block> register(
                            id: NeoIdentifier,
                            value: (properties: BlockBehaviour.Properties) -> V
                        ): RegisteredObject<V> {
                            return RegisteredObjectImpl(
                                Registries.BLOCK.neo as NeoResourceKey<Registry<V>>,
                                id,
                                Registry.register(
                                    registry,
                                    id.mojang,
                                    //? if <1.21.2 {
                                    value(BlockBehaviour.Properties.of())
                                    //? } else {
                                    /*value(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id.mojang)))
                                    *///? }
                                )
                            )
                        }
                    }
                }

                @Suppress("UNCHECKED_CAST")
                override fun beginBlocks(namespace: String): RegistrationConsumer.Blocks<String> {
                    val registry = BuiltInRegistries.BLOCK

                    return object : RegistrationConsumer.Blocks<String> {
                        override fun <V : Block> register(
                            id: String,
                            value: (properties: BlockBehaviour.Properties) -> V
                        ): RegisteredObject<V> {
                            val id = NeoIdentifier(namespace, id)
                            return RegisteredObjectImpl(
                                Registries.BLOCK.neo as NeoResourceKey<Registry<V>>,
                                id,
                                Registry.register(
                                    registry,
                                    id.mojang,
                                    //? if <1.21.2 {
                                    value(BlockBehaviour.Properties.of())
                                    //? } else {
                                    /*value(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id.mojang)))
                                    *///? }
                                )
                            )
                        }
                    }
                }

                @Suppress("UNCHECKED_CAST")
                override fun beginItems(): RegistrationConsumer.Items<NeoIdentifier> {
                    val registry = BuiltInRegistries.ITEM

                    return object : RegistrationConsumer.Items<NeoIdentifier> {
                        override fun <V : Item> register(
                            id: NeoIdentifier,
                            value: (properties: Item.Properties) -> V
                        ): RegisteredObject<V> {
                            return RegisteredObjectImpl(
                                Registries.ITEM.neo as NeoResourceKey<Registry<V>>,
                                id,
                                Registry.register(
                                    registry,
                                    id.mojang,
                                    //? if <1.21.2 {
                                    value(Item.Properties())
                                    //? } else {
                                    /*value(Item.Properties().setId(ResourceKey.create(Registries.ITEM, id.mojang)))
                                    *///? }
                                )
                            )
                        }

                        override fun registerBlockItem(
                            id: NeoIdentifier,
                            block: () -> Block,
                            properties: (properties: Item.Properties) -> Item.Properties
                        ): RegisteredObject<BlockItem> {
                            return register(id) { BlockItem(block(), properties(it)) }
                        }
                    }
                }

                @Suppress("UNCHECKED_CAST")
                override fun beginItems(namespace: String): RegistrationConsumer.Items<String> {
                    val registry = BuiltInRegistries.ITEM

                    return object : RegistrationConsumer.Items<String> {
                        override fun <V : Item> register(
                            id: String,
                            value: (properties: Item.Properties) -> V
                        ): RegisteredObject<V> {
                            val id = NeoIdentifier(namespace, id)
                            return RegisteredObjectImpl(
                                Registries.ITEM.neo as NeoResourceKey<Registry<V>>,
                                id,
                                Registry.register(
                                    registry,
                                    id.mojang,
                                    //? if <1.21.2 {
                                    value(Item.Properties())
                                    //? } else {
                                    /*value(Item.Properties().setId(ResourceKey.create(Registries.ITEM, id.mojang)))
                                    *///? }
                                )
                            )
                        }

                        override fun registerBlockItem(
                            id: String,
                            block: () -> Block,
                            properties: (properties: Item.Properties) -> Item.Properties
                        ): RegisteredObject<BlockItem> {
                            return register(id) { BlockItem(block(), properties(it)) }
                        }
                    }
                }
            })
        }
        //? }
    }

    @JvmStatic
    internal fun init() = Unit
}