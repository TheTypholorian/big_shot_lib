package net.typho.big_shot_lib.impl

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
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.texture.AbstractTexture
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.core.Direction
import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTab
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.BigShotApi.toShortString
import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.client.rendering.NeoShaderLoader
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlAlphaFunction
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBeginMode
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferUsage
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureFormat
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlBuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlResourceType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlShader
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.GlShaderType
import net.typho.big_shot_lib.api.client.rendering.state.GpuDrawState
import net.typho.big_shot_lib.api.client.rendering.state.LayeringState
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager
import net.typho.big_shot_lib.api.client.rendering.opengl.util.BlendFunction
import net.typho.big_shot_lib.api.client.rendering.util.quad.NeoVertexData
import net.typho.big_shot_lib.api.event.RegistryBuilder
import net.typho.big_shot_lib.api.math.vec.IVec3
import net.typho.big_shot_lib.api.math.vec.NeoVec3f
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
/*import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.event.registry.RegistryAttribute
*///? } neoforge {
//? }

object InternalUtilImpl : InternalUtil {
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

    override val positionVertexElement: VertexFormatElement
        //? if >=1.21 {
        get() = VertexFormatElement.POSITION.getExtensionValue()
        //? } else {
        /*get() = DefaultVertexFormat.ELEMENT_POSITION.getExtensionValue()
        *///? }
    override val colorVertexElement: VertexFormatElement
        //? if >=1.21 {
        get() = VertexFormatElement.COLOR.getExtensionValue()
        //? } else {
        /*get() = DefaultVertexFormat.ELEMENT_COLOR.getExtensionValue()
        *///? }
    override val textureUVVertexElement: VertexFormatElement
        //? if >=1.21 {
        get() = VertexFormatElement.UV0.getExtensionValue()
        //? } else {
        /*get() = DefaultVertexFormat.ELEMENT_UV0.getExtensionValue()
        *///? }
    override val overlayUVVertexElement: VertexFormatElement
        //? if >=1.21 {
        get() = VertexFormatElement.UV1.getExtensionValue()
        //? } else {
        /*get() = DefaultVertexFormat.ELEMENT_UV1.getExtensionValue()
        *///? }
    override val lightUVVertexElement: VertexFormatElement
        //? if >=1.21 {
        get() = VertexFormatElement.UV2.getExtensionValue()
        //? } else {
        /*get() = DefaultVertexFormat.ELEMENT_UV2.getExtensionValue()
        *///? }
    override val normalVertexElement: VertexFormatElement
        //? if >=1.21 {
        get() = VertexFormatElement.NORMAL.getExtensionValue()
        //? } else {
        /*get() = DefaultVertexFormat.ELEMENT_NORMAL.getExtensionValue()
        *///? }

    override val blockVertexFormat: VertexFormat = DefaultVertexFormat.BLOCK.getExtensionValue()
    override val newEntityVertexFormat: VertexFormat = DefaultVertexFormat.NEW_ENTITY.getExtensionValue()
    override val particleVertexFormat: VertexFormat = DefaultVertexFormat.PARTICLE.getExtensionValue()
    override val positionVertexFormat: VertexFormat = DefaultVertexFormat.POSITION.getExtensionValue()
    override val positionColorVertexFormat: VertexFormat = DefaultVertexFormat.POSITION_COLOR.getExtensionValue()
    override val positionColorNormalVertexFormat: VertexFormat = DefaultVertexFormat.POSITION_COLOR_NORMAL.getExtensionValue()
    override val positionColorLightVertexFormat: VertexFormat = DefaultVertexFormat.POSITION_COLOR_LIGHTMAP.getExtensionValue()
    override val positionTexVertexFormat: VertexFormat = DefaultVertexFormat.POSITION_TEX.getExtensionValue()
    override val positionTexColorVertexFormat: VertexFormat = DefaultVertexFormat.POSITION_TEX_COLOR.getExtensionValue()
    override val positionColorTexLightVertexFormat: VertexFormat = DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP.getExtensionValue()
    override val positionTexLightColorVertexFormat: VertexFormat = DefaultVertexFormat.POSITION_TEX_LIGHTMAP_COLOR.getExtensionValue()
    override val positionTexColorNormalVertexFormat: VertexFormat = DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL.getExtensionValue()

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
        return NeoVec3f(pose.transformNormal(x, y, z, Vector3f()))
        //? } else {
        /*return NeoVec3f(pose.normal().transform(Vector3f(x, y, z)))
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

    @OptIn(ExperimentalUnsignedTypes::class)
    override fun createBakedQuad(
        vertices: Array<NeoVertexData>,
        tintIndex: Int,
        direction: Direction,
        sprite: TextureAtlasSprite,
        shade: Boolean
    ): BakedQuad {
        val data = IntArray(32)

        vertices[0].packToInts(data, 0)
        vertices[1].packToInts(data, 8)
        vertices[2].packToInts(data, 16)
        vertices[3].packToInts(data, 24)

        return BakedQuad(data, tintIndex, direction, sprite, shade)
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

    override fun <T> createRegistryBuilder(key: ResourceKey<Registry<T>>): RegistryBuilder<T> {
        return RegistryBuilderImpl(key)
    }

    override fun createCreativeTabBuilder(): CreativeModeTab.Builder {
        return CreativeModeTab.builder()
    }
}