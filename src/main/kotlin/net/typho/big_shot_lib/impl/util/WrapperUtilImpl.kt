package net.typho.big_shot_lib.impl.util

import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceKey
import net.minecraft.server.packs.PackResources
import net.minecraft.server.packs.resources.Resource
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.tags.TagKey
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexConsumer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebuffer
import net.typho.big_shot_lib.api.client.rendering.util.MultiBufferSourceInjection
import net.typho.big_shot_lib.api.client.rendering.util.NeoMultiBufferSource
import net.typho.big_shot_lib.api.client.rendering.util.quad.NeoBakedQuad
import net.typho.big_shot_lib.api.client.util.resource.NeoResourceManager
import net.typho.big_shot_lib.api.util.NeoRegistry
import net.typho.big_shot_lib.api.util.NeoRegistryAccess
import net.typho.big_shot_lib.api.util.WrapperUtil
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import net.typho.big_shot_lib.api.util.resource.NeoResourceKey
import net.typho.big_shot_lib.api.util.resource.NeoTagKey
import net.typho.big_shot_lib.impl.client.rendering.util.NeoRenderSettingsImpl
import net.typho.big_shot_lib.impl.client.rendering.util.NeoVertexConsumerWrapper
import net.typho.big_shot_lib.impl.client.rendering.util.VertexConsumerWrapper
import net.typho.big_shot_lib.impl.mojang
import net.typho.big_shot_lib.impl.neo
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.jvm.optionals.getOrNull

object WrapperUtilImpl : WrapperUtil {
    override fun wrap(manager: ResourceManager): NeoResourceManager {
        return object : NeoResourceManager {
            override fun getNamespaces(): Set<String> {
                return manager.namespaces
            }

            override fun getResourceStack(location: NeoIdentifier): List<Resource> {
                return manager.getResourceStack(location.mojang)
            }

            override fun listResources(
                folder: String,
                predicate: (id: NeoIdentifier) -> Boolean
            ): Map<NeoIdentifier, Resource> {
                return manager.listResources(folder) { predicate(it.neo) }.mapKeys { (key, value) -> key.neo }
            }

            override fun listResourceStacks(
                folder: String,
                predicate: (id: NeoIdentifier) -> Boolean
            ): Map<NeoIdentifier, List<Resource>> {
                return manager.listResourceStacks(folder) { predicate(it.neo) }.mapKeys { (key, value) -> key.neo }
            }

            override fun listPacks(): Stream<PackResources> {
                return manager.listPacks()
            }

            override fun getResource(location: NeoIdentifier): Optional<Resource> {
                return manager.getResource(location.mojang)
            }
        }
    }

    override fun wrap(target: RenderTarget): GlFramebuffer {
        return target.getExtensionValue()
    }

    override fun <T : Any> wrap(registry: Registry<T>): NeoRegistry<T> {
        return object : NeoRegistry<T> {
            override val key: NeoResourceKey<out Registry<T>> = registry.key().neo

            override fun get(value: NeoIdentifier): T? {
                //? if <1.21.2 {
                return registry.get(value.mojang)
                //? } else {
                /*return registry.get(value.mojang).getOrNull()?.value()
                *///? }
            }

            override fun getKey(value: T): NeoResourceKey<T> {
                return registry.getResourceKey(value).orElseThrow().neo
            }

            override fun contains(value: NeoIdentifier): Boolean {
                return registry.containsKey(value.mojang)
            }

            override fun keys(): Set<NeoResourceKey<T>> {
                return registry.keySet().map { NeoResourceKey<T>(key.location, it.neo) }.toSet()
            }

            override fun entries(): Set<Pair<NeoResourceKey<T>, T>> {
                return registry.entrySet().map { entry -> entry.key.neo to entry.value }.toSet()
            }

            override fun values(): Collection<T> {
                return registry.entrySet().map { it.value }
            }

            override fun getTag(key: NeoTagKey<T>): Set<T>? {
                //? if <1.21.2 {
                return registry.getTag(key.mojang)
                    .map { set ->
                        set.stream()
                            .map { it.value() }
                            .collect(Collectors.toSet())
                    }
                    .getOrNull()
                //? } else {
                /*return if (registry.listTagIds().anyMatch { it.neo == key }) {
                    registry.getTagOrEmpty(key.mojang).toList()
                        .stream()
                        .map { it.value() }
                        .collect(Collectors.toSet())
                } else null
                *///? }
            }

            override fun tags(): Set<NeoTagKey<T>> {
                //? if <1.21.2 {
                return registry.tags.map { it.first.neo }.collect(Collectors.toSet())
                //? } else {
                /*return registry.listTagIds().map { it.neo }.collect(Collectors.toSet())
                *///? }
            }
        }
    }

    override fun wrap(access: RegistryAccess): NeoRegistryAccess {
        return object : NeoRegistryAccess {
            override fun <T : Any> registry(key: NeoResourceKey<Registry<T>>): NeoRegistry<T>? {
                //? if <1.21.2 {
                return access.registry(key.mojang).map { wrap(it) }.getOrNull()
                //? } else {
                /*return access.lookup(key.mojang).map { wrap(it) }.getOrNull()
                *///? }
            }
        }
    }

    override fun wrap(
        quad: BakedQuad
    ): NeoBakedQuad {
        return quad.getExtensionValue()
    }

    override fun wrap(consumer: VertexConsumer): NeoVertexConsumer {
        return NeoVertexConsumerWrapper(consumer)
    }

    override fun unwrap(consumer: NeoVertexConsumer): VertexConsumer {
        return VertexConsumerWrapper(consumer)
    }

    override fun <T : Any> wrap(key: ResourceKey<T>): NeoResourceKey<T> {
        return key.neo
    }

    override fun <T : Any> wrap(key: TagKey<T>): NeoTagKey<T> {
        return key.neo
    }

    override fun <T : Any> unwrap(key: NeoResourceKey<T>): ResourceKey<T> {
        return key.mojang
    }

    override fun <T : Any> unwrap(key: NeoTagKey<T>): TagKey<T> {
        return key.mojang
    }

    override fun inject(
        vanilla: MultiBufferSource.BufferSource,
        injection: MultiBufferSourceInjection
    ): MultiBufferSource.BufferSource {
        val injections = vanilla.getExtensionValue<MutableList<MultiBufferSourceInjection>>()
        injections.add(injection)
        return vanilla
    }

    override fun wrap(source: MultiBufferSource): NeoMultiBufferSource {
        return NeoMultiBufferSource { renderSettings -> wrap(source.getBuffer((renderSettings as NeoRenderSettingsImpl).renderType)) }
    }

    override fun unwrap(source: NeoMultiBufferSource): MultiBufferSource {
        return MultiBufferSource { renderType -> unwrap(source.getBuffer(NeoRenderSettingsImpl(renderType))) }
    }
}