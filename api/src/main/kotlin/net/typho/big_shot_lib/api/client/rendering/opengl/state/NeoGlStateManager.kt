package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.BigShotApi.loadService
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.*
import net.typho.big_shot_lib.api.math.rect.AbstractRect2
import net.typho.big_shot_lib.api.util.EnumArrayMap
import net.typho.big_shot_lib.api.util.NeoColor

interface NeoGlStateManager {
    val buffers: EnumArrayMap<GlBufferTarget, GlStateStack<Int>>
    val program: GlStateStack<Int>
    val programPipeline: GlStateStack<Int>
    val vertexArray: GlStateStack<Int>
    val textures: EnumArrayMap<GlTextureTarget, GlStateStack<Int>>
    val renderbuffer: GlStateStack<Int>
    val framebuffer: GlStateStack<Int>

    var activeTexture: Int

    val blendColor: GlStateStack<NeoColor>
    val blendEquation: GlStateStack<GlBlendEquation>
    val blendFunction: GlStateStack<BlendFunction>
    val colorMask: GlStateStack<ColorMask>
    val cullFace: GlStateStack<GlCullFace>
    val depthMask: GlStateStack<Boolean>
    val depthFunc: GlStateStack<GlAlphaFunction>
    val polygonMode: GlStateStack<GlPolygonMode>
    val polygonOffset: GlStateStack<PolygonOffset>
    val scissor: GlStateStack<AbstractRect2<Int, *, *>>
    val stencilFunction: GlStateStack<StencilFunction>
    val stencilMask: GlStateStack<Int>
    val stencilOp: GlStateStack<StencilOp>
    val viewport: GlStateStack<AbstractRect2<Int, *, *>>

    val blendEnabled: GlStateStack<Boolean>
    val colorLogicOpEnabled: GlStateStack<Boolean>
    val cullFaceEnabled: GlStateStack<Boolean>
    val debugOutputEnabled: GlStateStack<Boolean>
    val debugOutputSynchronousEnabled: GlStateStack<Boolean>
    val depthClampEnabled: GlStateStack<Boolean>
    val depthEnabled: GlStateStack<Boolean>
    val ditherEnabled: GlStateStack<Boolean>
    val framebufferSRGBEnabled: GlStateStack<Boolean>
    val lineSmoothEnabled: GlStateStack<Boolean>
    val multisampleEnabled: GlStateStack<Boolean>
    val polygonOffsetEnabled: GlStateStack<Boolean>
    val polygonSmoothEnabled: GlStateStack<Boolean>
    val primitiveRestartEnabled: GlStateStack<Boolean>
    val primitiveRestartFixedIndexEnabled: GlStateStack<Boolean>
    val rasterizerDiscardEnabled: GlStateStack<Boolean>
    val sampleAlphaToCoverageEnabled: GlStateStack<Boolean>
    val sampleAlphaToOneEnabled: GlStateStack<Boolean>
    val sampleCoverageEnabled: GlStateStack<Boolean>
    val sampleShadingEnabled: GlStateStack<Boolean>
    val sampleMaskEnabled: GlStateStack<Boolean>
    val scissorEnabled: GlStateStack<Boolean>
    val stencilEnabled: GlStateStack<Boolean>
    val textureCubeMapSeamlessEnabled: GlStateStack<Boolean>
    val programPointSizeEnabled: GlStateStack<Boolean>

    companion object {
        @JvmField
        val INSTANCE = NeoGlStateManager::class.loadService()
    }
}