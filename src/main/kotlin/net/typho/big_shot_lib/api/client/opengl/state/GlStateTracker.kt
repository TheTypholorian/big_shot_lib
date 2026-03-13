package net.typho.big_shot_lib.api.client.opengl.state

import net.typho.big_shot_lib.api.math.rect.AbstractRect2
import net.typho.big_shot_lib.api.util.NeoColor

interface GlStateTracker {
    operator fun <V> get(type: GlStateType<V>): V

    operator fun <V> set(type: GlStateType<V>, value: V)

    var arrayBufferBinding: Int
    var elementArrayBufferBinding: Int
    var pixelPackBufferBinding: Int
    var pixelUnpackBufferBinding: Int
    var textureBufferBinding: Int
    var copyReadBufferBinding: Int
    var copyWriteBufferBinding: Int
    var drawIndirectBufferBinding: Int
    var dispatchIndirectBufferBinding: Int
    var queryBufferBinding: Int
    var transformFeedbackBufferBinding: Int
    var uniformBufferBinding: Int
    var atomicCounterBufferBinding: Int
    var shaderStorageBufferBinding: Int

    var texture1DBinding: Int
    var texture2DBinding: Int
    var texture3DBinding: Int
    var texture1DArrayBinding: Int
    var texture2DArrayBinding: Int
    var textureRectangleBinding: Int
    var textureCubeMapBinding: Int
    var textureCubeMapArrayBinding: Int
    var texture2DMultisampleBinding: Int
    var texture2DMultisampleArrayBinding: Int

    var currentFramebuffer: Int
    var currentRenderBuffer: Int
    var currentShaderProgram: Int
    var currentVertexArray: Int

    var blendEnabled: Boolean
    var colorLogicOpEnabled: Boolean
    var cullFaceEnabled: Boolean
    var debugOutputEnabled: Boolean
    var debugOutputSynchronousEnabled: Boolean
    var depthClampEnabled: Boolean
    var depthEnabled: Boolean
    var ditherEnabled: Boolean
    var framebufferSRGBEnabled: Boolean
    var lineSmoothEnabled: Boolean
    var multisampleEnabled: Boolean
    var polygonOffsetEnabled: Boolean
    var polygonSmoothEnabled: Boolean
    var primitiveRestartEnabled: Boolean
    var primitiveRestartFixedIndexEnabled: Boolean
    var rasterizerDiscardEnabled: Boolean
    var sampleAlphaToCoverageEnabled: Boolean
    var sampleAlphaToOneEnabled: Boolean
    var sampleCoverageEnabled: Boolean
    var sampleShadingEnabled: Boolean
    var sampleMaskEnabled: Boolean
    var scissorEnabled: Boolean
    var stencilEnabled: Boolean
    var textureCubeMapSeamlessEnabled: Boolean
    var programPointSizeEnabled: Boolean

    var blendColor: NeoColor
    var blendEquation: BlendEquation
    var blendFunction: BlendFunction
    var colorMask: ColorMask
    var cullFace: CullFace
    var depthMask: Boolean
    var depthFunc: ComparisonFunc
    var polygonMode: PolygonMode
    var polygonOffset: PolygonOffset
    var scissor: AbstractRect2<Int, *, *>
    var stencilFunction: StencilFunction
    var stencilMask: Int
    var stencilOp: StencilOp
}