package net.typho.big_shot_lib

import com.mojang.blaze3d.platform.InputConstants
import com.mojang.serialization.Lifecycle
import net.minecraft.client.KeyMapping
import net.minecraft.core.DefaultedMappedRegistry
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.ResourceManagerReloadListener
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.RegisterEvent
import net.typho.big_shot_lib.BigShotLib.toMojang
import net.typho.big_shot_lib.BigShotLib.toNeo
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint
import net.typho.big_shot_lib.api.client.util.KeyMappingCategory
import net.typho.big_shot_lib.api.client.util.KeyMappingFactory
import net.typho.big_shot_lib.api.client.util.ResourceListenerFactory
import net.typho.big_shot_lib.api.util.*
import net.typho.big_shot_lib.api.util.resources.NeoResourceKey
import net.typho.big_shot_lib.api.util.resources.NeoResourceManagerReloadListener
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import net.typho.big_shot_lib.api.util.resources.ResourceRegistry
import net.typho.big_shot_lib.impl.registration.KeyMappingCategoryImpl
import net.typho.big_shot_lib.impl.registration.RegisteredObjectImpl
import net.typho.big_shot_lib.mixin.KeyMappingCategoryAccessor
import java.util.function.Function
import java.util.function.Supplier
import java.util.function.UnaryOperator

@Mod(BigShotApi.MOD_ID)
@OnlyIn(Dist.CLIENT)
class BigShotLibNeoForge(eventBus: IEventBus, modContainer: ModContainer) {
    init {
        BigShotLib.init()
        eventBus.addListener { event: AddClientReloadListenersEvent ->
            BigShotClientEntrypoint.registerReloadListeners(object : ResourceListenerFactory {
                override fun register(
                    id: ResourceIdentifier,
                    listener: PreparableReloadListener
                ) {
                    event.addListener(id.toMojang(), listener)
                }

                override fun register(
                    id: ResourceIdentifier,
                    listener: NeoResourceManagerReloadListener
                ) {
                    event.addListener(id.toMojang(), object : ResourceManagerReloadListener {
                        override fun onResourceManagerReload(resourceManager: ResourceManager) {
                            listener.onResourceManagerReload(WrapperUtil.INSTANCE.wrap(resourceManager))
                        }
                    })
                }

                override fun register(registry: ResourceRegistry<*>) {
                    register(registry.name, registry)
                }
            })
        }
        eventBus.addListener { event: RegisterKeyMappingsEvent ->
            BigShotClientEntrypoint.registerKeyMappings(object : KeyMappingFactory {
                override fun getOrCreateCategory(id: ResourceIdentifier): KeyMappingCategory {
                    val category = KeyMapping.Category(id.toMojang())
                    val list = KeyMappingCategoryAccessor.getSortOrder()

                    if (!list.contains(category)) {
                        list.add(category)
                    }

                    return KeyMappingCategoryImpl(category)
                }

                override fun create(
                    id: ResourceIdentifier,
                    key: Int,
                    category: KeyMappingCategory
                ): KeyMapping {
                    val mapping = KeyMapping("key.${id.toShortLanguageKey()}", key, (category as KeyMappingCategoryImpl).inner)
                    event.register(mapping)
                    return mapping
                }

                override fun create(
                    id: ResourceIdentifier,
                    type: InputConstants.Type,
                    key: Int,
                    category: KeyMappingCategory
                ): KeyMapping {
                    val mapping = KeyMapping("key.${id.toShortLanguageKey()}", type, key, (category as KeyMappingCategoryImpl).inner)
                    event.register(mapping)
                    return mapping
                }
            })
        }
        eventBus.addListener { event: NewRegistryEvent ->
            BigShotCommonEntrypoint.registerRegistries(object : RegistryFactory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : Any> create(
                    id: ResourceIdentifier,
                    lifecycle: Lifecycle,
                    isIntrusive: Boolean
                ): NeoRegistry<T> {
                    val registry = MappedRegistry<T>(ResourceKey.createRegistryKey(id.toMojang()), lifecycle)
                    event.register(registry)
                    return WrapperUtil.INSTANCE.wrap(registry)
                }

                @Suppress("UNCHECKED_CAST")
                override fun <T : Any> createDefaulted(
                    id: ResourceIdentifier,
                    defaultKey: ResourceIdentifier,
                    lifecycle: Lifecycle,
                    isIntrusive: Boolean
                ): NeoRegistry<T> {
                    val registry = DefaultedMappedRegistry<T>(id.toString(), ResourceKey.createRegistryKey(id.toMojang()), lifecycle, false)
                    event.register(registry)
                    return WrapperUtil.INSTANCE.wrap(registry)
                }
            })
        }
        eventBus.addListener { event: RegisterEvent ->
            BigShotCommonEntrypoint.registerContent(object : RegistrationFactory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : Any> begin(
                    key: ResourceKey<Registry<T>>,
                    namespace: String
                ): RegistrationConsumer<T> {
                    val registry: DeferredRegister<T> = DeferredRegister.create(key, namespace)
                    registry.register(eventBus)
                    return object : RegistrationConsumer<T> {
                        override fun <V : T> register(
                            id: String,
                            value: Supplier<V>
                        ): RegisteredObject<V> {
                            return RegisteredObjectImpl(
                                key.toNeo() as NeoResourceKey<Registry<V>>,
                                ResourceIdentifier(namespace, id),
                                registry.register(id, value)
                            )
                        }
                    }
                }

                override fun <T : Any> begin(
                    key: NeoResourceKey<Registry<T>>,
                    namespace: String
                ): RegistrationConsumer<T> {
                    return begin(key.toMojang(), namespace)
                }

                @Suppress("UNCHECKED_CAST")
                override fun beginBlocks(namespace: String): RegistrationConsumer.Blocks {
                    val registry = DeferredRegister.createBlocks(namespace)
                    registry.register(eventBus)
                    return object : RegistrationConsumer.Blocks {
                        override fun <V : Block> register(
                            id: String,
                            value: Function<BlockBehaviour.Properties, V>
                        ): RegisteredObject<V> {
                            return RegisteredObjectImpl(
                                Registries.BLOCK.toNeo() as NeoResourceKey<Registry<V>>,
                                ResourceIdentifier(namespace, id),
                                registry.registerBlock(id, value)
                            )
                        }
                    }
                }

                @Suppress("UNCHECKED_CAST")
                override fun beginItems(namespace: String): RegistrationConsumer.Items {
                    val registry = DeferredRegister.createItems(namespace)
                    registry.register(eventBus)
                    return object : RegistrationConsumer.Items {
                        override fun <V : Item> register(
                            id: String,
                            value: Function<Item.Properties, V>
                        ): RegisteredObject<V> {
                            return RegisteredObjectImpl(
                                Registries.ITEM.toNeo() as NeoResourceKey<Registry<V>>,
                                ResourceIdentifier(namespace, id),
                                registry.registerItem(id, value)
                            )
                        }

                        override fun registerBlockItem(
                            id: String,
                            block: Supplier<out Block>,
                            properties: UnaryOperator<Item.Properties>
                        ): RegisteredObject<BlockItem> {
                            return RegisteredObjectImpl(
                                Registries.ITEM.toNeo() as NeoResourceKey<Registry<BlockItem>>,
                                ResourceIdentifier(namespace, id),
                                registry.registerSimpleBlockItem(id, block, properties)
                            )
                        }
                    }
                }
            })
        }
    }
}