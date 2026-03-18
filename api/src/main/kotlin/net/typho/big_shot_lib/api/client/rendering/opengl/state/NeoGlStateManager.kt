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

    companion object {
        @JvmField
        val INSTANCE = NeoGlStateManager::class.loadService()
    }
}