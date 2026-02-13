package net.typho.big_shot_lib.api.client.rendering.state

import net.typho.big_shot_lib.api.client.rendering.util.GlBindable
import net.typho.big_shot_lib.api.client.rendering.util.GlNamed
import org.lwjgl.opengl.GL14.*

enum class BlendEquation(
    @JvmField
    val glId: Int
) : GlNamed, GlBindable {
    ADD(GL_FUNC_ADD),
    SUBTRACT(GL_FUNC_SUBTRACT),
    REVERSE_SUBTRACT(GL_FUNC_REVERSE_SUBTRACT),
    MIN(GL_MIN),
    MAX(GL_MAX);

    override fun glId() = glId

    override fun bind() = OpenGL.Companion.INSTANCE.blendEquation(this)

    override fun unbind() {
    }
}