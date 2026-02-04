package net.typho.big_shot_lib.api.render_queue.shards

import org.lwjgl.opengl.GL11.*

enum class CullFace(
    @JvmField
    val glId: Int
) {
    FRONT(GL_FRONT),
    BACK(GL_BACK),
    FRONT_AND_BACK(GL_FRONT_AND_BACK);

    companion object {
        @JvmField
        val DEFAULT = BACK
    }
}