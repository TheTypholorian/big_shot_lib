package net.typho.big_shot_lib.impl.client

import com.mojang.blaze3d.pipeline.RenderTarget
import com.mojang.blaze3d.pipeline.TextureTarget
import com.mojang.blaze3d.shaders.Program
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.blaze3d.vertex.VertexFormatElement
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderStateShard
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.ShaderInstance
import net.minecraft.client.renderer.texture.AbstractTexture
import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.client.InternalClientUtil
import net.typho.big_shot_lib.api.client.ext.VertexFormatElementExtension
import net.typho.big_shot_lib.api.client.rendering.NeoShaderLoader
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlAlphaFunction
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBeginMode
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferUsage
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlDataType
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureFormat
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlVertexElementReadType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlBuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlResourceType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlShader
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlShaderType
import net.typho.big_shot_lib.api.client.rendering.state.GpuDrawState
import net.typho.big_shot_lib.api.client.rendering.state.LayeringState
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager
import net.typho.big_shot_lib.api.client.rendering.opengl.util.BlendFunction
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormats.register
import net.typho.big_shot_lib.api.math.IVec3
import net.typho.big_shot_lib.impl.client.rendering.opengl.ShaderInstanceExtension
import net.typho.big_shot_lib.impl.client.rendering.state.NeoTextureStateShard
import net.typho.big_shot_lib.api.util.getExtensionValue
import net.typho.big_shot_lib.api.util.setExtensionValue
import net.typho.big_shot_lib.impl.client.rendering.opengl.GlBufferImpl
import net.typho.big_shot_lib.impl.client.rendering.opengl.NeoDynamicTexture
import org.joml.Vector3f
import sun.misc.Unsafe
import java.lang.reflect.Modifier

//? fabric {

//? } neoforge {
//? }

object InternalClientUtilImpl : InternalClientUtil {
    @JvmField
    val UNSAFE = getUnsafe()

    private fun getUnsafe(): Unsafe {
        val fields = Unsafe::class.java.getDeclaredFields()

        for (field in fields) {
            if (field.type != Unsafe::class.java) {
                continue
            }

            val modifiers = field.modifiers
            if (!(Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers))) {
                continue
            }

            try {
                field.setAccessible(true)
                return field.get(null) as Unsafe
            } catch (ignored: Exception) {
            }
        }

        throw UnsupportedOperationException("Big Shot Lib requires sun.misc.Unsafe to be available.")
    }

    override fun getTexture(location: Identifier): AbstractTexture? {
        return Minecraft.getInstance().textureManager.getTexture(location, null)
    }

    override fun getProgram(location: Identifier): GlProgram? {
        return Minecraft.getInstance().gameRenderer.getShader(location.toShortString())?.getExtensionValue() ?: NeoShaderLoader[location]
    }

    override fun transformNormal(
        pose: PoseStack.Pose,
        x: Float,
        y: Float,
        z: Float
    ): IVec3<Float> {
        //? if >=1.20.5 {
        return IVec3(pose.transformNormal(x, y, z, Vector3f()))
        //? } else {
        /*return IVec3(pose.normal().transform(Vector3f(x, y, z)))
        *///? }
    }

    override fun createShader(location: Identifier, type: GlShaderType, glId: Int): GlShader {
        return when (type) {
            GlShaderType.VERTEX -> Program(Program.Type.VERTEX, glId, location.toShortString()).getExtensionValue()
            GlShaderType.FRAGMENT -> Program(Program.Type.VERTEX, glId, location.toShortString()).getExtensionValue()
            else -> GlShader.Impl(location, type, glId)
        }
    }

    override fun createProgram(
        location: Identifier,
        format: VertexFormat,
        glId: Int
    ): GlProgram {
        val shader = UNSAFE.allocateInstance(ShaderInstance::class.java) as ShaderInstance
        (shader as ShaderInstanceExtension).`big_shot_lib$init`(location, format, glId)
        return shader.getExtensionValue<GlProgram>()
    }

    override fun createRenderType(
        location: Identifier,
        format: VertexFormat,
        drawState: GpuDrawState.Builder,
        defaultBufferSize: Int,
        mode: GlBeginMode,
        affectsCrumbling: Boolean,
        sortOnUpload: Boolean,
        isOutline: Boolean
    ): RenderType {
        val blend = drawState.blend?.let { function ->
            RenderStateShard.TransparencyStateShard(
                "$function",
                {
                    NeoGlStateManager.INSTANCE.blendEnabled = true
                    NeoGlStateManager.INSTANCE.blendFunction = function
                },
                {
                    NeoGlStateManager.INSTANCE.blendEnabled = false
                    NeoGlStateManager.INSTANCE.blendFunction = BlendFunction.DEFAULT
                }
            ).also { it.setExtensionValue(drawState.blend) }
        } ?: RenderStateShard.NO_TRANSPARENCY
        val mask = RenderStateShard.WriteMaskStateShard(
            drawState.writeColor,
            drawState.writeDepth
        )
        val cull = if (drawState.cull) RenderStateShard.CULL else RenderStateShard.NO_CULL
        val depthTest = drawState.depth?.let { function ->
            when (function) {
                GlAlphaFunction.EQUAL -> RenderStateShard.EQUAL_DEPTH_TEST
                GlAlphaFunction.LEQUAL -> RenderStateShard.LEQUAL_DEPTH_TEST
                GlAlphaFunction.GREATER -> RenderStateShard.GREATER_DEPTH_TEST
                GlAlphaFunction.ALWAYS -> RenderStateShard.NO_DEPTH_TEST
                else -> RenderStateShard.DepthTestStateShard(
                    function.toString(),
                    function.glId
                )
            }
        } ?: RenderStateShard.NO_DEPTH_TEST
        val layering = when (drawState.layering) {
            LayeringState.DISABLED -> RenderStateShard.NO_LAYERING
            LayeringState.POLYGON_OFFSET -> RenderStateShard.POLYGON_OFFSET_LAYERING
            LayeringState.VIEW_OFFSET -> RenderStateShard.VIEW_OFFSET_Z_LAYERING
        }
        val lightmap = if (drawState.lightmap) RenderStateShard.LIGHTMAP else RenderStateShard.NO_LIGHTMAP
        val overlay = if (drawState.overlay) RenderStateShard.OVERLAY else RenderStateShard.NO_OVERLAY
        val texture = drawState.texture?.let { texture ->
            NeoTextureStateShard(texture)
        } ?: RenderStateShard.NO_TEXTURE
        val shader = drawState.shader?.let { shader ->
            RenderStateShard.ShaderStateShard { shader.get().getExtensionValue() }
        } ?: RenderStateShard.NO_SHADER

        return RenderType.create(
            location.toShortString(),
            format.getExtensionValue<VertexFormat>(),
            when (mode) {
                GlBeginMode.POINTS -> throw UnsupportedOperationException(mode.toString())
                GlBeginMode.LINES -> VertexFormat.Mode.LINES
                GlBeginMode.LINE_LOOP -> throw UnsupportedOperationException(mode.toString())
                GlBeginMode.LINE_STRIP -> VertexFormat.Mode.LINE_STRIP
                GlBeginMode.TRIANGLES -> VertexFormat.Mode.TRIANGLES
                GlBeginMode.TRIANGLE_STRIP -> VertexFormat.Mode.TRIANGLE_STRIP
                GlBeginMode.TRIANGLE_FAN -> VertexFormat.Mode.TRIANGLE_FAN
                GlBeginMode.QUADS -> VertexFormat.Mode.QUADS
            },
            defaultBufferSize,
            affectsCrumbling,
            sortOnUpload,
            RenderType.CompositeState.builder()
                .setTransparencyState(blend)
                .setWriteMaskState(mask)
                .setCullState(cull)
                .setDepthTestState(depthTest)
                .setLayeringState(layering)
                .setLightmapState(lightmap)
                .setOverlayState(overlay)
                .setTextureState(texture)
                .setShaderState(shader)
                .createCompositeState(isOutline)
        )
    }

    override fun createRenderTarget(width: Int, height: Int, useDepth: Boolean, name: () -> String): RenderTarget {
        return TextureTarget(width, height, useDepth, Minecraft.ON_OSX)
    }

    override fun createTexture(
        width: Int,
        height: Int,
        format: GlTextureFormat,
        blur: Boolean,
        mipmap: Boolean
    ): AbstractTexture {
        return NeoDynamicTexture(width, height, format, blur, mipmap, GlResourceType.TEXTURE.create())
    }

    override fun createBuffer(size: Long, usage: GlBufferUsage, target: GlBufferTarget): GlBuffer {
        return GlBufferImpl(size, usage, target)
    }

    override fun createRegisteredVertexFormatBuilder(location: Identifier): VertexFormat.Builder {
        return object : VertexFormat.Builder() {
            override fun build(): VertexFormat {
                val format = super.build()
                register(location, format)
                return format
            }
        }
    }

    override fun createVertexFormatElement(
        index: Int,
        inType: GlDataType,
        outType: GlVertexElementReadType,
        count: Int
    ): VertexFormatElement {
        if (!outType.supports(inType)) {
            throw IllegalArgumentException("GlDataType.$inType is not supported by GlVertexElementReadType.$outType")
        }

        val element = VertexFormatElement.register(
            VertexFormatElement.BY_ID.size,
            index,
            when (inType) {
                GlDataType.UBYTE -> VertexFormatElement.Type.UBYTE
                GlDataType.BYTE -> VertexFormatElement.Type.BYTE
                GlDataType.USHORT -> VertexFormatElement.Type.USHORT
                GlDataType.SHORT -> VertexFormatElement.Type.SHORT
                GlDataType.UINT -> VertexFormatElement.Type.UINT
                GlDataType.INT -> VertexFormatElement.Type.INT
                GlDataType.FLOAT -> VertexFormatElement.Type.FLOAT
            },
            null,
            count
        )
        (element as VertexFormatElementExtension).`big_shot_lib$outType` = outType
        return element
    }
}