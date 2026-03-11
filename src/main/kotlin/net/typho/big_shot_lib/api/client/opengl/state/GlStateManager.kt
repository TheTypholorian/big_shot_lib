package net.typho.big_shot_lib.api.client.opengl.state

import net.typho.big_shot_lib.api.client.opengl.buffers.BufferType
import net.typho.big_shot_lib.api.client.opengl.buffers.GlFramebuffer
import net.typho.big_shot_lib.api.client.opengl.util.GlBinder
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import net.typho.big_shot_lib.api.client.opengl.util.TextureType
import net.typho.big_shot_lib.api.math.rect.NeoRect2i
import net.typho.big_shot_lib.api.util.NeoCollections
import net.typho.big_shot_lib.api.util.NeoColor
import java.util.*
import kotlin.enums.enumEntries

open class GlStateManager<V>(
    @JvmField
    val name: String,
    @JvmField
    val bind: (value: V?) -> Unit,
    @JvmField
    val query: () -> V
) : GlBinder<V> {
    companion object {
        @JvmField
        val buffers = createMap<BufferType, Int>(
            BufferType::name,
            OpenGL.INSTANCE::bindBuffer,
            OpenGL.INSTANCE::getBoundBuffer
        )
        @JvmField
        val renderBuffer = GlStateManager(
            "RENDER_BUFFER",
            OpenGL.INSTANCE::bindRenderBuffer,
            OpenGL.INSTANCE::getBoundRenderBuffer
        )
        @JvmField
        val textures = createMap<TextureType, Int>(
            TextureType::name,
            OpenGL.INSTANCE::bindTexture,
            OpenGL.INSTANCE::getBoundTexture
        )
        @JvmField
        val framebuffer = GlStateManager(
            "FRAMEBUFFER",
            OpenGL.INSTANCE::bindFramebuffer,
            OpenGL.INSTANCE::getBoundFramebuffer
        )
        @JvmField
        val vertexArray = GlStateManager(
            "VERTEX_ARRAY",
            OpenGL.INSTANCE::bindVertexArray,
            OpenGL.INSTANCE::getBoundVertexArray
        )
        @JvmField
        val shader = GlStateManager(
            "SHADER",
            OpenGL.INSTANCE::bindShaderProgram,
            OpenGL.INSTANCE::getBoundShaderProgram
        )
        @JvmField
        val blendColor = GlStateManager(
            "BLEND_COLOR",
            { OpenGL.INSTANCE.blendColor(it ?: NeoColor.FULL_ON) },
            OpenGL.INSTANCE::getBlendColor
        )
        @JvmField
        val blendEquation = GlStateManager(
            "BLEND_EQUATION",
            { OpenGL.INSTANCE.blendEquation(it ?: BlendEquation.ADD) },
            OpenGL.INSTANCE::getBlendEquation
        )
        @JvmField
        val blendFunc = GlStateManager<BlendFunction>(
            "BLEND_FUNCTION",
            { (it ?: BlendFunction.DEFAULT).bind() },
            OpenGL.INSTANCE::getBlendFunctionSeparate
        )
        @JvmField
        val colorMask = GlStateManager(
            "COLOR_MASK",
            { OpenGL.INSTANCE.colorMask(it ?: ColorMask.DEFAULT) },
            OpenGL.INSTANCE::getColorMask
        )
        @JvmField
        val cullFace = GlStateManager(
            "CULL_FACE",
            { OpenGL.INSTANCE.cullFace(it ?: CullFace.BACK) },
            OpenGL.INSTANCE::getCullFace
        )
        @JvmField
        val depthMask = GlStateManager(
            "COLOR_MASK",
            { OpenGL.INSTANCE.depthMask(it ?: true) },
            OpenGL.INSTANCE::getDepthMask
        )
        @JvmField
        val depthFunc = GlStateManager(
            "DEPTH_FUNC",
            { OpenGL.INSTANCE.depthFunc(it ?: ComparisonFunc.LEQUAL) },
            OpenGL.INSTANCE::getDepthFunc
        )
        @JvmField
        val polygonMode = GlStateManager(
            "POLYGON_MODE",
            { OpenGL.INSTANCE.polygonMode(it ?: PolygonMode.FILL) },
            OpenGL.INSTANCE::getPolygonMode
        )
        @JvmField
        val polygonOffset = GlStateManager(
            "POLYGON_OFFSET",
            { OpenGL.INSTANCE.polygonOffset(it ?: PolygonOffset(0f, 0f)) },
            OpenGL.INSTANCE::getPolygonOffset
        )
        @JvmField
        val stencilFunc = GlStateManager(
            "STENCIL_FUNC",
            { OpenGL.INSTANCE.stencilFunc(it ?: StencilFunc(ComparisonFunc.ALWAYS, 0, 0)) },
            OpenGL.INSTANCE::getStencilFunc
        )
        @JvmField
        val stencilMask = GlStateManager(
            "STENCIL_MASK",
            { OpenGL.INSTANCE.stencilMask(it ?: 0xFFFFFFFF.toInt()) },
            OpenGL.INSTANCE::getStencilMask
        )
        @JvmField
        val stencilOp = GlStateManager(
            "STENCIL_MASK",
            { OpenGL.INSTANCE.stencilOp(it ?: StencilOp(IntAction.KEEP, IntAction.KEEP, IntAction.KEEP)) },
            OpenGL.INSTANCE::getStencilOp
        )
        @JvmField
        val viewport = GlStateManager(
            "VIEWPORT",
            { OpenGL.INSTANCE.viewport(it ?: NeoRect2i(0, 0, GlFramebuffer.MAIN.width, GlFramebuffer.MAIN.height)) },
            OpenGL.INSTANCE::getViewport
        )
        val all: List<GlStateManager<*>> by lazy {
            NeoCollections.flatListOf(
                buffers.values,
                renderBuffer,
                textures.values,
                framebuffer,
                vertexArray,
                shader,
                blendColor,
                colorMask,
                depthMask,
                depthFunc,
                polygonMode,
                stencilFunc,
                stencilMask,
                stencilOp,
                GlFlag.entries.map { it.stack }
            )
        }

        @JvmStatic
        inline fun <reified T : Enum<T>, V> createMap(
            name: (type: T) -> String,
            crossinline bind: (type: T, value: V?) -> Unit,
            crossinline query: (type: T) -> V
        ): Map<T, GlStateManager<V>> {
            return createMap(name, { true }, bind, query)
        }

        @JvmStatic
        inline fun <reified T : Enum<T>, V> createMap(
            name: (type: T) -> String,
            predicate: (type: T) -> Boolean,
            crossinline bind: (type: T, value: V?) -> Unit,
            crossinline query: (type: T) -> V
        ): Map<T, GlStateManager<V>> {
            val map = HashMap<T, GlStateManager<V>>()

            for (entry in enumEntries<T>()) {
                if (predicate(entry)) {
                    map[entry] = GlStateManager(
                        name(entry),
                        { bind(entry, it) },
                        { query(entry) },
                    )
                }
            }

            return map
        }
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