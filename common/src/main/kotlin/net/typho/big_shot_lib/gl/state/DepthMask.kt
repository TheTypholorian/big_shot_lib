package net.typho.big_shot_lib.gl.state

import com.mojang.blaze3d.platform.GlStateManager
import org.lwjgl.opengl.GL11.GL_DEPTH_WRITEMASK
import org.lwjgl.opengl.GL11.glGetBoolean

object DepthMask : GlState<Boolean> {
    override fun default() = true

    override fun queryValue(): Boolean? {
        return glGetBoolean(GL_DEPTH_WRITEMASK)
    }

    override fun set(value: Boolean) {
        GlStateManager._depthMask(value)
    }
}
