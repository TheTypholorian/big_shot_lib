package net.typho.big_shot_lib.impl.util

import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebuffer
import net.typho.big_shot_lib.api.client.rendering.util.MultiBufferSourceInjection
import net.typho.big_shot_lib.api.client.rendering.util.NeoMultiBufferSource
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexConsumer
import net.typho.big_shot_lib.api.client.rendering.util.quad.NeoBakedQuad
import net.typho.big_shot_lib.api.util.NeoRegistry
import net.typho.big_shot_lib.api.util.NeoRegistryAccess
import net.typho.big_shot_lib.api.util.WrapperUtil
import net.typho.big_shot_lib.impl.client.rendering.util.NeoVertexConsumerWrapper
import net.typho.big_shot_lib.impl.client.rendering.util.VertexConsumerWrapper
import java.util.stream.Collectors
import kotlin.jvm.optionals.getOrNull

object WrapperUtilImpl : WrapperUtil {
    override fun wrap(target: RenderTarget): GlFramebuffer {
        return target.getExtensionValue()
    }

    override fun <T : Any> wrap(registry: Registry<T>): NeoRegistry<T> {
        return object : NeoRegistry<T> {
            override val key: ResourceKey<out Registry<T>> = registry.key()

            override fun get(value: Identifier): T? {
                //? if <1.21.2 {
                return registry.get(value)
                //? } else {
                /*return registry.get(value).getOrNull()?.value()
                *///? }
            }

            override fun getKey(value: T): ResourceKey<T> {
                return registry.getResourceKey(value).orElseThrow()
            }

            override fun contains(value: Identifier): Boolean {
                return registry.containsKey(value)
            }

            override fun keys(): Set<ResourceKey<T>> {
                return registry.keySet().map { ResourceKey.create(key, it) }.toSet()
            }

            override fun entries(): Set<Pair<ResourceKey<T>, T>> {
                return registry.entrySet().map { entry -> entry.key to entry.value }.toSet()
            }

            override fun values(): Collection<T> {
                return registry.entrySet().map { it.value }
            }

            override fun getTag(key: TagKey<T>): Set<T>? {
                //? if <1.21.2 {
                return registry.getTag(key)
                    .map { set ->
                        set.stream()
                            .map { it.value() }
                            .collect(Collectors.toSet())
                    }
                    .getOrNull()
                //? } else {
                /*return if (registry.listTagIds().anyMatch { it == key }) {
                    registry.getTagOrEmpty(key).toList()
                        .stream()
                        .map { it.value() }
                        .collect(Collectors.toSet())
                } else null
                *///? }
            }

            override fun tags(): Set<TagKey<T>> {
                //? if <1.21.2 {
                return registry.tags.map { it.first }.collect(Collectors.toSet())
                //? } else {
                /*return registry.listTagIds().map { it }.collect(Collectors.toSet())
                *///? }
            }

            override fun addAlias(old: Identifier, new: Identifier) {
                registry.addAlias(old, new)
            }
        }
    }

    override fun wrap(access: RegistryAccess): NeoRegistryAccess {
        return object : NeoRegistryAccess {
            override fun <T : Any> registry(key: ResourceKey<Registry<T>>): NeoRegistry<T>? {
                //? if <1.21.2 {
                return access.registry(key).map { wrap(it) }.getOrNull()
                //? } else {
                /*return access.lookup(key).map { wrap(it) }.getOrNull()
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

    override fun inject(
        vanilla: MultiBufferSource.BufferSource,
        injection: MultiBufferSourceInjection
    ): MultiBufferSource.BufferSource {
        val injections = vanilla.getExtensionValue<MutableList<MultiBufferSourceInjection>>()
        injections.add(injection)
        return vanilla
    }

    override fun uninject(
        vanilla: MultiBufferSource.BufferSource,
        injection: MultiBufferSourceInjection
    ): MultiBufferSource.BufferSource {
        val injections = vanilla.getExtensionValue<MutableList<MultiBufferSourceInjection>>()
        injections.remove(injection)
        return vanilla
    }

    override fun wrap(source: MultiBufferSource): NeoMultiBufferSource {
        return NeoMultiBufferSource { renderType -> wrap(source.getBuffer(renderType.getExtensionValue())) }
    }

    override fun unwrap(source: NeoMultiBufferSource): MultiBufferSource {
        return MultiBufferSource { renderType -> unwrap(source.getBuffer(renderType.getExtensionValue())) }
    }
}