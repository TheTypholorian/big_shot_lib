package net.typho.big_shot_lib.api.client.opengl.state

import net.typho.big_shot_lib.api.client.opengl.buffers.GlFramebuffer
import net.typho.big_shot_lib.api.client.opengl.util.GlBinder
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import net.typho.big_shot_lib.api.math.rect.NeoRect2i
import net.typho.big_shot_lib.api.util.NeoColor
import java.util.*

open class GlStateStack<V>(
    @JvmField
    val name: String,
    @JvmField
    val bind: (value: V?) -> Unit,
    @JvmField
    val query: () -> V
) : GlBinder<V> {
    companion object {
        @JvmField
        val blendColor = GlStateStack(
            "BLEND_COLOR",
            { OpenGL.INSTANCE.blendColor(it ?: NeoColor.FULL_ON) },
            OpenGL.INSTANCE::getBlendColor
        )
        @JvmField
        val blendEquation = GlStateStack(
            "BLEND_EQUATION",
            { OpenGL.INSTANCE.blendEquation(it ?: BlendEquation.ADD) },
            OpenGL.INSTANCE::getBlendEquation
        )
        @JvmField
        val blendFunc = GlStateStack<BlendFunction>(
            "BLEND_FUNCTION",
            { (it ?: BlendFunction.DEFAULT).bind() },
            OpenGL.INSTANCE::getBlendFunctionSeparate
        )
        @JvmField
        val colorMask = GlStateStack(
            "COLOR_MASK",
            { OpenGL.INSTANCE.colorMask(it ?: ColorMask.DEFAULT) },
            OpenGL.INSTANCE::getColorMask
        )
        @JvmField
        val cullFace = GlStateStack(
            "CULL_FACE",
            { OpenGL.INSTANCE.cullFace(it ?: CullFace.BACK) },
            OpenGL.INSTANCE::getCullFace
        )
        @JvmField
        val depthMask = GlStateStack(
            "COLOR_MASK",
            { OpenGL.INSTANCE.depthMask(it ?: true) },
            OpenGL.INSTANCE::getDepthMask
        )
        @JvmField
        val depthFunc = GlStateStack(
            "DEPTH_FUNC",
            { OpenGL.INSTANCE.depthFunc(it ?: ComparisonFunc.LEQUAL) },
            OpenGL.INSTANCE::getDepthFunc
        )
        @JvmField
        val polygonMode = GlStateStack(
            "POLYGON_MODE",
            { OpenGL.INSTANCE.polygonMode(it ?: PolygonMode.FILL) },
            OpenGL.INSTANCE::getPolygonMode
        )
        @JvmField
        val polygonOffset = GlStateStack(
            "POLYGON_OFFSET",
            { OpenGL.INSTANCE.polygonOffset(it ?: PolygonOffset(0f, 0f)) },
            OpenGL.INSTANCE::getPolygonOffset
        )
        @JvmField
        val stencilFunc = GlStateStack(
            "STENCIL_FUNC",
            { OpenGL.INSTANCE.stencilFunc(it ?: StencilFunc(ComparisonFunc.ALWAYS, 0, 0)) },
            OpenGL.INSTANCE::getStencilFunc
        )
        @JvmField
        val stencilMask = GlStateStack(
            "STENCIL_MASK",
            { OpenGL.INSTANCE.stencilMask(it ?: 0xFFFFFFFF.toInt()) },
            OpenGL.INSTANCE::getStencilMask
        )
        @JvmField
        val stencilOp = GlStateStack(
            "STENCIL_MASK",
            { OpenGL.INSTANCE.stencilOp(it ?: StencilOp(IntAction.KEEP, IntAction.KEEP, IntAction.KEEP)) },
            OpenGL.INSTANCE::getStencilOp
        )
        @JvmField
        val viewport = GlStateStack(
            "VIEWPORT",
            { OpenGL.INSTANCE.viewport(it ?: NeoRect2i(0, 0, GlFramebuffer.MAIN.width, GlFramebuffer.MAIN.height)) },
            OpenGL.INSTANCE::getViewport
        )
    }

    @JvmField
    protected val bound = LinkedList<V>()
    @JvmField
    protected var restoreTo: V? = null

    override fun bind(value: V, pushStack: Boolean) {
        if (pushStack) {
            push(value)
        } else {
            bind(value)
        }
    }

    override fun unbind(popStack: Boolean) {
        if (popStack) {
            pop()
        } else {
            rebind()
        }
    }

    fun push(value: V) {
        if (bound.isEmpty()) {
            restoreTo = query()
        }

        if (bound.lastOrNull() != value) {
            bind(value)
        }

        bound.add(value)
    }

    fun pop() {
        if (bound.isEmpty()) {
            throw IllegalStateException("Tried to pop GlStateStack $name that was already empty")
        }

        val removed = bound.removeLast()
        val current = getBound()

        if (current == null) {
            bind(restoreTo)
        } else if (current != removed) {
            bind(current)
        }
    }

    fun rebind() {
        bind(getBound())
    }

    fun getBound(): V? = bound.lastOrNull()

    fun ensureEmpty() {
        if (bound.isNotEmpty()) {
            throw IllegalStateException("Someone pushed and forgot to pop GlStateStack $name")
        }
    }
}