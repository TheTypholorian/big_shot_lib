package net.typho.big_shot_lib.impl.client.rendering.common

import com.mojang.blaze3d.GpuFormat
import com.mojang.blaze3d.PrimitiveTopology
import com.mojang.blaze3d.pipeline.BindGroupLayout
import com.mojang.blaze3d.pipeline.BlendFunction
import com.mojang.blaze3d.pipeline.ColorTargetState
import com.mojang.blaze3d.pipeline.DepthStencilState
import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.platform.CompareOp
import com.mojang.blaze3d.shaders.UniformType
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.renderer.LayeringTransform
import net.minecraft.client.renderer.RenderSetup
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.Identifier
import net.minecraft.world.level.saveddata.maps.MapItemSavedData.type
import net.typho.big_shot_lib.api.client.rendering.common.GpuBuffer
import net.typho.big_shot_lib.api.client.rendering.common.GpuDrawSettings
import net.typho.big_shot_lib.api.client.rendering.common.GpuObjectName
import net.typho.big_shot_lib.api.client.rendering.common.GpuTexture
import net.typho.big_shot_lib.api.client.rendering.common.IGpuObjects
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuBlendFunction
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuBufferUsage
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuTextureFormat
import net.typho.big_shot_lib.api.client.rendering.common.constant.GpuTextureUsage
import net.typho.big_shot_lib.api.util.Extension.Companion.castTo
import net.typho.big_shot_lib.api.util.buffer.MemoryPointer

object GpuObjectsImpl : IGpuObjects {
    override fun renderType(
        location: Identifier,
        format: VertexFormat,
        drawState: GpuDrawSettings.Builder,
        defaultBufferSize: Int,
        affectsCrumbling: Boolean,
        sortOnUpload: Boolean,
        isOutline: Boolean
    ): RenderType {
        //? if <1.21.11 {
        /*
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
            VertexFormat.Mode.QUADS,
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
        *///? } else {
        val pipeline = RenderPipeline.builder()

        pipeline.withLocation(location)

        pipeline.withVertexShader(drawState.vertexShader ?: throw NullPointerException("Must specify vertex shader in render type $location"))
        pipeline.withFragmentShader(drawState.fragmentShader ?: throw NullPointerException("Must specify fragment shader in render type $location"))

        pipeline.withDepthStencilState(
            DepthStencilState(
                drawState.depth?.castTo() ?: CompareOp.ALWAYS_PASS,
                drawState.writeDepth
            )
        )
        drawState.blend?.let { blend ->
            pipeline.withColorTargetState(
                ColorTargetState(
                    when (blend) {
                        is GpuBlendFunction.Basic -> BlendFunction(
                            blend.src.castTo(),
                            blend.dest.castTo(),
                            blend.src.castTo(),
                            blend.dest.castTo()
                        )

                        is GpuBlendFunction.Separate -> BlendFunction(
                            blend.src.castTo(),
                            blend.dest.castTo(),
                            blend.srcA.castTo(),
                            blend.destA.castTo()
                        )
                    }
                )
            )
        } ?: pipeline.withColorTargetState(ColorTargetState.DEFAULT)
        pipeline.withCull(drawState.cull)

        val layout = BindGroupLayout.builder()

        drawState.samplers.forEach { layout.withSampler(it) }
        drawState.uniforms.forEach { layout.withUniform(it, UniformType.UNIFORM_BUFFER) }
        drawState.texelBuffers.forEach { buffer ->
            layout.withUniform(
                buffer.name,
                UniformType.TEXEL_BUFFER,
                GpuFormat.entries.firstOrNull { it.componentCount() == buffer.components && it.componentType() == buffer.type } ?: throw IllegalArgumentException("Invalid component count ${buffer.components} and type ${buffer.type}")
            )
        }

        pipeline.withBindGroupLayout(layout.build())
        pipeline.withPrimitiveTopology(PrimitiveTopology.QUADS)

        pipeline.withShaderDefine("USE_VERTEX_COMPRESSION")
        pipeline.withShaderDefine("USE_FOG")
        pipeline.withShaderDefine("ALPHA_CUTOUT", 0.5f)

        pipeline.withVertexBinding(0, format)

        val setup = RenderSetup.builder(pipeline.build())

        if (affectsCrumbling) {
            setup.affectsCrumbling()
        }

        if (sortOnUpload) {
            setup.sortOnUpload()
        }

        // TODO
        setup.setOutline(if (isOutline) RenderSetup.OutlineProperty.IS_OUTLINE else RenderSetup.OutlineProperty.AFFECTS_OUTLINE)

        if (drawState.lightmap) {
            setup.useLightmap()
        }

        if (drawState.overlay) {
            setup.useOverlay()
        }

        setup.setLayeringTransform(if (drawState.zOffset) LayeringTransform.VIEW_OFFSET_Z_LAYERING else LayeringTransform.NO_LAYERING)

        return RenderType.create(
            location.toShortString(),
            setup.createRenderSetup()
        )
        //? }
    }

    override fun texture(
        name: GpuObjectName?,
        width: Int,
        height: Int,
        usage: GpuTextureUsage,
        format: GpuTextureFormat,
        blur: Boolean,
        mipmap: Boolean
    ): GpuTexture {
        return RenderSystem.getDevice().createTexture(name, usage.flags, format.castTo(), width, height, 1, 0)
    }

    override fun buffer(
        name: GpuObjectName?,
        size: Long,
        usage: GpuBufferUsage
    ): GpuBuffer {
        return RenderSystem.getDevice().createBuffer(name, usage.flags, size)
    }

    override fun buffer(name: GpuObjectName?, usage: GpuBufferUsage, data: MemoryPointer): GpuBuffer {
        return RenderSystem.getDevice().createBuffer(name, usage.flags, data.asByteBuffer())
    }
}