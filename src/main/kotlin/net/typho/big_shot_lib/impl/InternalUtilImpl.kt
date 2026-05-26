package net.typho.big_shot_lib.impl

import com.mojang.blaze3d.shaders.Program
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.blaze3d.vertex.VertexFormatElement
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderStateShard
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.ShaderInstance
import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.BigShotApi.toShortString
import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlAlphaFunction
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBeginMode
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl.NeoGlShader
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlShader
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlShaderType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture2D
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlDrawState
import net.typho.big_shot_lib.api.client.rendering.opengl.state.LayeringState
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager
import net.typho.big_shot_lib.api.client.rendering.opengl.util.BlendFunction
import net.typho.big_shot_lib.api.client.rendering.util.NeoAtlas
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat
import net.typho.big_shot_lib.api.math.vec.IVec3
import net.typho.big_shot_lib.api.math.vec.NeoVec3f
import net.typho.big_shot_lib.impl.client.rendering.opengl.ShaderInstanceExtension
import net.typho.big_shot_lib.impl.client.rendering.opengl.state.NeoTextureStateShard
import net.typho.big_shot_lib.api.util.getExtensionValue
import net.typho.big_shot_lib.api.util.setExtensionValue
import org.joml.Vector3f
import sun.misc.Unsafe
import java.lang.reflect.Modifier

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

    override fun createVertexFormatBuilder(): NeoVertexFormat.Builder {
        return VertexFormat.builder().getExtensionValue()
    }

    override val positionVertexElement: NeoVertexFormat.Element
        //? if >=1.21 {
        get() = VertexFormatElement.POSITION.getExtensionValue()
        //? } else {
        /*get() = DefaultVertexFormat.ELEMENT_POSITION.getExtensionValue()
        *///? }
    override val colorVertexElement: NeoVertexFormat.Element
        //? if >=1.21 {
        get() = VertexFormatElement.COLOR.getExtensionValue()
        //? } else {
        /*get() = DefaultVertexFormat.ELEMENT_COLOR.getExtensionValue()
        *///? }
    override val textureUVVertexElement: NeoVertexFormat.Element
        //? if >=1.21 {
        get() = VertexFormatElement.UV0.getExtensionValue()
        //? } else {
        /*get() = DefaultVertexFormat.ELEMENT_UV0.getExtensionValue()
        *///? }
    override val overlayUVVertexElement: NeoVertexFormat.Element
        //? if >=1.21 {
        get() = VertexFormatElement.UV1.getExtensionValue()
        //? } else {
        /*get() = DefaultVertexFormat.ELEMENT_UV1.getExtensionValue()
        *///? }
    override val lightUVVertexElement: NeoVertexFormat.Element
        //? if >=1.21 {
        get() = VertexFormatElement.UV2.getExtensionValue()
        //? } else {
        /*get() = DefaultVertexFormat.ELEMENT_UV2.getExtensionValue()
        *///? }
    override val normalVertexElement: NeoVertexFormat.Element
        //? if >=1.21 {
        get() = VertexFormatElement.NORMAL.getExtensionValue()
        //? } else {
        /*get() = DefaultVertexFormat.ELEMENT_NORMAL.getExtensionValue()
        *///? }

    //? if <1.21.9 {
    override val blitScreenVertexFormat: NeoVertexFormat = DefaultVertexFormat.BLIT_SCREEN.getExtensionValue()
    //? } else {
    /*override val blitScreenVertexFormat: NeoVertexFormat = DefaultVertexFormat.POSITION.getExtensionValue()
    *///? }
    override val blockVertexFormat: NeoVertexFormat = DefaultVertexFormat.BLOCK.getExtensionValue()
    override val newEntityVertexFormat: NeoVertexFormat = DefaultVertexFormat.NEW_ENTITY.getExtensionValue()
    override val particleVertexFormat: NeoVertexFormat = DefaultVertexFormat.PARTICLE.getExtensionValue()
    override val positionVertexFormat: NeoVertexFormat = DefaultVertexFormat.POSITION.getExtensionValue()
    override val positionColorVertexFormat: NeoVertexFormat = DefaultVertexFormat.POSITION_COLOR.getExtensionValue()
    override val positionColorNormalVertexFormat: NeoVertexFormat = DefaultVertexFormat.POSITION_COLOR_NORMAL.getExtensionValue()
    override val positionColorLightVertexFormat: NeoVertexFormat = DefaultVertexFormat.POSITION_COLOR_LIGHTMAP.getExtensionValue()
    override val positionTexVertexFormat: NeoVertexFormat = DefaultVertexFormat.POSITION_TEX.getExtensionValue()
    override val positionTexColorVertexFormat: NeoVertexFormat = DefaultVertexFormat.POSITION_TEX_COLOR.getExtensionValue()
    override val positionColorTexLightVertexFormat: NeoVertexFormat = DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP.getExtensionValue()
    override val positionTexLightColorVertexFormat: NeoVertexFormat = DefaultVertexFormat.POSITION_TEX_LIGHTMAP_COLOR.getExtensionValue()
    override val positionTexColorNormalVertexFormat: NeoVertexFormat = DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL.getExtensionValue()

    override fun getTexture(location: Identifier): GlTexture2D {
        return Minecraft.getInstance().textureManager.getTexture(location).getExtensionValue()
    }

    override fun getAtlas(location: Identifier): NeoAtlas {
        //? if <1.21.9 {
        return Minecraft.getInstance().modelManager.getAtlas(location.withPrefix("textures/atlas/").withSuffix(".png")).getExtensionValue()
        //? } else {
        /*return Minecraft.getInstance().atlasManager.getAtlasOrThrow(location.mojang).getExtensionValue()
        *///? }
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
            else -> NeoGlShader(location, type, glId)
        }
    }

    override fun createProgram(
        location: Identifier,
        format: NeoVertexFormat,
        glId: Int
    ): GlProgram {
        val shader = UNSAFE.allocateInstance(ShaderInstance::class.java) as ShaderInstance
        (shader as ShaderInstanceExtension).`big_shot_lib$init`(location, format, glId)
        return shader.getExtensionValue<GlProgram>()
    }

    override fun createRenderType(
        location: Identifier,
        format: NeoVertexFormat,
        drawState: GlDrawState.Builder,
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
}