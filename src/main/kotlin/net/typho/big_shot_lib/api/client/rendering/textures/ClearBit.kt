package net.typho.big_shot_lib.api.client.rendering.textures

import net.typho.big_shot_lib.api.client.rendering.state.OpenGL
import net.typho.big_shot_lib.api.util.IColor
import org.lwjgl.opengl.GL11.*

sealed interface ClearBit {
    fun mask(): Int

    fun run()

    companion object {
        @JvmStatic
        fun Iterator<ClearBit>.initAndGetClearMask(): Int {
            var mask = 0

            for (bit in this) {
                bit.run()
                mask = mask or bit.mask()
            }

            return mask
        }

        @JvmStatic
        fun Iterable<ClearBit>.initAndGetClearMask(): Int {
            return iterator().initAndGetClearMask()
        }

        @JvmStatic
        fun Array<out ClearBit>.initAndGetClearMask(): Int {
            return iterator().initAndGetClearMask()
        }
    }

    data class Color(
        @JvmField
        val color: IColor
    ) : ClearBit {
        override fun mask() = GL_COLOR_BUFFER_BIT

        override fun run() {
            OpenGL.INSTANCE.clearColor(color)
        }
    }

    data class Depth(
        @JvmField
        val depth: Float
    ) : ClearBit {
        override fun mask() = GL_DEPTH_BUFFER_BIT

        override fun run() {
            OpenGL.INSTANCE.clearDepth(depth)
        }
    }

    data class Stencil(
        @JvmField
        val stencil: Int
    ) : ClearBit {
        override fun mask() = GL_STENCIL_BUFFER_BIT

        override fun run() {
            OpenGL.INSTANCE.clearStencil(stencil)
        }
    }
}