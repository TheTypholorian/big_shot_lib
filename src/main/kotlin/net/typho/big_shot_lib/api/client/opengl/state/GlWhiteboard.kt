package net.typho.big_shot_lib.api.client.opengl.state

import net.typho.big_shot_lib.api.client.opengl.util.BoundResource
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import net.typho.big_shot_lib.api.math.rect.AbstractRect2
import net.typho.big_shot_lib.api.util.NeoColor

class GlWhiteboard(
    @JvmField
    val tracker: GlStateTracker = OpenGL.INSTANCE
) : BoundResource, GlStateTracker {
    private val modified = Array<Any?>(GlStateType.registry.size) { null }

    private fun <V : Any> set(state: GlStateType<V>, value: V) {
        if (modified[state.id] == null) {
            modified[state.id] = value
        }

        state.raw.set(tracker, value)
    }

    override fun unbind() {
        @Suppress("UNCHECKED_CAST")
        fun <V : Any> set(state: GlStateType<V>, value: Any) {
            state.raw.set(tracker, value as V)
        }

        modified.forEachIndexed { index, value ->
            if (value != null) {
                set(GlStateType.registry[index], value)
            }
        }
        modified.fill(null)
    }

    override var arrayBufferBinding: Int
        get() = tracker.arrayBufferBinding
        set(value) = set(GlBufferType.ARRAY_BUFFER.state, value)
    override var elementArrayBufferBinding: Int
        get() = tracker.elementArrayBufferBinding
        set(value) = set(GlBufferType.ELEMENT_ARRAY_BUFFER.state, value)
    override var pixelPackBufferBinding: Int
        get() = tracker.pixelPackBufferBinding
        set(value) = set(GlBufferType.PIXEL_PACK_BUFFER.state, value)
    override var pixelUnpackBufferBinding: Int
        get() = tracker.pixelUnpackBufferBinding
        set(value) = set(GlBufferType.PIXEL_UNPACK_BUFFER.state, value)
    override var textureBufferBinding: Int
        get() = tracker.textureBufferBinding
        set(value) = set(GlBufferType.TEXTURE_BUFFER.state, value)
    override var copyReadBufferBinding: Int
        get() = tracker.copyReadBufferBinding
        set(value) = set(GlBufferType.COPY_READ_BUFFER.state, value)
    override var copyWriteBufferBinding: Int
        get() = tracker.copyWriteBufferBinding
        set(value) = set(GlBufferType.COPY_WRITE_BUFFER.state, value)
    override var drawIndirectBufferBinding: Int
        get() = tracker.drawIndirectBufferBinding
        set(value) = set(GlBufferType.DRAW_INDIRECT_BUFFER.state, value)
    override var dispatchIndirectBufferBinding: Int
        get() = tracker.dispatchIndirectBufferBinding
        set(value) = set(GlBufferType.DISPATCH_INDIRECT_BUFFER.state, value)
    override var queryBufferBinding: Int
        get() = tracker.queryBufferBinding
        set(value) = set(GlBufferType.QUERY_BUFFER.state, value)
    override var transformFeedbackBufferBinding: Int
        get() = tracker.transformFeedbackBufferBinding
        set(value) = set(GlBufferType.TRANSFORM_FEEDBACK_BUFFER.state, value)
    override var uniformBufferBinding: Int
        get() = tracker.uniformBufferBinding
        set(value) = set(GlBufferType.UNIFORM_BUFFER.state, value)
    override var atomicCounterBufferBinding: Int
        get() = tracker.atomicCounterBufferBinding
        set(value) = set(GlBufferType.ATOMIC_COUNTER_BUFFER.state, value)
    override var shaderStorageBufferBinding: Int
        get() = tracker.shaderStorageBufferBinding
        set(value) = set(GlBufferType.SHADER_STORAGE_BUFFER.state, value)

    override var blendColor: NeoColor
        get() = tracker.blendColor
        set(value) = set(GlStateType.BLEND_COLOR, value)
    override var blendEquation: BlendEquation
        get() = tracker.blendEquation
        set(value) = set(GlStateType.BLEND_EQUATION, value)
    override var blendFunction: BlendFunction
        get() = tracker.blendFunction
        set(value) = set(GlStateType.BLEND_FUNCTION, value)
    override var colorMask: ColorMask
        get() = tracker.colorMask
        set(value) = set(GlStateType.COLOR_MASK, value)
    override var cullFace: CullFace
        get() = tracker.cullFace
        set(value) = set(GlStateType.CULL_FACE, value)
    override var depthMask: Boolean
        get() = tracker.depthMask
        set(value) = set(GlStateType.DEPTH_MASK, value)
    override var depthFunc: ComparisonFunc
        get() = tracker.depthFunc
        set(value) = set(GlStateType.DEPTH_FUNC, value)
    override var polygonMode: PolygonMode
        get() = tracker.polygonMode
        set(value) = set(GlStateType.POLYGON_MODE, value)
    override var polygonOffset: PolygonOffset
        get() = tracker.polygonOffset
        set(value) = set(GlStateType.POLYGON_OFFSET, value)
    override var scissor: AbstractRect2<Int, *, *>
        get() = tracker.scissor
        set(value) = set(GlStateType.SCISSOR, value)
    override var stencilFunction: StencilFunction
        get() = tracker.stencilFunction
        set(value) = set(GlStateType.STENCIL_FUNCTION, value)
    override var stencilMask: Int
        get() = tracker.stencilMask
        set(value) = set(GlStateType.STENCIL_MASK, value)
    override var stencilOp: StencilOp
        get() = tracker.stencilOp
        set(value) = set(GlStateType.STENCIL_OP, value)
    override var viewport: AbstractRect2<Int, *, *>
        get() = tracker.viewport
        set(value) = set(GlStateType.VIEWPORT, value)

    override var blendEnabled: Boolean
        get() = tracker.blendEnabled
        set(value) = set(GlFlag.BLEND.state, value)
    override var colorLogicOpEnabled: Boolean
        get() = tracker.colorLogicOpEnabled
        set(value) = set(GlFlag.COLOR_LOGIC_OP.state, value)
    override var cullFaceEnabled: Boolean
        get() = tracker.cullFaceEnabled
        set(value) = set(GlFlag.CULL_FACE.state, value)
    override var debugOutputEnabled: Boolean
        get() = tracker.debugOutputEnabled
        set(value) = set(GlFlag.DEBUG_OUTPUT.state, value)
    override var debugOutputSynchronousEnabled: Boolean
        get() = tracker.debugOutputSynchronousEnabled
        set(value) = set(GlFlag.DEBUG_OUTPUT_SYNCHRONOUS.state, value)
    override var depthClampEnabled: Boolean
        get() = tracker.depthClampEnabled
        set(value) = set(GlFlag.DEPTH_CLAMP.state, value)
    override var depthEnabled: Boolean
        get() = tracker.depthEnabled
        set(value) = set(GlFlag.DEPTH_TEST.state, value)
    override var ditherEnabled: Boolean
        get() = tracker.ditherEnabled
        set(value) = set(GlFlag.DITHER.state, value)
    override var framebufferSRGBEnabled: Boolean
        get() = tracker.framebufferSRGBEnabled
        set(value) = set(GlFlag.FRAMEBUFFER_SRGB.state, value)
    override var lineSmoothEnabled: Boolean
        get() = tracker.lineSmoothEnabled
        set(value) = set(GlFlag.LINE_SMOOTH.state, value)
    override var multisampleEnabled: Boolean
        get() = tracker.multisampleEnabled
        set(value) = set(GlFlag.MULTISAMPLE.state, value)
    override var polygonOffsetEnabled: Boolean
        get() = tracker.polygonOffsetEnabled
        set(value) = set(GlFlag.POLYGON_OFFSET.state, value)
    override var polygonSmoothEnabled: Boolean
        get() = tracker.polygonSmoothEnabled
        set(value) = set(GlFlag.POLYGON_SMOOTH.state, value)
    override var primitiveRestartEnabled: Boolean
        get() = tracker.primitiveRestartEnabled
        set(value) = set(GlFlag.PRIMITIVE_RESTART.state, value)
    override var primitiveRestartFixedIndexEnabled: Boolean
        get() = tracker.primitiveRestartFixedIndexEnabled
        set(value) = set(GlFlag.PRIMITIVE_RESTART_FIXED_INDEX.state, value)
    override var rasterizerDiscardEnabled: Boolean
        get() = tracker.rasterizerDiscardEnabled
        set(value) = set(GlFlag.RASTERIZER_DISCARD.state, value)
    override var sampleAlphaToCoverageEnabled: Boolean
        get() = tracker.sampleAlphaToCoverageEnabled
        set(value) = set(GlFlag.SAMPLE_ALPHA_TO_COVERAGE.state, value)
    override var sampleAlphaToOneEnabled: Boolean
        get() = tracker.sampleAlphaToOneEnabled
        set(value) = set(GlFlag.SAMPLE_ALPHA_TO_ONE.state, value)
    override var sampleCoverageEnabled: Boolean
        get() = tracker.sampleCoverageEnabled
        set(value) = set(GlFlag.SAMPLE_COVERAGE.state, value)
    override var sampleShadingEnabled: Boolean
        get() = tracker.sampleShadingEnabled
        set(value) = set(GlFlag.SAMPLE_SHADING.state, value)
    override var sampleMaskEnabled: Boolean
        get() = tracker.sampleMaskEnabled
        set(value) = set(GlFlag.SAMPLE_MASK.state, value)
    override var scissorEnabled: Boolean
        get() = tracker.scissorEnabled
        set(value) = set(GlFlag.SCISSOR_TEST.state, value)
    override var stencilEnabled: Boolean
        get() = tracker.stencilEnabled
        set(value) = set(GlFlag.STENCIL_TEST.state, value)
    override var textureCubeMapSeamlessEnabled: Boolean
        get() = tracker.textureCubeMapSeamlessEnabled
        set(value) = set(GlFlag.TEXTURE_CUBE_MAP_SEAMLESS.state, value)
    override var programPointSizeEnabled: Boolean
        get() = tracker.programPointSizeEnabled
        set(value) = set(GlFlag.PROGRAM_POINT_SIZE.state, value)

    override var texture1DBinding: Int
        get() = tracker.texture1DBinding
        set(value) = set(GlTextureType.TEXTURE_1D.state, value)
    override var texture2DBinding: Int
        get() = tracker.texture2DBinding
        set(value) = set(GlTextureType.TEXTURE_2D.state, value)
    override var texture3DBinding: Int
        get() = tracker.texture3DBinding
        set(value) = set(GlTextureType.TEXTURE_3D.state, value)
    override var texture1DArrayBinding: Int
        get() = tracker.texture1DArrayBinding
        set(value) = set(GlTextureType.TEXTURE_1D_ARRAY.state, value)
    override var texture2DArrayBinding: Int
        get() = tracker.texture2DArrayBinding
        set(value) = set(GlTextureType.TEXTURE_2D_ARRAY.state, value)
    override var textureRectangleBinding: Int
        get() = tracker.textureRectangleBinding
        set(value) = set(GlTextureType.TEXTURE_RECTANGLE.state, value)
    override var textureCubeMapBinding: Int
        get() = tracker.textureCubeMapBinding
        set(value) = set(GlTextureType.TEXTURE_CUBE_MAP.state, value)
    override var textureCubeMapArrayBinding: Int
        get() = tracker.textureCubeMapArrayBinding
        set(value) = set(GlTextureType.TEXTURE_CUBE_MAP_ARRAY.state, value)
    override var texture2DMultisampleBinding: Int
        get() = tracker.texture2DMultisampleBinding
        set(value) = set(GlTextureType.TEXTURE_2D_MULTISAMPLE.state, value)
    override var texture2DMultisampleArrayBinding: Int
        get() = tracker.texture2DMultisampleArrayBinding
        set(value) = set(GlTextureType.TEXTURE_2D_MULTISAMPLE_ARRAY.state, value)

    override var currentFramebuffer: Int
        get() = tracker.currentFramebuffer
        set(value) = set(GlStateType.FRAMEBUFFER, value)
    override var currentRenderBuffer: Int
        get() = tracker.currentRenderBuffer
        set(value) = set(GlStateType.RENDER_BUFFER, value)
    override var currentShaderProgram: Int
        get() = tracker.currentShaderProgram
        set(value) = set(GlStateType.SHADER_PROGRAM, value)
    override var currentVertexArray: Int
        get() = tracker.currentVertexArray
        set(value) = set(GlStateType.VERTEX_ARRAY, value)
}