package net.typho.big_shot_lib.gl.state

import com.mojang.blaze3d.platform.GlStateManager

class ColorMask(
    var red: Boolean,
    var green: Boolean,
    var blue: Boolean,
    var alpha: Boolean,
) : GlState<ColorMask>, GlState.Value {
    companion object {
        val DEFAULT = ColorMask(true, true, true, true)
    }

    override fun default() = DEFAULT

    override fun queryValue(): ColorMask? = null

    override fun set(value: ColorMask) {
        GlStateManager._colorMask(value.red, value.green, value.blue, value.alpha)
    }

    override fun getType() = this
}
