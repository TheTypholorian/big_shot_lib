package net.typho.big_shot_lib.api.client.rendering.state

import net.typho.big_shot_lib.api.client.rendering.buffers.BufferType
import net.typho.big_shot_lib.api.client.rendering.textures.TextureType
import net.typho.big_shot_lib.api.util.IColor
import net.typho.big_shot_lib.api.util.NeoCollections
import java.io.PrintStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.function.*
import java.util.function.Function
import kotlin.enums.enumEntries
import kotlin.io.path.outputStream

open class GlStateStack<V>(
    @JvmField
    val name: String,
    @JvmField
    val bind: Consumer<V?>,
    @JvmField
    val isNull: Predicate<V>,
    @JvmField
    val query: Supplier<V>
) {
    companion object {
        @JvmField
        val buffers = createMap<BufferType, Int>(
            BufferType::name,
            { type, value -> if (type != BufferType.ELEMENT_ARRAY_BUFFER || (value != null && value != 0)) OpenGL.INSTANCE.bindBuffer(type, value) },
            { type, value -> value == 0 },
            OpenGL.INSTANCE::getBoundBuffer
        )
        @JvmField
        val renderBuffer = GlStateStack(
            "RENDER_BUFFER",
            OpenGL.INSTANCE::bindRenderBuffer,
            { value -> value == 0 },
            OpenGL.INSTANCE::getBoundRenderBuffer
        )
        @JvmField
        val textures = createMap<TextureType, Int>(
            TextureType::name,
            OpenGL.INSTANCE::bindTexture,
            { type, value -> value == 0 },
            OpenGL.INSTANCE::getBoundTexture
        )
        @JvmField
        val framebuffer = GlStateStack(
            "FRAMEBUFFER",
            OpenGL.INSTANCE::bindFramebuffer,
            { value -> value == 0 },
            OpenGL.INSTANCE::getBoundFramebuffer
        )
        @JvmField
        val vertexArray = GlStateStack(
            "VERTEX_ARRAY",
            OpenGL.INSTANCE::bindVertexArray,
            { value -> value == 0 },
            OpenGL.INSTANCE::getBoundVertexArray
        )
        @JvmField
        val shader = GlStateStack(
            "SHADER",
            OpenGL.INSTANCE::bindShaderProgram,
            { value -> value == 0 },
            OpenGL.INSTANCE::getBoundShaderProgram
        )
        @JvmField
        val blendColor = GlStateStack(
            "BLEND_COLOR",
            { OpenGL.INSTANCE.blendColor(it ?: IColor.FULL_ON) },
            { false },
            OpenGL.INSTANCE::getBlendColor
        )
        @JvmField
        val colorMask = GlStateStack(
            "COLOR_MASK",
            { OpenGL.INSTANCE.colorMask(it ?: ColorMask.DEFAULT) },
            { false },
            OpenGL.INSTANCE::getColorMask
        )
        @JvmField
        val depthMask = GlStateStack(
            "COLOR_MASK",
            { OpenGL.INSTANCE.depthMask(it ?: true) },
            { false },
            OpenGL.INSTANCE::getDepthMask
        )
        @JvmField
        val depthFunc = GlStateStack(
            "DEPTH_FUNC",
            { OpenGL.INSTANCE.depthFunc(it ?: ComparisonFunc.LEQUAL) },
            { false },
            OpenGL.INSTANCE::getDepthFunc
        )
        @JvmField
        val polygonMode = GlStateStack(
            "POLYGON_MODE",
            { OpenGL.INSTANCE.polygonMode(it ?: PolygonMode.FILL) },
            { false },
            OpenGL.INSTANCE::getPolygonMode
        )
        @JvmField
        val stencilFunc = GlStateStack(
            "STENCIL_FUNC",
            { OpenGL.INSTANCE.stencilFunc(it ?: StencilFunc(ComparisonFunc.ALWAYS, 0, 0)) },
            { false },
            OpenGL.INSTANCE::getStencilFunc
        )
        @JvmField
        val stencilMask = GlStateStack(
            "STENCIL_MASK",
            { OpenGL.INSTANCE.stencilMask(it ?: 0xFFFFFFFF.toInt()) },
            { false },
            OpenGL.INSTANCE::getStencilMask
        )
        @JvmField
        val stencilOp = GlStateStack(
            "STENCIL_MASK",
            { OpenGL.INSTANCE.stencilOp(it ?: StencilOp(IntAction.KEEP, IntAction.KEEP, IntAction.KEEP)) },
            { false },
            OpenGL.INSTANCE::getStencilOp
        )
        @JvmField
        val all: List<GlStateStack<*>> = NeoCollections.flatListOf(
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
            stencilOp
        )
        @JvmField
        var debugOut: DebugOut? = null

        @JvmStatic
        inline fun <reified T : Enum<T>, V> createMap(name: Function<T, String>, bind: BiConsumer<T, V?>, isNull: BiPredicate<T, V>, query: Function<T, V>): Map<T, GlStateStack<V>> {
            return createMap(name, { true }, bind, isNull, query)
        }

        @JvmStatic
        inline fun <reified T : Enum<T>, V> createMap(name: Function<T, String>, predicate: Predicate<T>, bind: BiConsumer<T, V?>, isNull: BiPredicate<T, V>, query: Function<T, V>): Map<T, GlStateStack<V>> {
            val map = HashMap<T, GlStateStack<V>>()

            for (entry in enumEntries<T>()) {
                if (predicate.test(entry)) {
                    map[entry] = GlStateStack(
                        name.apply(entry),
                        { bind.accept(entry, it) },
                        { isNull.test(entry, it) },
                        { query.apply(entry) }
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

        if (isNull.test(value)) {
            pop()
        } else {
            var message = ""

            if (bound.isEmpty()) {
                restoreTo = query.get()
                message += "Pushed $name while empty, selected $restoreTo to restore to\n"
            }

            if (bound.lastOrNull() != value) {
                bind.accept(value)
                message += "Pushed and bound $value\n"
            } else {
                message += "Pushed and did not bind $value\n"
            }

            bound.add(value)
            debugOut?.stream?.println("$message\tat ${getStackTrace().firstOrNull()}")
        }
    }

    fun pop() {
        if (bound.isEmpty()) {
            throw IllegalStateException("Tried to pop GlStateStack $name that was already empty")
        }

        listeners.forEach { it.onPopped(this) }

        val removed = bound.removeLast()
        val current = getBound()

        if (current == null) {
            bind.accept(restoreTo)
            debugOut?.stream?.println("Restored $name from $removed to $restoreTo\n\tat ${getStackTrace().firstOrNull()}")
        } else if (current != removed) {
            bind.accept(current)
            debugOut?.stream?.println("Popped $name from $removed to $current\n\tat ${getStackTrace().firstOrNull()}")
        }
    }

    fun rebind() {
        val value = getBound()
        listeners.forEach { it.onRebound(this, value) }
        bind.accept(value)
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