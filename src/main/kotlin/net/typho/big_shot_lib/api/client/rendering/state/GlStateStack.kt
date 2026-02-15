package net.typho.big_shot_lib.api.client.rendering.state

import net.typho.big_shot_lib.api.client.rendering.buffers.BufferType
import net.typho.big_shot_lib.api.client.rendering.textures.TextureType
import net.typho.big_shot_lib.api.client.rendering.util.GlNamed
import java.util.*
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Predicate
import kotlin.enums.enumEntries

open class GlStateStack(
    @JvmField
    val name: String,
    @JvmField
    val bind: Consumer<Int>
) {
    companion object {
        @JvmField
        val buffers = createMap<BufferType>(BufferType::name, OpenGL.INSTANCE::bindBuffer)
        @JvmField
        val renderBuffer = GlStateStack("RENDER_BUFFER", OpenGL.INSTANCE::bindRenderBuffer)
        @JvmField
        val textures = createMap<TextureType>(TextureType::name, OpenGL.INSTANCE::bindTexture)
        @JvmField
        val framebuffer = GlStateStack("FRAMEBUFFER", OpenGL.INSTANCE::bindFramebuffer)
        @JvmField
        val vertexArray = GlStateStack("VERTEX_ARRAY", OpenGL.INSTANCE::bindVertexArray)
        @JvmField
        val shader = GlStateStack("SHADER", OpenGL.INSTANCE::bindShaderProgram)

        @JvmStatic
        inline fun <reified T : Enum<T>> createMap(name: Function<T, String>, bind: BiConsumer<T, Int>): Map<T, GlStateStack> {
            return createMap(name, { true }, bind)
        }

        @JvmStatic
        inline fun <reified T : Enum<T>> createMap(name: Function<T, String>, predicate: Predicate<T>, bind: BiConsumer<T, Int>): Map<T, GlStateStack> {
            val map = HashMap<T, GlStateStack>()

            for (entry in enumEntries<T>()) {
                if (predicate.test(entry)) {
                    map[entry] = GlStateStack(name.apply(entry)) { bind.accept(entry, it) }
                }
            }

            return map
        }

        @JvmStatic
        fun ensureAllEmpty() {
            buffers.values.forEach { it.ensureEmpty() }
            renderBuffer.ensureEmpty()
            textures.values.forEach { it.ensureEmpty() }
            framebuffer.ensureEmpty()
            vertexArray.ensureEmpty()
            shader.ensureEmpty()
        }
    }

    @JvmField
    protected val bound = LinkedList<Int>()

    fun push(glId: Int) {
        if (glId == 0) {
            pop()
        } else {
            if (bound.lastOrNull() != glId) {
                bind.accept(glId)
            }

            bound.add(glId)
        }
    }

    fun push(named: GlNamed) = push(named.glId())

    fun pop() {
        if (bound.isEmpty()) {
            throw IllegalStateException("Tried to pop GlStateStack $name that was already empty")
        }

        val removed = bound.removeLast()
        val current = getBound()

        if (current == null) {
            bind.accept(0)
        } else if (current != removed) {
            bind.accept(current)
        }
    }

    fun rebind() {
        bind.accept(getBound() ?: 0)
    }

    fun getBound(): Int? = bound.lastOrNull()

    fun ensureEmpty() {
        if (bound.isNotEmpty()) {
            throw IllegalStateException("Someone pushed and forgot to pop GlStateStack $name")
        }
    }
}