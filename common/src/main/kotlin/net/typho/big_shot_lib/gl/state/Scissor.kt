package net.typho.big_shot_lib.gl.state

import org.lwjgl.opengl.GL11.*

object Scissor : GlState<Scissor.Area> {
    override fun default() = Area.DEFAULT

    override fun queryValue(): Area? {
        val area = IntArray(4)
        glGetIntegerv(GL_SCISSOR_BOX, area)
        return Area(area[0], area[1], area[2], area[3])
    }

    override fun set(value: Area) {
        glScissor(value.x, value.y, value.width, value.height)
    }

    class Area(
        var x: Int,
        var y: Int,
        var width: Int,
        var height: Int,
    ) {
        companion object {
            val DEFAULT = Area(0, 0, 0, 0)
        }
    }
}
