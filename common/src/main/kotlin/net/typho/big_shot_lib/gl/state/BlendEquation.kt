package net.typho.big_shot_lib.gl.state

import com.mojang.blaze3d.platform.GlStateManager
import org.lwjgl.opengl.GL14.*
import org.lwjgl.opengl.GL20.GL_BLEND_EQUATION_RGB

enum class BlendEquation(val id: Int) : GlState<BlendEquation>, GlState.Value {
    ADD(GL_FUNC_ADD),
    SUBTRACT(GL_FUNC_SUBTRACT),
    REVERSE_SUBTRACT(GL_FUNC_REVERSE_SUBTRACT),
    MIN(GL_MIN),
    MAX(GL_MAX);

    companion object {
        @JvmField
        val DEFAULT = ADD
    }

    override fun default() = DEFAULT

    override fun queryValue(): BlendEquation? {
        val id = glGetInteger(GL_BLEND_EQUATION_RGB)
        return BlendEquation.entries.find { it.id == id }
    }

    override fun set(value: BlendEquation) {
        GlStateManager._blendEquation(value.id)
    }

    override fun getType() = this
}
