package net.typho.big_shot_lib.api.client.opengl.state

import net.typho.big_shot_lib.api.client.opengl.buffers.GlFramebuffer
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import net.typho.big_shot_lib.api.math.rect.NeoRect2i
import net.typho.big_shot_lib.api.util.NeoColor
import kotlin.reflect.KMutableProperty1

class GlStateType<V> internal constructor(
    @JvmField
    val default: () -> V,
    @JvmField
    val raw: KMutableProperty1<GlStateTracker, V>
) {
    private val stack = arrayListOf<V>()
    private var restoreTo: V? = null

    fun push(value: V, tracker: GlStateTracker = OpenGL.INSTANCE) {
        if (stack.isEmpty()) {
            restoreTo = raw.get(tracker)
        }

        if (stack.lastOrNull() != value) {
            raw.set(tracker, value)
        }

        stack.add(value)
    }

    fun pop(tracker: GlStateTracker = OpenGL.INSTANCE) {
        val removed = stack.removeLastOrNull() ?: throw IllegalStateException("Popped empty gl state stack")
        val current = currentValue()

        if (current == null) {
            raw.set(tracker, restoreTo ?: default())
        } else if (current != removed) {
            raw.set(tracker, current)
        }
    }

    fun rebind(tracker: GlStateTracker = OpenGL.INSTANCE) {
        currentValue()?.let { raw.set(tracker, it) }
    }

    fun currentValue(): V? = stack.lastOrNull()

    companion object {
        @JvmField
        val FRAMEBUFFER = GlStateType({ 0 }, GlStateTracker::currentFramebuffer)
        @JvmField
        val RENDER_BUFFER = GlStateType({ 0 }, GlStateTracker::currentRenderBuffer)
        @JvmField
        val SHADER_PROGRAM = GlStateType({ 0 }, GlStateTracker::currentShaderProgram)
        @JvmField
        val VERTEX_ARRAY = GlStateType({ 0 }, GlStateTracker::currentVertexArray)

        @JvmField
        val BLEND_COLOR = GlStateType({ NeoColor.FULL_ON }, GlStateTracker::blendColor)
        @JvmField
        val BLEND_EQUATION = GlStateType({ BlendEquation.ADD }, GlStateTracker::blendEquation)
        @JvmField
        val COLOR_MASK = GlStateType({ ColorMask.DEFAULT }, GlStateTracker::colorMask)
        @JvmField
        val CULL_FACE = GlStateType({ CullFace.DEFAULT }, GlStateTracker::cullFace)
        @JvmField
        val DEPTH_MASK = GlStateType({ true }, GlStateTracker::depthMask)
        @JvmField
        val DEPTH_FUNC = GlStateType({ ComparisonFunc.LEQUAL }, GlStateTracker::depthFunc)
        @JvmField
        val POLYGON_MODE = GlStateType({ PolygonMode.FILL }, GlStateTracker::polygonMode)
        @JvmField
        val POLYGON_OFFSET = GlStateType({ PolygonOffset(0f, 0f) }, GlStateTracker::polygonOffset)
        @JvmField
        val SCISSOR = GlStateType({ NeoRect2i(0, 0, GlFramebuffer.MAIN.width, GlFramebuffer.MAIN.height) }, GlStateTracker::scissor)
        @JvmField
        val STENCIL_FUNCTION = GlStateType({ StencilFunction(ComparisonFunc.ALWAYS, 0, 0) }, GlStateTracker::stencilFunction)
        @JvmField
        val STENCIL_MASK = GlStateType({ 0xFFFFFFFF.toInt() }, GlStateTracker::stencilMask)
        @JvmField
        val STENCIL_OP = GlStateType({ StencilOp(IntAction.KEEP, IntAction.KEEP, IntAction.KEEP) }, GlStateTracker::stencilOp)
        @JvmField
        val VIEWPORT = GlStateType({ NeoRect2i(0, 0, GlFramebuffer.MAIN.width, GlFramebuffer.MAIN.height) }, GlStateTracker::viewport)
    }
}