package net.typho.big_shot_lib.api.client.rendering.state

import net.typho.big_shot_lib.api.client.rendering.buffers.BufferType
import net.typho.big_shot_lib.api.client.rendering.textures.TextureType
import net.typho.big_shot_lib.api.client.rendering.util.GlNamed
import java.util.*
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Predicate
import kotlin.enums.enumEntries

open class GlStateStack(
    @JvmField
    val bind: Consumer<Int>
) {
    companion object {
        @JvmField
        val buffers = createMap<BufferType>(OpenGL.INSTANCE::bindBuffer)
        @JvmField
        val renderBuffer = GlStateStack(OpenGL.INSTANCE::bindRenderBuffer)
        @JvmField
        val textures = createMap<TextureType>(OpenGL.INSTANCE::bindTexture)
        @JvmField
        val framebuffer = GlStateStack(OpenGL.INSTANCE::bindFramebuffer)
        @JvmField
        val vertexArray = GlStateStack(OpenGL.INSTANCE::bindVertexArray)
        @JvmField
        val shader = GlStateStack(OpenGL.INSTANCE::bindShaderProgram)

        @JvmStatic
        inline fun <reified T : Enum<T>> createMap(bind: BiConsumer<T, Int>): Map<T, GlStateStack> {
            return createMap({ true }, bind)
        }

        @JvmStatic
        inline fun <reified T : Enum<T>> createMap(predicate: Predicate<T>, bind: BiConsumer<T, Int>): Map<T, GlStateStack> {
            val map = HashMap<T, GlStateStack>()

            for (entry in enumEntries<T>()) {
                if (predicate.test(entry)) {
                    map[entry] = GlStateStack { bind.accept(entry, it) }
                }
            }

            return map
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
        val removed = bound.pop()
        val current = bound.lastOrNull()

        if (current == null) {
            bind.accept(0)
        } else if (current != removed) {
            bind.accept(current)
        }
    }

    fun rebind() {
        bind.accept(bound.lastOrNull() ?: 0)
    }
}