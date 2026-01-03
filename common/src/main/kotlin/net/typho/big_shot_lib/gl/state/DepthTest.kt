package net.typho.big_shot_lib.gl.state

import com.mojang.blaze3d.platform.GlStateManager
import org.lwjgl.opengl.GL11.GL_DEPTH_FUNC
import org.lwjgl.opengl.GL11.glGetInteger

object DepthTest : GlState<ComparisonMode> {
    override fun default() = ComparisonMode.DEFAULT

    override fun queryValue(): ComparisonMode? {
        val depth = glGetInteger(GL_DEPTH_FUNC)
        return ComparisonMode.entries.find { it.id == depth }
    }

    override fun set(value: ComparisonMode) {
        GlStateManager._depthFunc(value.id)
    }
}
