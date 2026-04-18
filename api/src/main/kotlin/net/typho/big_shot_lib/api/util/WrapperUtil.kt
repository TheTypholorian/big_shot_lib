package net.typho.big_shot_lib.api.util

import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceKey
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.tags.TagKey
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexConsumer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlFramebuffer
import net.typho.big_shot_lib.api.client.rendering.util.MultiBufferSourceInjection
import net.typho.big_shot_lib.api.client.rendering.util.NeoMultiBufferSource
import net.typho.big_shot_lib.api.client.rendering.util.quad.NeoBakedQuad
import net.typho.big_shot_lib.api.client.util.resource.NeoResourceManager
import net.typho.big_shot_lib.api.util.resource.NeoResourceKey
import net.typho.big_shot_lib.api.util.resource.NeoTagKey

interface WrapperUtil {
    fun wrap(manager: ResourceManager): NeoResourceManager

    fun wrap(target: RenderTarget): GlFramebuffer

    fun <T : Any> wrap(registry: Registry<T>): NeoRegistry<T>

    fun wrap(access: RegistryAccess): NeoRegistryAccess

    fun wrap(quad: BakedQuad): NeoBakedQuad

    fun wrap(consumer: VertexConsumer): NeoVertexConsumer

    fun unwrap(consumer: NeoVertexConsumer): VertexConsumer

    fun <T : Any> wrap(key: ResourceKey<T>): NeoResourceKey<T>

    fun <T : Any> unwrap(key: NeoResourceKey<T>): ResourceKey<T>

    fun <T : Any> wrap(key: TagKey<T>): NeoTagKey<T>

    fun <T : Any> unwrap(key: NeoTagKey<T>): TagKey<T>

    fun inject(vanilla: MultiBufferSource.BufferSource, injection: MultiBufferSourceInjection): MultiBufferSource.BufferSource

    fun wrap(source: MultiBufferSource): NeoMultiBufferSource

    fun unwrap(source: NeoMultiBufferSource): MultiBufferSource

    companion object {
        val INSTANCE by lazy { WrapperUtil::class.loadService() }
    }
}