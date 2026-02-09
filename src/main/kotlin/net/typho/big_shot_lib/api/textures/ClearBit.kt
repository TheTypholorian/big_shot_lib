package net.typho.big_shot_lib.api.textures

import net.typho.big_shot_lib.api.state.OpenGL
import net.typho.big_shot_lib.api.util.IColor
import org.lwjgl.opengl.GL11.*

sealed interface ClearBit {
    fun mask(): Int

    fun run()

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