package net.typho.big_shot_lib.impl

import com.google.common.collect.ImmutableList
import com.mojang.blaze3d.shaders.Program
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.blaze3d.vertex.VertexFormatElement
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderStateShard
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.ShaderInstance
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBeginMode
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl.NeoGlShader
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlShader
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlShaderType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture2D
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlBlendShard
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlCullShard
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlDepthShard
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlDrawState
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlLayeringShard
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlTextureBinding
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource
import net.typho.big_shot_lib.api.client.rendering.util.NeoAtlas
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderType
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexFormat
import net.typho.big_shot_lib.api.math.vec.IVec3
import net.typho.big_shot_lib.api.math.vec.NeoVec3f
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import net.typho.big_shot_lib.api.util.resource.NeoResourceKey
import net.typho.big_shot_lib.impl.client.rendering.opengl.ShaderInstanceExtension
import net.typho.big_shot_lib.impl.util.getExtensionValue
import net.typho.big_shot_lib.impl.util.setExtensionValue
import org.joml.Vector3f
import sun.misc.Unsafe
import java.lang.reflect.Modifier
import java.util.Optional

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

    override fun getTexture(location: NeoIdentifier): GlTexture2D {
        return Minecraft.getInstance().textureManager.getTexture(location.mojang).getExtensionValue()
    }

    override fun getAtlas(location: NeoIdentifier): NeoAtlas {
        //? if <1.21.9 {
        return Minecraft.getInstance().modelManager.getAtlas(location.withPrefix("textures/atlas/").withSuffix(".png").mojang).getExtensionValue()
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

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getRegistry(key: NeoResourceKey<Registry<T>>): Registry<T>? {
        //? if <1.21.2 {
        return BuiltInRegistries.REGISTRY.get(key.location.mojang) as? Registry<T>
        //? } else {
        /*return BuiltInRegistries.REGISTRY.get(key.location.mojang).map { it as? Registry<T> }.orElse(null)
        *///? }
    }

    override fun mainWindowHandle(): Long {
        //? if <1.21.9 {
        return Minecraft.getInstance().window.window
        //? } else {
        /*return Minecraft.getInstance().window.handle()
        *///? }
    }

    override fun createShader(location: NeoIdentifier, type: GlShaderType, glId: Int): GlShader {
        return when (type) {
            GlShaderType.VERTEX -> Program(Program.Type.VERTEX, glId, location.toShortString()).getExtensionValue()
            GlShaderType.FRAGMENT -> Program(Program.Type.VERTEX, glId, location.toShortString()).getExtensionValue()
            else -> NeoGlShader(location, type, glId)
        }
    }

    override fun createProgram(
        location: NeoIdentifier,
        format: NeoVertexFormat,
        glId: Int
    ): GlProgram {
        val shader = UNSAFE.allocateInstance(ShaderInstance::class.java) as ShaderInstance
        (shader as ShaderInstanceExtension).`big_shot_lib$init`(location, format, glId)
        return shader.getExtensionValue<GlProgram>()
    }

    override fun createRenderType(
        location: NeoIdentifier,
        format: NeoVertexFormat,
        drawState: GlDrawState,
        defaultBufferSize: Int,
        mode: GlBeginMode,
        affectsCrumbling: Boolean,
        sortOnUpload: Boolean,
        isOutline: Boolean
    ): NeoRenderType {
        // TODO
        val blendStack = arrayListOf<BoundResource>()
        val layeringStack = arrayListOf<BoundResource>()

        val blend = drawState.blend.let {
            if (it is GlBlendShard.Enabled) {
                RenderStateShard.TransparencyStateShard(
                    "${it.function} ${it.equation} ${it.color}",
                    { blendStack.add(it.bind()) },
                    { blendStack.removeLast().unbind() }
                )
            } else {
                RenderStateShard.NO_TRANSPARENCY
            }
        }
        blend.setExtensionValue(drawState.blend)
        val mask = RenderStateShard.WriteMaskStateShard(
            drawState.colorMask.mask,
            drawState.depth.let { if (it is GlDepthShard.Enabled) it.mask else false }
        )
        val cull = if (drawState.cull is GlCullShard.Enabled) RenderStateShard.CULL else RenderStateShard.NO_CULL
        val depthTest = drawState.depth.let {
            if (it is GlDepthShard.Enabled) {
                RenderStateShard.DepthTestStateShard(
                    it.func.toString(),
                    it.func.glId
                )
            } else {
                RenderStateShard.NO_DEPTH_TEST
            }
        }
        val layering = drawState.layering.let {
            when (it) {
                is GlLayeringShard.EnabledPolygonOffset -> RenderStateShard.LayeringStateShard(
                    it.offset.toString(),
                    { layeringStack.add(it.bind()) },
                    { layeringStack.removeLast().unbind() }
                )

                is GlLayeringShard.EnabledViewOffset -> RenderStateShard.LayeringStateShard(
                    it.scale.toString(),
                    { layeringStack.add(it.bind()) },
                    { layeringStack.removeLast().unbind() }
                )

                else -> RenderStateShard.NO_LAYERING
            }
        }
        layering.setExtensionValue(drawState.layering)
        val lightmap = RenderStateShard.LightmapStateShard(drawState.lightmap.enabled)
        val overlay = RenderStateShard.OverlayStateShard(drawState.overlay.enabled)

        val textures = when (drawState.shader.textures.size) {
            0 -> RenderStateShard.NO_TEXTURE
            1 -> {
                val texture = drawState.shader.textures.values.first()

                if (texture is GlTextureBinding.FromLocation) {
                    RenderStateShard.TextureStateShard(
                        texture.location.mojang,
                        false,
                        true
                    )
                } else {
                    object : RenderStateShard.EmptyTextureStateShard(
                        {
                            RenderSystem._setShaderTexture(0, texture.texture.glId)
                        },
                        {
                        }
                    ) {
                        override fun cutoutTexture(): Optional<ResourceLocation> {
                            return Optional.ofNullable(texture.location?.mojang)
                        }
                    }
                }
            }
            else -> {
                if (drawState.shader.textures.values.all { it is GlTextureBinding.FromLocation }) {
                    val builder = RenderStateShard.MultiTextureStateShard.builder()

                    for (entry in drawState.shader.textures) {
                        builder.add(
                            entry.value.location!!.mojang,
                            false,
                            true
                        )
                    }

                    builder.build()
                } else {
                    object : RenderStateShard.EmptyTextureStateShard(
                        {
                            var i = 0 // TODO

                            for (entry in drawState.shader.textures) {
                                RenderSystem._setShaderTexture(i++, entry.value.texture.glId)
                            }
                        },
                        {
                        }
                    ) {
                        override fun cutoutTexture(): Optional<ResourceLocation> {
                            return Optional.ofNullable(drawState.shader.textures.values.firstOrNull { it.location != null }?.location?.mojang)
                        }
                    }
                }
            }
        }
        val shader = RenderStateShard.ShaderStateShard { drawState.shader.program?.getExtensionValue() }

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
                .setTextureState(textures)
                .setShaderState(shader)
                .createCompositeState(isOutline)
        ).getExtensionValue()
    }
}