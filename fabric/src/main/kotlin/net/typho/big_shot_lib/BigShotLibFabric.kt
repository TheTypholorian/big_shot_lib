package net.typho.big_shot_lib

import com.mojang.blaze3d.platform.InputConstants
import com.mojang.serialization.Lifecycle
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.client.KeyMapping
import net.minecraft.core.DefaultedMappedRegistry
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.typho.big_shot_lib.BigShotLib.toMojang
import net.typho.big_shot_lib.BigShotLib.toNeo
import net.typho.big_shot_lib.api.client.registration.BigShotClientRegistrationEntrypoint
import net.typho.big_shot_lib.api.client.registration.KeyMappingCategory
import net.typho.big_shot_lib.api.client.registration.KeyMappingFactory
import net.typho.big_shot_lib.api.client.registration.ResourceListenerFactory
import net.typho.big_shot_lib.api.registration.*
import net.typho.big_shot_lib.api.services.NeoResourceManagerReloadListener
import net.typho.big_shot_lib.api.services.WrapperUtil
import net.typho.big_shot_lib.api.util.resources.NeoResourceKey
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import net.typho.big_shot_lib.api.util.resources.ResourceRegistry
import net.typho.big_shot_lib.impl.registration.KeyMappingCategoryImpl
import net.typho.big_shot_lib.impl.registration.RegisteredObjectImpl
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.function.Function
import java.util.function.Supplier
import java.util.function.UnaryOperator

object BigShotLibFabric : ClientModInitializer {
    override fun onInitializeClient() {
        BigShotLib.init()
        BigShotClientRegistrationEntrypoint.registerReloadListeners(object : ResourceListenerFactory {
            override fun register(
                id: ResourceIdentifier,
                listener: PreparableReloadListener
            ) {
                ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(object : IdentifiableResourceReloadListener {
                    override fun getFabricId(): ResourceLocation {
                        return id.toMojang()
                    }

                    override fun reload(
                        preparationBarrier: PreparableReloadListener.PreparationBarrier,
                        resourceManager: ResourceManager,
                        backgroundExecutor: Executor,
                        gameExecutor: Executor
                    ): CompletableFuture<Void> {
                        return listener.reload(
                            preparationBarrier,
                            resourceManager,
                            backgroundExecutor,
                            gameExecutor
                        )
                    }
                })
            }

            override fun register(
                id: ResourceIdentifier,
                listener: NeoResourceManagerReloadListener
            ) {
                ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(object : SimpleSynchronousResourceReloadListener {
                    override fun getFabricId(): ResourceLocation {
                        return id.toMojang()
                    }

                    override fun onResourceManagerReload(resourceManager: ResourceManager) {
                        listener.onResourceManagerReload(WrapperUtil.INSTANCE.wrap(resourceManager))
                    }
                })
            }

            override fun register(registry: ResourceRegistry<*>) {
                register(registry.name, registry)
            }
        })
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
                KeyBindingHelper.registerKeyBinding(mapping)
                return mapping
            }

            override fun create(
                name: String,
                type: InputConstants.Type,
                key: Int,
                category: KeyMappingCategory
            ): KeyMapping {
                val mapping = KeyMapping(name, type, key, (category as KeyMappingCategoryImpl).label)
                KeyBindingHelper.registerKeyBinding(mapping)
                return mapping
            }
        })
        BigShotCommonRegistrationEntrypoint.registerRegistries(object : RegistryFactory {
            @Suppress("UNCHECKED_CAST")
            override fun <T> create(
                id: ResourceIdentifier,
                lifecycle: Lifecycle
            ): Registry<T> {
                return Registry.register(
                    BuiltInRegistries.REGISTRY as Registry<Registry<T>>,
                    id.toMojang(),
                    MappedRegistry<T>(ResourceKey.createRegistryKey(id.toMojang()), lifecycle)
                )
            }

            @Suppress("UNCHECKED_CAST")
            override fun <T> createDefaulted(
                id: ResourceIdentifier,
                defaultKey: ResourceIdentifier,
                lifecycle: Lifecycle
            ): Registry<T> {
                return Registry.register(
                    BuiltInRegistries.REGISTRY as Registry<Registry<T>>,
                    id.toMojang(),
                    DefaultedMappedRegistry<T>(id.toString(), ResourceKey.createRegistryKey(id.toMojang()), lifecycle, false)
                )
            }
        })
        BigShotCommonRegistrationEntrypoint.registerContent(object : RegistrationFactory {
            @Suppress("UNCHECKED_CAST")
            override fun <T> begin(
                key: ResourceKey<Registry<T>>,
                namespace: String
            ): RegistrationConsumer<T> {
                val registry = BuiltInRegistries.REGISTRY.get(key.location()).orElseThrow().value() as Registry<T>

                return object : RegistrationConsumer<T> {
                    override fun <V : T> register(
                        id: String,
                        value: Supplier<V>
                    ): RegisteredObject<V> {
                        val id = ResourceIdentifier(namespace, id)
                        return RegisteredObjectImpl(
                            key.toNeo() as NeoResourceKey<Registry<V>>,
                            id,
                            Registry.register(
                                registry,
                                id.toMojang(),
                                value.get()!!
                            )
                        )
                    }
                }
            }

            override fun <T> begin(
                key: NeoResourceKey<Registry<T>>,
                namespace: String
            ): RegistrationConsumer<T> {
                return begin(key.toMojang(), namespace)
            }

            @Suppress("UNCHECKED_CAST")
            override fun beginBlocks(namespace: String): RegistrationConsumer.Blocks {
                val registry = BuiltInRegistries.BLOCK

                return object : RegistrationConsumer.Blocks {
                    override fun <V : Block> register(
                        id: String,
                        value: Function<BlockBehaviour.Properties, V>
                    ): RegisteredObject<V> {
                        val id = ResourceIdentifier(namespace, id)
                        return RegisteredObjectImpl(
                            Registries.BLOCK.toNeo() as NeoResourceKey<Registry<V>>,
                            id,
                            Registry.register(
                                registry,
                                id.toMojang(),
                                value.apply(BlockBehaviour.Properties.of())
                            )
                        )
                    }
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun beginItems(namespace: String): RegistrationConsumer.Items {
                val registry = BuiltInRegistries.ITEM

                return object : RegistrationConsumer.Items {
                    override fun <V : Item> register(
                        id: String,
                        value: Function<Item.Properties, V>
                    ): RegisteredObject<V> {
                        val id = ResourceIdentifier(namespace, id)
                        return RegisteredObjectImpl(
                            Registries.ITEM.toNeo() as NeoResourceKey<Registry<V>>,
                            id,
                            Registry.register(
                                registry,
                                id.toMojang(),
                                value.apply(Item.Properties())
                            )
                        )
                    }

                    override fun registerBlockItem(
                        id: String,
                        block: Supplier<out Block>,
                        properties: UnaryOperator<Item.Properties>
                    ): RegisteredObject<BlockItem> {
                        return register(id) { BlockItem(block.get(), properties.apply(it)) }
                    }
                }
            }
        })
    }
}