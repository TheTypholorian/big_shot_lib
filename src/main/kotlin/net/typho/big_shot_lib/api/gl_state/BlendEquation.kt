package net.typho.big_shot_lib.api.gl_state

import net.typho.big_shot_lib.api.Bindable
import net.typho.big_shot_lib.api.GlNamed
import net.typho.big_shot_lib.api.NeoGlStateManager
import org.lwjgl.opengl.GL14.*

enum class BlendEquation(
    @JvmField
    val glId: Int
) : GlNamed, Bindable {
    ADD(GL_FUNC_ADD),
    SUBTRACT(GL_FUNC_SUBTRACT),
    REVERSE_SUBTRACT(GL_FUNC_REVERSE_SUBTRACT),
    MIN(GL_MIN),
    MAX(GL_MAX);

    override fun glId() = glId

    override fun bind() = NeoGlStateManager.blendEquation(this)

    override fun unbind() {
    }
}