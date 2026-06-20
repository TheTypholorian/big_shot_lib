package net.typho.big_shot_lib.api.client

import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.blaze3d.vertex.VertexFormatElement
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.texture.AbstractTexture
import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBeginMode
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferUsage
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlDataType
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureFormat
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlVertexElementReadType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlBuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlShader
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlShaderType
import net.typho.big_shot_lib.api.client.rendering.state.GpuDrawState
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormats.register
import net.typho.big_shot_lib.api.math.IVec3
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService

interface InternalClientUtil {
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

    fun createVertexFormatElement(
        index: Int,
        inType: GlDataType,
        outType: GlVertexElementReadType,
        count: Int
    ): VertexFormatElement

    companion object {
        @JvmStatic
        @get:JvmName("getInstance")
        val INSTANCE by lazy { InternalClientUtil::class.loadService() }
    }
}