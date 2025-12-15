package net.typho.big_shot_lib.gl.state

import org.lwjgl.opengl.ARBImaging.GL_BLEND_COLOR
import org.lwjgl.opengl.GL11.glGetFloatv
import org.lwjgl.opengl.GL14.glBlendColor

object BlendColor : GlState<BlendColor.Color> {
    override fun default() = Color.DEFAULT

    override fun queryValue(): Color? {
        val color = FloatArray(4)
        glGetFloatv(GL_BLEND_COLOR, color)
        return Color(color[0], color[1], color[2], color[3])
    }

    override fun set(value: Color) {
        glBlendColor(value.red, value.green, value.blue, value.alpha)
    }

    class Color(
        var red: Float,
        var green: Float,
        var blue: Float,
        var alpha: Float,
    ) {
        companion object {
            val DEFAULT = Color(0f, 0f, 0f, 0f)
        }
    }
}
