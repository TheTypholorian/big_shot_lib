package net.typho.big_shot_lib.gl.state

import org.lwjgl.opengl.GL11.glColorMask

object ColorMask : GlState<ColorMask.Mask> {
    override fun default() = Mask.DEFAULT

    override fun queryValue(): Mask? = null

    override fun set(value: Mask) {
        glColorMask(value.red, value.green, value.blue, value.alpha)
    }

    class Mask(
        var red: Boolean,
        var green: Boolean,
        var blue: Boolean,
        var alpha: Boolean,
    ) {
        companion object {
            val DEFAULT = Mask(true, true, true, true)
        }
    }
}
