package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import net.typho.big_shot_lib.api.util.NeoColor
import org.lwjgl.opengl.GL11.*
import kotlin.collections.iterator

sealed interface GlClearBit {
    fun mask(): Int

    fun run()

    companion object {
        @JvmStatic
        fun Iterator<GlClearBit>.initAndGetMask(): Int {
            var mask = 0

            for (bit in this) {
                bit.run()
                mask = mask or bit.mask()
            }

            return mask
        }

        @JvmStatic
        fun Iterable<GlClearBit>.initAndGetMask(): Int {
            return iterator().initAndGetMask()
        }

        @JvmStatic
        fun Array<out GlClearBit>.initAndGetMask(): Int {
            return iterator().initAndGetMask()
        }
    }

    data class Color(
        @JvmField
        val color: NeoColor
    ) : GlClearBit {
        override fun mask() = GL_COLOR_BUFFER_BIT

        override fun run() {
            glClearColor(color.redF, color.greenF, color.blueF, color.alphaF ?: 1f)
        }
    }

    data class Depth(
        @JvmField
        val depth: Float
    ) : GlClearBit {
        override fun mask() = GL_DEPTH_BUFFER_BIT

        override fun run() {
            glClearDepth(depth.toDouble())
        }
    }

    data class Stencil(
        @JvmField
        val stencil: Int
    ) : GlClearBit {
        override fun mask() = GL_STENCIL_BUFFER_BIT

        override fun run() {
            glClearStencil(stencil)
        }
    }
}