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
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.function.Function
import java.util.function.Supplier
import java.util.function.UnaryOperator

object BigShotLibFabric : ClientModInitializer {
    override fun onInitializeClient() {
        BigShotClientEntrypoint.registerReloadListeners(object : ResourceListenerFactory {
            override fun register(
                location: ResourceIdentifier,
                listener: PreparableReloadListener
            ) {
                ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(object : IdentifiableResourceReloadListener {
                    override fun getFabricId(): ResourceLocation {
                        return location.toMojang()
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
                location: ResourceIdentifier,
                listener: NeoResourceManagerReloadListener
            ) {
                ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(object : SimpleSynchronousResourceReloadListener {
                    override fun getFabricId(): ResourceLocation {
                        return location.toMojang()
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
        BigShotClientEntrypoint.registerKeyMappings(object : KeyMappingFactory {
            val categories = HashMap<ResourceIdentifier, KeyMappingCategory>()

            override fun getOrCreateCategory(location: ResourceIdentifier): KeyMappingCategory {
                return categories.computeIfAbsent(location, ::KeyMappingCategoryImpl)
            }

            override fun create(
                location: ResourceIdentifier,
                key: Int,
                category: KeyMappingCategory
            ): KeyMapping {
                val mapping = KeyMapping("key.${location.toShortLanguageKey()}", key, (category as KeyMappingCategoryImpl).label)
                KeyBindingHelper.registerKeyBinding(mapping)
                return mapping
            }

            override fun create(
                location: ResourceIdentifier,
                type: InputConstants.Type,
                key: Int,
                category: KeyMappingCategory
            ): KeyMapping {
                val mapping = KeyMapping("key.${location.toShortLanguageKey()}", type, key, (category as KeyMappingCategoryImpl).label)
                KeyBindingHelper.registerKeyBinding(mapping)
                return mapping
            }
        })
        BigShotCommonEntrypoint.registerRegistries(object : RegistryFactory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : Any> create(
                location: ResourceIdentifier,
                lifecycle: Lifecycle,
                isIntrusive: Boolean
            ): NeoRegistry<T> {
                return WrapperUtil.INSTANCE.wrap(Registry.register(
                    BuiltInRegistries.REGISTRY as Registry<Registry<T>>,
                    location.toMojang(),
                    MappedRegistry<T>(ResourceKey.createRegistryKey(location.toMojang()), lifecycle, isIntrusive)
                ))
            }

            @Suppress("UNCHECKED_CAST")
            override fun <T : Any> createDefaulted(
                location: ResourceIdentifier,
                defaultKey: ResourceIdentifier,
                lifecycle: Lifecycle,
                isIntrusive: Boolean
            ): NeoRegistry<T> {
                return WrapperUtil.INSTANCE.wrap(Registry.register(
                    BuiltInRegistries.REGISTRY as Registry<Registry<T>>,
                    location.toMojang(),
                    DefaultedMappedRegistry<T>(location.toString(), ResourceKey.createRegistryKey(location.toMojang()), lifecycle, isIntrusive)
                ))
            }
        })
        BigShotCommonEntrypoint.registerContent(object : RegistrationFactory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : Any> begin(
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
                                value.get()
                            )
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