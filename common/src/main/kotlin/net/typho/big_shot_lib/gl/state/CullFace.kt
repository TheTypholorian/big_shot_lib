package net.typho.big_shot_lib.gl.state

import org.lwjgl.opengl.GL11.*

enum class CullFace(val id: Int) : GlState<CullFace>, GlState.Value {
    FRONT(GL_FRONT),
    BACK(GL_BACK),
    FRONT_AND_BACK(GL_FRONT_AND_BACK);

    companion object {
        @JvmField
        val DEFAULT = BACK
    }

    override fun default() = DEFAULT

    override fun queryValue(): CullFace? {
        val face = glGetInteger(GL_CULL_FACE)
        return CullFace.entries.find { it.id == face }
    }

    override fun set(value: CullFace) {
        glCullFace(value.id)
    }

    override fun getType() = this
}
