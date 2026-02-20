package net.typho.big_shot_lib.api.services

import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.ByteBufferBuilder
import com.mojang.blaze3d.vertex.MeshData
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.server.packs.resources.ResourceManager
import net.typho.big_shot_lib.api.BigShotApi.loadService
import net.typho.big_shot_lib.api.client.rendering.meshes.NeoVertexConsumer
import net.typho.big_shot_lib.api.client.rendering.meshes.NeoVertexFormat
import net.typho.big_shot_lib.api.client.rendering.meshes.TexturedQuad
import net.typho.big_shot_lib.api.client.rendering.textures.GlFramebuffer
import net.typho.big_shot_lib.api.client.rendering.util.GlIndexType
import net.typho.big_shot_lib.api.client.rendering.util.GlShapeType
import net.typho.big_shot_lib.api.util.NeoRegistry
import net.typho.big_shot_lib.api.util.NeoRegistryAccess

interface WrapperUtil {
    fun wrap(manager: ResourceManager): NeoResourceManager

    fun wrap(target: RenderTarget): GlFramebuffer

    fun <T> wrap(registry: Registry<T>): NeoRegistry<T>

    fun wrap(access: RegistryAccess): NeoRegistryAccess

    fun wrap(quad: BakedQuad): TexturedQuad

    fun wrap(consumer: VertexConsumer): NeoVertexConsumer

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

    fun getIndexType(state: MeshData.DrawState): GlIndexType

    fun createBufferBuilder(buffer: ByteBufferBuilder, mode: GlShapeType, format: NeoVertexFormat): BufferBuilder

    companion object {
        @JvmField
        val INSTANCE: WrapperUtil = WrapperUtil::class.loadService()
    }
}