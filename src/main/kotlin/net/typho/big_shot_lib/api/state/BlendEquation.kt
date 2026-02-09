package net.typho.big_shot_lib.api.state

import net.typho.big_shot_lib.api.util.Bindable
import net.typho.big_shot_lib.api.util.GlNamed
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

    override fun bind() = OpenGL.INSTANCE.blendEquation(this)

    override fun unbind() {
    }
}