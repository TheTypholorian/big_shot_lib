package net.typho.big_shot_lib.api.client.opengl.state

import kotlin.reflect.KMutableProperty1

class GlStateType<V : Any> internal constructor(
    @JvmField
    val raw: KMutableProperty1<GlStateTracker, V>
) {
    @JvmField
    val id = registry.size

    init {
        registry += this
    }

    companion object {
        var registry: List<GlStateType<*>> = listOf()
            private set

        @JvmField
        val FRAMEBUFFER = GlStateType(GlStateTracker::currentFramebuffer)
        @JvmField
        val RENDER_BUFFER = GlStateType(GlStateTracker::currentRenderBuffer)
        @JvmField
        val SHADER_PROGRAM = GlStateType(GlStateTracker::currentShaderProgram)
        @JvmField
        val VERTEX_ARRAY = GlStateType(GlStateTracker::currentVertexArray)

        @JvmField
        val BLEND_COLOR = GlStateType(GlStateTracker::blendColor)
        @JvmField
        val BLEND_EQUATION = GlStateType(GlStateTracker::blendEquation)
        @JvmField
        val BLEND_FUNCTION = GlStateType(GlStateTracker::blendFunction)
        @JvmField
        val COLOR_MASK = GlStateType(GlStateTracker::colorMask)
        @JvmField
        val CULL_FACE = GlStateType(GlStateTracker::cullFace)
        @JvmField
        val DEPTH_MASK = GlStateType(GlStateTracker::depthMask)
        @JvmField
        val DEPTH_FUNC = GlStateType(GlStateTracker::depthFunc)
        @JvmField
        val POLYGON_MODE = GlStateType(GlStateTracker::polygonMode)
        @JvmField
        val POLYGON_OFFSET = GlStateType(GlStateTracker::polygonOffset)
        @JvmField
        val SCISSOR = GlStateType(GlStateTracker::scissor)
        @JvmField
        val STENCIL_FUNCTION = GlStateType(GlStateTracker::stencilFunction)
        @JvmField
        val STENCIL_MASK = GlStateType(GlStateTracker::stencilMask)
        @JvmField
        val STENCIL_OP = GlStateType(GlStateTracker::stencilOp)
        @JvmField
        val VIEWPORT = GlStateType(GlStateTracker::viewport)
    }
}