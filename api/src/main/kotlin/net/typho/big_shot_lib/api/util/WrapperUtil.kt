package net.typho.big_shot_lib.api.util

import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.ByteBufferBuilder
import com.mojang.blaze3d.vertex.MeshData
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.ResourceKey
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.tags.TagKey
import net.typho.big_shot_lib.api.BigShotApi.loadService
import net.typho.big_shot_lib.api.client.rendering.NeoVertexConsumer
import net.typho.big_shot_lib.api.client.rendering.NeoVertexFormat
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBeginMode
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlIndexDataType
import net.typho.big_shot_lib.api.client.rendering.quad.NeoBakedQuad
import net.typho.big_shot_lib.api.client.util.resource.NeoResourceManager
import net.typho.big_shot_lib.api.util.resource.NeoResourceKey
import net.typho.big_shot_lib.api.util.resource.NeoTagKey

interface WrapperUtil {
    fun wrap(manager: ResourceManager): NeoResourceManager

    fun wrap(target: RenderTarget): GlFramebuffer

    fun <T : Any> wrap(registry: Registry<T>): NeoRegistry<T>

    fun wrap(access: RegistryAccess): NeoRegistryAccess

    fun wrap(quad: BakedQuad, cache: Boolean): NeoBakedQuad

    fun wrap(consumer: VertexConsumer): NeoVertexConsumer

    fun <T : Any> wrap(key: ResourceKey<T>): NeoResourceKey<T>

    fun <T : Any> unwrap(key: NeoResourceKey<T>): ResourceKey<T>

    fun <T : Any> wrap(key: TagKey<T>): NeoTagKey<T>

    fun <T : Any> unwrap(key: NeoTagKey<T>): TagKey<T>

    fun createVertexFormatBuilder(): NeoVertexFormat.Builder

    fun positionVertexElement(): NeoVertexFormat.Element

    fun colorVertexElement(): NeoVertexFormat.Element

    fun textureUVVertexElement(): NeoVertexFormat.Element

    fun overlayUVVertexElement(): NeoVertexFormat.Element

    fun lightUVVertexElement(): NeoVertexFormat.Element

    fun normalVertexElement(): NeoVertexFormat.Element

    fun blitScreenVertexFormat(): NeoVertexFormat

    fun blockVertexFormat(): NeoVertexFormat

    fun newEntityVertexFormat(): NeoVertexFormat

    fun particleVertexFormat(): NeoVertexFormat

    fun positionVertexFormat(): NeoVertexFormat

    fun positionColorVertexFormat(): NeoVertexFormat

    fun positionColorNormalVertexFormat(): NeoVertexFormat

    fun positionColorLightVertexFormat(): NeoVertexFormat

    fun positionTexVertexFormat(): NeoVertexFormat

    fun positionTexColorVertexFormat(): NeoVertexFormat

    fun positionColorTexLightVertexFormat(): NeoVertexFormat

    fun positionTexLightColorVertexFormat(): NeoVertexFormat

    fun positionTexColorNormalVertexFormat(): NeoVertexFormat

    fun getIndexType(state: MeshData.DrawState): GlIndexDataType

    fun createBufferBuilder(buffer: ByteBufferBuilder, mode: GlBeginMode, format: NeoVertexFormat): BufferBuilder

    companion object {
        @JvmField
        val INSTANCE = WrapperUtil::class.loadService()
    }
}