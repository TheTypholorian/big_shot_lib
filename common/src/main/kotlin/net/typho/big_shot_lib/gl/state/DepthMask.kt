package net.typho.big_shot_lib.gl.state

import com.mojang.blaze3d.platform.GlStateManager
import org.lwjgl.opengl.GL11.GL_DEPTH_WRITEMASK
import org.lwjgl.opengl.GL11.glGetInteger

object DepthMask : GlState<Int> {
    override fun default() = 0xFF

    override fun queryValue(): Int? {
        return glGetInteger(GL_DEPTH_WRITEMASK)
    }

    override fun set(value: Int) {
        GlStateManager._depthFunc(value)
    }
}
