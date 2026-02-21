package net.typho.big_shot_lib

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.KeyMapping
import net.minecraft.core.Registry
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
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.registration.BigShotClientRegistrationEntrypoint
import net.typho.big_shot_lib.api.client.registration.KeyMappingCategory
import net.typho.big_shot_lib.api.client.registration.KeyMappingFactory
import net.typho.big_shot_lib.api.client.registration.ResourceListenerFactory
import net.typho.big_shot_lib.api.registration.BigShotCommonRegistrationEntrypoint
import net.typho.big_shot_lib.api.registration.RegistrationConsumer
import net.typho.big_shot_lib.api.registration.RegistrationFactory
import net.typho.big_shot_lib.api.registration.RegistryFactory
import net.typho.big_shot_lib.api.services.NeoResourceManagerReloadListener
import net.typho.big_shot_lib.api.services.WrapperUtil
import net.typho.big_shot_lib.api.util.resources.NeoResourceKey
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import net.typho.big_shot_lib.api.util.resources.ResourceRegistry
import net.typho.big_shot_lib.impl.registration.KeyMappingCategoryImpl
import java.util.function.Function
import java.util.function.Supplier
import java.util.function.UnaryOperator

@Mod(BigShotApi.MOD_ID)
@OnlyIn(Dist.CLIENT)
class BigShotLibNeoForge(eventBus: IEventBus, modContainer: ModContainer) {
    init {
        BigShotLib.init()
        eventBus.addListener { event: AddClientReloadListenersEvent ->
            BigShotClientRegistrationEntrypoint.registerReloadListeners(object : ResourceListenerFactory {
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
            BigShotClientRegistrationEntrypoint.registerKeyMappings(object : KeyMappingFactory {
                val categories = HashMap<ResourceIdentifier, KeyMappingCategory>()

                override fun getOrCreateCategory(id: ResourceIdentifier): KeyMappingCategory {
                    return categories.computeIfAbsent(id, ::KeyMappingCategoryImpl)
                }

                override fun create(
                    name: String,
                    key: Int,
                    category: KeyMappingCategory
                ): KeyMapping {
                    val mapping = KeyMapping(name, key, (category as KeyMappingCategoryImpl).label)
                    event.register(mapping)
                    return mapping
                }

                override fun create(
                    name: String,
                    type: InputConstants.Type,
                    key: Int,
                    category: KeyMappingCategory
                ): KeyMapping {
                    val mapping = KeyMapping(name, type, key, (category as KeyMappingCategoryImpl).label)
                    event.register(mapping)
                    return mapping
                }
            })
        }
        eventBus.addListener { event: NewRegistryEvent ->
            BigShotCommonRegistrationEntrypoint.registerRegistries(object : RegistryFactory {
                override fun <T> register(registry: Registry<T>): Registry<T> {
                    event.register(registry)
                    return registry
                }
            })
        }
        eventBus.addListener { event: RegisterEvent ->
            BigShotCommonRegistrationEntrypoint.registerContent(object : RegistrationFactory {
                override fun <T> begin(
                    registry: ResourceKey<T>,
                    namespace: String
                ): RegistrationConsumer<T> {
                    val deferred: DeferredRegister<T> = DeferredRegister.create(registry, namespace)
                    deferred.register(eventBus)
                    return object : RegistrationConsumer<T> {
                        override fun <V : T> register(
                            id: String,
                            value: Supplier<V>
                        ): Supplier<V> {
                            return deferred.register(id, value)
                        }
                    }
                }

                override fun <T> begin(
                    registry: NeoResourceKey<T>,
                    namespace: String
                ): RegistrationConsumer<T> {
                    return begin(registry.toMojang(), namespace)
                }

                override fun beginBlocks(namespace: String): RegistrationConsumer.Blocks {
                    val deferred = DeferredRegister.createBlocks(namespace)
                    deferred.register(eventBus)
                    return object : RegistrationConsumer.Blocks {
                        override fun <V : Block> register(
                            id: String,
                            value: Function<BlockBehaviour.Properties, V>
                        ): Supplier<V> {
                            return deferred.registerBlock(id, value)
                        }
                    }
                }

                override fun beginItems(namespace: String): RegistrationConsumer.Items {
                    val deferred = DeferredRegister.createItems(namespace)
                    deferred.register(eventBus)
                    return object : RegistrationConsumer.Items {
                        override fun <V : Item> register(
                            id: String,
                            value: Function<Item.Properties, V>
                        ): Supplier<V> {
                            return deferred.registerItem(id, value)
                        }

                        override fun registerBlockItem(
                            id: String,
                            block: Supplier<out Block>,
                            properties: UnaryOperator<Item.Properties>
                        ): Supplier<BlockItem> {
                            return deferred.registerSimpleBlockItem(id, block, properties.apply(Item.Properties()))
                        }
                    }
                }
            })
        }
    }
}