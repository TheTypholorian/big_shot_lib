package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import org.lwjgl.opengl.GL15.*

enum class GlBufferAccess(
    override val glId: Int
) : GlConstant {
    READ_ONLY(GL_READ_ONLY),
    WRITE_ONLY(GL_WRITE_ONLY),
    READ_WRITE(GL_READ_WRITE)
}