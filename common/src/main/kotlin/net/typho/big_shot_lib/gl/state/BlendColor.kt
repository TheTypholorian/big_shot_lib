package net.typho.big_shot_lib.gl.state

import org.lwjgl.opengl.ARBImaging.GL_BLEND_COLOR
import org.lwjgl.opengl.GL11.glGetFloatv
import org.lwjgl.opengl.GL14.glBlendColor

class BlendColor(
    var red: Float,
    var green: Float,
    var blue: Float,
    var alpha: Float,
) : GlState<BlendColor>, GlState.Value {
    companion object {
        @JvmField
        val DEFAULT = BlendColor(0f, 0f, 0f, 0f)
    }

    override fun default() = DEFAULT

    override fun queryValue(): BlendColor? {
        val color = FloatArray(4)
        glGetFloatv(GL_BLEND_COLOR, color)
        return BlendColor(color[0], color[1], color[2], color[3])
    }

    override fun set(value: BlendColor) {
        glBlendColor(value.red, value.green, value.blue, value.alpha)
    }

    override fun getType() = this
}
