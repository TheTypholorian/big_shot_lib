package net.typho.big_shot_lib.api.client.opengl.state

import net.typho.big_shot_lib.api.math.rect.AbstractRect2
import net.typho.big_shot_lib.api.util.NeoColor
import kotlin.reflect.KProperty

interface GlStateTracker {
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
    var viewport: AbstractRect2<Int, *, *>

    open class Redirect(
        private val other: GlStateTracker
    ) : GlStateTracker {
        override var arrayBufferBinding: Int by other::arrayBufferBinding
        override var elementArrayBufferBinding: Int by other::elementArrayBufferBinding
        override var pixelPackBufferBinding: Int by other::pixelPackBufferBinding
        override var pixelUnpackBufferBinding: Int by other::pixelUnpackBufferBinding
        override var textureBufferBinding: Int by other::textureBufferBinding
        override var copyReadBufferBinding: Int by other::copyReadBufferBinding
        override var copyWriteBufferBinding: Int by other::copyWriteBufferBinding
        override var drawIndirectBufferBinding: Int by other::drawIndirectBufferBinding
        override var dispatchIndirectBufferBinding: Int by other::dispatchIndirectBufferBinding
        override var queryBufferBinding: Int by other::queryBufferBinding
        override var transformFeedbackBufferBinding: Int by other::transformFeedbackBufferBinding
        override var uniformBufferBinding: Int by other::uniformBufferBinding
        override var atomicCounterBufferBinding: Int by other::atomicCounterBufferBinding
        override var shaderStorageBufferBinding: Int by other::shaderStorageBufferBinding
        override var texture1DBinding: Int by other::texture1DBinding
        override var texture2DBinding: Int by other::texture2DBinding
        override var texture3DBinding: Int by other::texture3DBinding
        override var texture1DArrayBinding: Int by other::texture1DArrayBinding
        override var texture2DArrayBinding: Int by other::texture2DArrayBinding
        override var textureRectangleBinding: Int by other::textureRectangleBinding
        override var textureCubeMapBinding: Int by other::textureCubeMapBinding
        override var textureCubeMapArrayBinding: Int by other::textureCubeMapArrayBinding
        override var texture2DMultisampleBinding: Int by other::texture2DMultisampleBinding
        override var texture2DMultisampleArrayBinding: Int by other::texture2DMultisampleArrayBinding
        override var currentFramebuffer: Int by other::currentFramebuffer
        override var currentRenderBuffer: Int by other::currentRenderBuffer
        override var currentShaderProgram: Int by other::currentShaderProgram
        override var currentVertexArray: Int by other::currentVertexArray
        override var blendEnabled: Boolean by other::blendEnabled
        override var colorLogicOpEnabled: Boolean by other::colorLogicOpEnabled
        override var cullFaceEnabled: Boolean by other::cullFaceEnabled
        override var debugOutputEnabled: Boolean by other::debugOutputEnabled
        override var debugOutputSynchronousEnabled: Boolean by other::debugOutputSynchronousEnabled
        override var depthClampEnabled: Boolean by other::depthClampEnabled
        override var depthEnabled: Boolean by other::depthEnabled
        override var ditherEnabled: Boolean by other::ditherEnabled
        override var framebufferSRGBEnabled: Boolean by other::framebufferSRGBEnabled
        override var lineSmoothEnabled: Boolean by other::lineSmoothEnabled
        override var multisampleEnabled: Boolean by other::multisampleEnabled
        override var polygonOffsetEnabled: Boolean by other::polygonOffsetEnabled
        override var polygonSmoothEnabled: Boolean by other::polygonSmoothEnabled
        override var primitiveRestartEnabled: Boolean by other::primitiveRestartEnabled
        override var primitiveRestartFixedIndexEnabled: Boolean by other::primitiveRestartFixedIndexEnabled
        override var rasterizerDiscardEnabled: Boolean by other::rasterizerDiscardEnabled
        override var sampleAlphaToCoverageEnabled: Boolean by other::sampleAlphaToCoverageEnabled
        override var sampleAlphaToOneEnabled: Boolean by other::sampleAlphaToOneEnabled
        override var sampleCoverageEnabled: Boolean by other::sampleCoverageEnabled
        override var sampleShadingEnabled: Boolean by other::sampleShadingEnabled
        override var sampleMaskEnabled: Boolean by other::sampleMaskEnabled
        override var scissorEnabled: Boolean by other::scissorEnabled
        override var stencilEnabled: Boolean by other::stencilEnabled
        override var textureCubeMapSeamlessEnabled: Boolean by other::textureCubeMapSeamlessEnabled
        override var programPointSizeEnabled: Boolean by other::programPointSizeEnabled
        override var blendColor: NeoColor by other::blendColor
        override var blendEquation: BlendEquation by other::blendEquation
        override var blendFunction: BlendFunction by other::blendFunction
        override var colorMask: ColorMask by other::colorMask
        override var cullFace: CullFace by other::cullFace
        override var depthMask: Boolean by other::depthMask
        override var depthFunc: ComparisonFunc by other::depthFunc
        override var polygonMode: PolygonMode by other::polygonMode
        override var polygonOffset: PolygonOffset by other::polygonOffset
        override var scissor: AbstractRect2<Int, *, *> by other::scissor
        override var stencilFunction: StencilFunction by other::stencilFunction
        override var stencilMask: Int by other::stencilMask
        override var stencilOp: StencilOp by other::stencilOp
        override var viewport: AbstractRect2<Int, *, *> by other::viewport
    }

    abstract class Compact : GlStateTracker {
        protected inner class Delegate<V>(
            @JvmField
            val state: GlStateType<V>
        ) {
            operator fun getValue(tracker: Compact, property: KProperty<*>): V {
                return state
            }

            operator fun setValue(tracker: Compact, property: KProperty<*>, value: V) {
            }
        }

        abstract operator fun <V> get(state: GlStateType<V>): V

        abstract operator fun <V> set(state: GlStateType<V>, value: V)

        override var arrayBufferBinding: Int by Delegate(GlBufferType.ARRAY_BUFFER.state)
        override var elementArrayBufferBinding: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var pixelPackBufferBinding: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var pixelUnpackBufferBinding: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var textureBufferBinding: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var copyReadBufferBinding: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var copyWriteBufferBinding: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var drawIndirectBufferBinding: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var dispatchIndirectBufferBinding: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var queryBufferBinding: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var transformFeedbackBufferBinding: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var uniformBufferBinding: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var atomicCounterBufferBinding: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var shaderStorageBufferBinding: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var texture1DBinding: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var texture2DBinding: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var texture3DBinding: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var texture1DArrayBinding: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var texture2DArrayBinding: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var textureRectangleBinding: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var textureCubeMapBinding: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var textureCubeMapArrayBinding: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var texture2DMultisampleBinding: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var texture2DMultisampleArrayBinding: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var currentFramebuffer: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var currentRenderBuffer: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var currentShaderProgram: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var currentVertexArray: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var blendEnabled: Boolean
            get() = TODO("Not yet implemented")
            set(value) {}
        override var colorLogicOpEnabled: Boolean
            get() = TODO("Not yet implemented")
            set(value) {}
        override var cullFaceEnabled: Boolean
            get() = TODO("Not yet implemented")
            set(value) {}
        override var debugOutputEnabled: Boolean
            get() = TODO("Not yet implemented")
            set(value) {}
        override var debugOutputSynchronousEnabled: Boolean
            get() = TODO("Not yet implemented")
            set(value) {}
        override var depthClampEnabled: Boolean
            get() = TODO("Not yet implemented")
            set(value) {}
        override var depthEnabled: Boolean
            get() = TODO("Not yet implemented")
            set(value) {}
        override var ditherEnabled: Boolean
            get() = TODO("Not yet implemented")
            set(value) {}
        override var framebufferSRGBEnabled: Boolean
            get() = TODO("Not yet implemented")
            set(value) {}
        override var lineSmoothEnabled: Boolean
            get() = TODO("Not yet implemented")
            set(value) {}
        override var multisampleEnabled: Boolean
            get() = TODO("Not yet implemented")
            set(value) {}
        override var polygonOffsetEnabled: Boolean
            get() = TODO("Not yet implemented")
            set(value) {}
        override var polygonSmoothEnabled: Boolean
            get() = TODO("Not yet implemented")
            set(value) {}
        override var primitiveRestartEnabled: Boolean
            get() = TODO("Not yet implemented")
            set(value) {}
        override var primitiveRestartFixedIndexEnabled: Boolean
            get() = TODO("Not yet implemented")
            set(value) {}
        override var rasterizerDiscardEnabled: Boolean
            get() = TODO("Not yet implemented")
            set(value) {}
        override var sampleAlphaToCoverageEnabled: Boolean
            get() = TODO("Not yet implemented")
            set(value) {}
        override var sampleAlphaToOneEnabled: Boolean
            get() = TODO("Not yet implemented")
            set(value) {}
        override var sampleCoverageEnabled: Boolean
            get() = TODO("Not yet implemented")
            set(value) {}
        override var sampleShadingEnabled: Boolean
            get() = TODO("Not yet implemented")
            set(value) {}
        override var sampleMaskEnabled: Boolean
            get() = TODO("Not yet implemented")
            set(value) {}
        override var scissorEnabled: Boolean
            get() = TODO("Not yet implemented")
            set(value) {}
        override var stencilEnabled: Boolean
            get() = TODO("Not yet implemented")
            set(value) {}
        override var textureCubeMapSeamlessEnabled: Boolean
            get() = TODO("Not yet implemented")
            set(value) {}
        override var programPointSizeEnabled: Boolean
            get() = TODO("Not yet implemented")
            set(value) {}
        override var blendColor: NeoColor
            get() = TODO("Not yet implemented")
            set(value) {}
        override var blendEquation: BlendEquation
            get() = TODO("Not yet implemented")
            set(value) {}
        override var blendFunction: BlendFunction
            get() = TODO("Not yet implemented")
            set(value) {}
        override var colorMask: ColorMask
            get() = TODO("Not yet implemented")
            set(value) {}
        override var cullFace: CullFace
            get() = TODO("Not yet implemented")
            set(value) {}
        override var depthMask: Boolean
            get() = TODO("Not yet implemented")
            set(value) {}
        override var depthFunc: ComparisonFunc
            get() = TODO("Not yet implemented")
            set(value) {}
        override var polygonMode: PolygonMode
            get() = TODO("Not yet implemented")
            set(value) {}
        override var polygonOffset: PolygonOffset
            get() = TODO("Not yet implemented")
            set(value) {}
        override var scissor: AbstractRect2<Int, *, *>
            get() = TODO("Not yet implemented")
            set(value) {}
        override var stencilFunction: StencilFunction
            get() = TODO("Not yet implemented")
            set(value) {}
        override var stencilMask: Int
            get() = TODO("Not yet implemented")
            set(value) {}
        override var stencilOp: StencilOp
            get() = TODO("Not yet implemented")
            set(value) {}
        override var viewport: AbstractRect2<Int, *, *>
            get() = TODO("Not yet implemented")
            set(value) {}
    }
}