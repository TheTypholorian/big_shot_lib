package net.typho.big_shot_lib.api.client.opengl.state

import net.typho.big_shot_lib.api.client.opengl.buffers.BufferType
import net.typho.big_shot_lib.api.client.opengl.buffers.GlFramebuffer
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import net.typho.big_shot_lib.api.client.opengl.util.TextureType
import net.typho.big_shot_lib.api.util.NeoCollections
import net.typho.big_shot_lib.api.util.NeoColor
import java.awt.Rectangle
import java.io.PrintStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import kotlin.enums.enumEntries
import kotlin.io.path.outputStream

open class GlStateStack<V>(
    @JvmField
    val name: String,
    @JvmField
    val bind: (value: V?) -> Unit,
    @JvmField
    val query: () -> V
) {
    companion object {
        @JvmField
        val buffers = createMap<BufferType, Int>(
            BufferType::name,
            { type, value -> if (type != BufferType.ELEMENT_ARRAY_BUFFER || (value != null && value != 0)) OpenGL.INSTANCE.bindBuffer(type, value) },
            OpenGL.INSTANCE::getBoundBuffer
        )
        @JvmField
        val renderBuffer = GlStateStack(
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
        val framebuffer = GlStateStack(
            "FRAMEBUFFER",
            OpenGL.INSTANCE::bindFramebuffer,
            OpenGL.INSTANCE::getBoundFramebuffer
        )
        @JvmField
        val vertexArray = GlStateStack(
            "VERTEX_ARRAY",
            OpenGL.INSTANCE::bindVertexArray,
            OpenGL.INSTANCE::getBoundVertexArray
        )
        @JvmField
        val shader = GlStateStack(
            "SHADER",
            OpenGL.INSTANCE::bindShaderProgram,
            OpenGL.INSTANCE::getBoundShaderProgram
        )
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
            { OpenGL.INSTANCE.viewport(it ?: Rectangle(GlFramebuffer.MAIN.dimensions)) },
            OpenGL.INSTANCE::getViewport
        )
        val all: List<GlStateStack<*>> by lazy {
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
        @JvmField
        var debugOut: DebugOut? = null

        @JvmStatic
        inline fun <reified T : Enum<T>, V> createMap(
            name: (type: T) -> String,
            crossinline bind: (type: T, value: V?) -> Unit,
            crossinline query: (type: T) -> V
        ): Map<T, GlStateStack<V>> {
            return createMap(name, { true }, bind, query)
        }

        @JvmStatic
        inline fun <reified T : Enum<T>, V> createMap(
            name: (type: T) -> String,
            predicate: (type: T) -> Boolean,
            crossinline bind: (type: T, value: V?) -> Unit,
            crossinline query: (type: T) -> V
        ): Map<T, GlStateStack<V>> {
            val map = HashMap<T, GlStateStack<V>>()

            for (entry in enumEntries<T>()) {
                if (predicate(entry)) {
                    map[entry] = GlStateStack(
                        name(entry),
                        { bind(entry, it) },
                        { query(entry) },
                    )
                }
            }

            return map
        }

        @JvmStatic
        fun openDebug(path: Path = Paths.get("debug", "gl_state_stack_dump_${System.currentTimeMillis()}.txt")) {
            val dir = path.parent

            if (!Files.exists(dir)) {
                Files.createDirectories(dir)
            }

            val out = path.outputStream()
            debugOut = object : DebugOut {
                override val stream: PrintStream = PrintStream(out)

                override fun close() {
                    out.close()
                }
            }
        }

        @JvmStatic
        fun getStackTrace(): List<StackTraceElement> {
            return Thread.currentThread().stackTrace.dropWhile {
                it.className.startsWith(GlStateStack::class.java.packageName) ||
                        it.className.startsWith("java") ||
                        it.className.startsWith("org.lwjgl") ||
                        it.methodName == "bind" ||
                        it.methodName == "unbind"
            }
        }
    }

    @JvmField
    protected val bound = LinkedList<V>()
    @JvmField
    protected var restoreTo: V? = null
    @JvmField
    val listeners = LinkedList<Listener<V>>()

    fun push(value: V) {
        listeners.forEach { it.onPushed(this, value) }

        var message = ""

        if (bound.isEmpty()) {
            restoreTo = query()
            message += "Pushed $name while empty, selected $restoreTo to restore to\n"
        }

        if (bound.lastOrNull() != value) {
            bind(value)
            message += "Pushed $name and bound $value\n"
        } else {
            message += "Pushed $name and did not bind $value\n"
        }

        bound.add(value)
        debugOut?.stream?.println("$message\tat ${getStackTrace().firstOrNull()}")
    }

    fun pop() {
        if (bound.isEmpty()) {
            throw IllegalStateException("Tried to pop GlStateStack $name that was already empty")
        }

        listeners.forEach { it.onPopped(this) }

        val removed = bound.removeLast()
        val current = getBound()

        if (current == null) {
            debugOut?.stream?.println("Restored $name from $removed to $restoreTo\n\tat ${getStackTrace().firstOrNull()}")
            bind(restoreTo)
        } else if (current != removed) {
            debugOut?.stream?.println("Popped $name from $removed to $current\n\tat ${getStackTrace().firstOrNull()}")
            bind(current)
        } else {
            debugOut?.stream?.println("Popped $name and did not bind $current\n\tat ${getStackTrace().firstOrNull()}")
        }
    }

    fun rebind() {
        val value = getBound()
        listeners.forEach { it.onRebound(this, value) }
        bind(value)
        debugOut?.stream?.println("Rebound $name to $value\n\tat ${getStackTrace().firstOrNull()}")
    }

    fun getBound(): V? = bound.lastOrNull()

    fun ensureEmpty() {
        if (bound.isNotEmpty()) {
            throw IllegalStateException("Someone pushed and forgot to pop GlStateStack $name")
        }
    }

    interface Listener<V> {
        fun onPushed(stack: GlStateStack<V>, value: V)

        fun onPopped(stack: GlStateStack<V>)

        fun onRebound(stack: GlStateStack<V>, value: V?)
    }

    interface DebugOut : AutoCloseable {
        val stream: PrintStream
    }
}