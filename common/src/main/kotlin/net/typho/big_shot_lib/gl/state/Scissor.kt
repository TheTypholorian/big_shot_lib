package net.typho.big_shot_lib.gl.state

import com.mojang.blaze3d.platform.GlStateManager
import org.lwjgl.opengl.GL11.GL_SCISSOR_BOX
import org.lwjgl.opengl.GL11.glGetIntegerv

class Scissor(
    var x: Int,
    var y: Int,
    var width: Int,
    var height: Int,
) : GlState<Scissor>, GlState.Value {
    companion object {
        val DEFAULT = Scissor(0, 0, 0, 0)
    }

    override fun default() = DEFAULT

    override fun queryValue(): Scissor? {
        val area = IntArray(4)
        glGetIntegerv(GL_SCISSOR_BOX, area)
        return Scissor(area[0], area[1], area[2], area[3])
    }

    override fun set(value: Scissor) {
        GlStateManager._scissorBox(value.x, value.y, value.width, value.height)
    }

    override fun getType() = this
}
