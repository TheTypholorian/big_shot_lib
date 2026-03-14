package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.util.GlNamed
import org.lwjgl.opengl.GL15.*

enum class GlBufferUsage(
    override val glId: Int
): GlNamed {
    STREAM_DRAW(GL_STREAM_DRAW),
    STREAM_READ(GL_STREAM_READ),
    STREAM_COPY(GL_STREAM_COPY),

    DYNAMIC_DRAW(GL_DYNAMIC_DRAW),
    DYNAMIC_READ(GL_DYNAMIC_READ),
    DYNAMIC_COPY(GL_DYNAMIC_COPY),

    STATIC_DRAW(GL_STATIC_DRAW),
    STATIC_READ(GL_STATIC_READ),
    STATIC_COPY(GL_STATIC_COPY)
}