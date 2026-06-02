package net.typho.big_shot_lib.api

import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.blaze3d.vertex.VertexFormatElement
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.texture.AbstractTexture
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.core.Direction
import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBeginMode
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferUsage
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureFormat
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlBuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlShader
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlShaderType
import net.typho.big_shot_lib.api.client.rendering.state.GpuDrawState
import net.typho.big_shot_lib.api.client.rendering.util.quad.NeoVertexData
import net.typho.big_shot_lib.api.event.RegistryBuilder
import net.typho.big_shot_lib.api.math.vec.IVec3
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService

interface InternalUtil {
    val positionVertexElement: VertexFormatElement
    val colorVertexElement: VertexFormatElement
    val textureUVVertexElement: VertexFormatElement
    val overlayUVVertexElement: VertexFormatElement
    val lightUVVertexElement: VertexFormatElement
    val normalVertexElement: VertexFormatElement
    val blockVertexFormat: VertexFormat
    val newEntityVertexFormat: VertexFormat
    val particleVertexFormat: VertexFormat
    val positionVertexFormat: VertexFormat
    val positionColorVertexFormat: VertexFormat
    val positionColorNormalVertexFormat: VertexFormat
    val positionColorLightVertexFormat: VertexFormat
    val positionTexVertexFormat: VertexFormat
    val positionTexColorVertexFormat: VertexFormat
    val positionColorTexLightVertexFormat: VertexFormat
    val positionTexLightColorVertexFormat: VertexFormat
    val positionTexColorNormalVertexFormat: VertexFormat

    fun getTexture(location: Identifier): AbstractTexture?

    fun getProgram(location: Identifier): GlProgram?

    fun transformNormal(pose: PoseStack.Pose, x: Float, y: Float, z: Float): IVec3<Float>

    fun createShader(location: Identifier, type: GlShaderType, glId: Int): GlShader

    fun createProgram(location: Identifier, format: VertexFormat, glId: Int): GlProgram

    fun createRenderType(
        location: Identifier,
        format: VertexFormat,
        drawState: GpuDrawState.Builder,
        defaultBufferSize: Int,
        mode: GlBeginMode,
        affectsCrumbling: Boolean,
        sortOnUpload: Boolean,
        isOutline: Boolean
    ): RenderType

    fun createBakedQuad(
        vertices: Array<NeoVertexData>,
        tintIndex: Int,
        direction: Direction,
        sprite: TextureAtlasSprite,
        shade: Boolean
    ): BakedQuad

    fun createRenderTarget(
        width: Int,
        height: Int,
        useDepth: Boolean,
        name: () -> String
    ): RenderTarget

    fun createTexture(
        width: Int,
        height: Int,
        format: GlTextureFormat,
        blur: Boolean,
        mipmap: Boolean
    ): AbstractTexture

    fun createBuffer(
        size: Long,
        usage: GlBufferUsage,
        target: GlBufferTarget
    ): GlBuffer

    fun <T> createRegistryBuilder(
        key: ResourceKey<Registry<T>>
    ): RegistryBuilder<T>

    companion object {
        @JvmStatic
        @get:JvmName("getInstance")
        val INSTANCE by lazy { InternalUtil::class.loadService() }
    }
}