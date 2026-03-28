package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import net.typho.big_shot_lib.api.util.NeoColor
import org.lwjgl.opengl.GL11.*
import kotlin.collections.iterator

sealed interface GlClearBit {
    val bufferBit: GlBufferBit

    fun run()

    companion object {
        @JvmStatic
        fun Iterator<GlClearBit>.initAndGetMask(): Int {
            var mask = 0

            for (bit in this) {
                bit.run()
                mask = mask or bit.bufferBit.glId
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
        override val bufferBit = GlBufferBit.COLOR

        override fun run() {
            glClearColor(color.redF, color.greenF, color.blueF, color.alphaF ?: 1f)
        }
    }

    data class Depth(
        @JvmField
        val depth: Float
    ) : GlClearBit {
        override val bufferBit = GlBufferBit.DEPTH

        override fun run() {
            glClearDepth(depth.toDouble())
        }
    }

    data class Stencil(
        @JvmField
        val stencil: Int
    ) : GlClearBit {
        override val bufferBit = GlBufferBit.STENCIL

        override fun run() {
            glClearStencil(stencil)
        }
    }
}