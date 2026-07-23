package net.typho.big_shot_lib.client.impl

import com.mojang.blaze3d.GpuFormat
import com.mojang.blaze3d.vertex.VertexFormat
import net.typho.big_shot_lib.client.api.IInternalClientUtil
import net.typho.big_shot_lib.api.event.NeoClientEventBus
import net.typho.big_shot_lib.api.util.Extension.Companion.castTo
import sun.misc.Unsafe
import java.lang.reflect.Modifier
import kotlin.jvm.java

object InternalClientUtilImpl : IInternalClientUtil {
    @JvmField
    val UNSAFE = getUnsafe()

    private fun getUnsafe(): Unsafe {
        val fields = Unsafe::class.java.declaredFields

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

    override fun getEventBus(modId: String): NeoClientEventBus {
        TODO("")
        //return NeoClientEventBusImpl
    }

    /*
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
     */

    /*
    override fun createGpuFramebufferImpl(width: Int, height: Int, useDepth: Boolean, name: () -> String): GpuFramebufferImpl {
        return TextureTarget(width, height, useDepth, Minecraft.ON_OSX)
    }

    override fun createGpuFramebufferImpl(
        color: GpuTexture,
        depth: GpuTexture?,
        name: () -> String
    ): GpuFramebufferImpl {
        return TextureWrapperGpuFramebufferImpl(color, depth)
    }

    override fun createTexture(
        width: Int,
        height: Int,
        format: GlTextureFormat,
        blur: Boolean,
        mipmap: Boolean
    ): AbstractTexture {
        val texture = NeoDynamicTexture(width, height, format, blur, mipmap, GlResourceType.TEXTURE.create())
        GlStateManager._bindTexture(texture.glId)
        GlStateManager._texImage2D(GL_TEXTURE_2D, 0, format.internalId, width, height, 0, format.glId, format.type, null as IntBuffer?)
        return texture
    }

    override fun createTexture(
        width: Int,
        height: Int,
        glId: Int,
        format: GlTextureFormat,
        blur: Boolean,
        mipmap: Boolean
    ): AbstractTexture {
        return NeoDynamicTexture(width, height, format, blur, mipmap, glId)
    }

    override fun createBuffer(size: Long, usage: GlBufferUsage, target: GlBufferTarget): GlBuffer {
        return GlBufferImpl(size, usage, target)
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

    override fun rawGetGpuFramebufferImplColor(target: GpuFramebufferImpl): GpuTexture? {
        //? if <1.21.11 {
        /*return ITexture.tryWrap(target.width, target.height, target.colorTextureId)
        *///? }
    }

    override fun rawGetGpuFramebufferImplDepth(target: GpuFramebufferImpl): GpuTexture? {
        //? if <1.21.11 {
        /*return ITexture.tryWrap(target.width, target.height, target.depthTextureId)
        *///? }
    }

    override fun rawResizeGpuFramebufferImpl(
        target: GpuFramebufferImpl,
        width: Int,
        height: Int
    ) {
        //? if <1.21.11 {
        /*target.resize(width, height, Minecraft.ON_OSX)
        *///? } else {
        target.resize(width, height)
        //? }
    }

    override fun rawCreateBuffersGpuFramebufferImpl(
        target: GpuFramebufferImpl,
        width: Int,
        height: Int
    ) {
        //? if <1.21.11 {
        /*target.createBuffers(width, height, Minecraft.ON_OSX)
        *///? } else {
        target.createBuffers(width, height)
        //? }
    }

    override fun rawIsGpuFramebufferImplFreed(target: GpuFramebufferImpl): Boolean {
        return target.frameBufferId == -1
    }

    override fun rawGetGpuFramebufferImplId(target: GpuFramebufferImpl): Int {
        return target.frameBufferId
    }

    override fun rawFreeGpuFramebufferImpl(target: GpuFramebufferImpl) {
        target.destroyBuffers()
    }
     */
}