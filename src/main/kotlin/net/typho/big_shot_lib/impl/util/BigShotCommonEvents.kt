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
import com.mojang.serialization.Lifecycle
import net.minecraft.core.DefaultedMappedRegistry
import net.minecraft.core.MappedRegistry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.RegisterEvent
import net.typho.big_shot_lib.api.util.NeoRegistry
import net.typho.big_shot_lib.api.util.RegistrationConsumer
import net.typho.big_shot_lib.api.util.RegistrationFactory
import net.typho.big_shot_lib.api.util.RegistryFactory
import net.typho.big_shot_lib.api.util.WrapperUtil
import net.typho.big_shot_lib.impl.mojang
import net.typho.big_shot_lib.impl.neo
//? }

import net.minecraft.core.Registry
import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.util.BigShotCommonEntrypoint
import net.typho.big_shot_lib.api.util.RegisteredObject
import net.typho.big_shot_lib.api.util.event.BlockChangedEvent
import net.typho.big_shot_lib.api.util.event.CommonEventFactory
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import net.typho.big_shot_lib.api.util.resource.NeoResourceKey

//? neoforge {
@EventBusSubscriber
//? }
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

    internal fun init() {
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
                val registry = InternalUtil.INSTANCE.getRegistry(key)!!

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
                val registry = InternalUtil.INSTANCE.getRegistry(key)!!

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
        *///? }
    }

    //? neoforge {
    @SubscribeEvent
    @JvmStatic
    fun newRegistries(event: NewRegistryEvent) {
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

    @SubscribeEvent
    @JvmStatic
    fun register(event: RegisterEvent) {
        BigShotCommonEntrypoint.registerContent(object : RegistrationFactory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : Any> begin(key: NeoResourceKey<Registry<T>>): RegistrationConsumer<T, NeoIdentifier>? {
                if (event.registryKey != key.mojang) {
                    return null
                }

                return object : RegistrationConsumer<T, NeoIdentifier> {
                    override fun <V : T> register(
                        id: NeoIdentifier,
                        value: () -> V
                    ): RegisteredObject<V> {
                        return RegisteredObjectImpl(
                            key as NeoResourceKey<Registry<V>>,
                            id,
                            value().also {
                                event.register(
                                    key.mojang,
                                    id.mojang,
                                    { it }
                                )
                            }
                        )
                    }
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun <T : Any> begin(
                key: NeoResourceKey<Registry<T>>,
                namespace: String
            ): RegistrationConsumer<T, String>? {
                if (event.registryKey != key.mojang) {
                    return null
                }

                return object : RegistrationConsumer<T, String> {
                    override fun <V : T> register(
                        id: String,
                        value: () -> V
                    ): RegisteredObject<V> {
                        val id = NeoIdentifier(namespace, id)
                        return RegisteredObjectImpl(
                            key as NeoResourceKey<Registry<V>>,
                            id,
                            value().also {
                                event.register(
                                    key.mojang,
                                    id.mojang,
                                    { it }
                                )
                            }
                        )
                    }
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun beginBlocks(): RegistrationConsumer.Blocks<NeoIdentifier>? {
                if (event.registryKey != Registries.BLOCK) {
                    return null
                }

                return object : RegistrationConsumer.Blocks<NeoIdentifier> {
                    override fun <V : Block> register(
                        id: NeoIdentifier,
                        value: (properties: BlockBehaviour.Properties) -> V
                    ): RegisteredObject<V> {
                        return RegisteredObjectImpl(
                            Registries.BLOCK.neo as NeoResourceKey<Registry<V>>,
                            id,
                            //? if <1.21.2 {
                            value(BlockBehaviour.Properties.of())
                                //? } else {
                            /*value(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id.mojang)))
                                *///? }
                                .also {
                                    event.register(
                                        Registries.BLOCK,
                                        id.mojang,
                                        { it }
                                    )
                                }
                        )
                    }
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun beginBlocks(namespace: String): RegistrationConsumer.Blocks<String>? {
                if (event.registryKey != Registries.BLOCK) {
                    return null
                }

                return object : RegistrationConsumer.Blocks<String> {
                    override fun <V : Block> register(
                        id: String,
                        value: (properties: BlockBehaviour.Properties) -> V
                    ): RegisteredObject<V> {
                        val id = NeoIdentifier(namespace, id)
                        return RegisteredObjectImpl(
                            Registries.BLOCK.neo as NeoResourceKey<Registry<V>>,
                            id,
                            //? if <1.21.2 {
                            value(BlockBehaviour.Properties.of())
                                //? } else {
                            /*value(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id.mojang)))
                                *///? }
                                .also {
                                    event.register(
                                        Registries.BLOCK,
                                        id.mojang,
                                        { it }
                                    )
                                }
                        )
                    }
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun beginItems(): RegistrationConsumer.Items<NeoIdentifier>? {
                if (event.registryKey != Registries.ITEM) {
                    return null
                }

                return object : RegistrationConsumer.Items<NeoIdentifier> {
                    override fun <V : Item> register(
                        id: NeoIdentifier,
                        value: (properties: Item.Properties) -> V
                    ): RegisteredObject<V> {
                        return RegisteredObjectImpl(
                            Registries.ITEM.neo as NeoResourceKey<Registry<V>>,
                            id,
                            //? if <1.21.2 {
                            value(Item.Properties())
                                //? } else {
                            /*value(Item.Properties().setId(ResourceKey.create(Registries.ITEM, id.mojang)))
                                *///? }
                                .also {
                                    event.register(
                                        Registries.ITEM,
                                        id.mojang,
                                        { it }
                                    )
                                }
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
            override fun beginItems(namespace: String): RegistrationConsumer.Items<String>? {
                if (event.registryKey != Registries.ITEM) {
                    return null
                }

                return object : RegistrationConsumer.Items<String> {
                    override fun <V : Item> register(
                        id: String,
                        value: (properties: Item.Properties) -> V
                    ): RegisteredObject<V> {
                        val id = NeoIdentifier(namespace, id)
                        return RegisteredObjectImpl(
                            Registries.ITEM.neo as NeoResourceKey<Registry<V>>,
                            id,
                            //? if <1.21.2 {
                            value(Item.Properties())
                                //? } else {
                            /*value(Item.Properties().setId(ResourceKey.create(Registries.ITEM, id.mojang)))
                                *///? }
                                .also {
                                    event.register(
                                        Registries.ITEM,
                                        id.mojang,
                                        { it }
                                    )
                                }
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