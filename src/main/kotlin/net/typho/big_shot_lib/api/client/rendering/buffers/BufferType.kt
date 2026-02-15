package net.typho.big_shot_lib.api.client.rendering.buffers

import net.typho.big_shot_lib.api.client.rendering.util.GlNamed
import org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER
import org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER
import org.lwjgl.opengl.GL21.GL_PIXEL_PACK_BUFFER
import org.lwjgl.opengl.GL21.GL_PIXEL_UNPACK_BUFFER
import org.lwjgl.opengl.GL31.*
import org.lwjgl.opengl.GL40.GL_DRAW_INDIRECT_BUFFER
import org.lwjgl.opengl.GL43.*
import org.lwjgl.opengl.GL44.GL_QUERY_BUFFER

enum class BufferType(
    @JvmField
    val glId: Int,
    @JvmField
    val isIndexed: Boolean = false
) : GlNamed {
    ARRAY_BUFFER(GL_ARRAY_BUFFER),
    ELEMENT_ARRAY_BUFFER(GL_ELEMENT_ARRAY_BUFFER),
    PIXEL_PACK_BUFFER(GL_PIXEL_PACK_BUFFER),
    PIXEL_UNPACK_BUFFER(GL_PIXEL_UNPACK_BUFFER),
    TEXTURE_BUFFER(GL_TEXTURE_BUFFER),
    COPY_READ_BUFFER(GL_COPY_READ_BUFFER),
    COPY_WRITE_BUFFER(GL_COPY_WRITE_BUFFER),
    DRAW_INDIRECT_BUFFER(GL_DRAW_INDIRECT_BUFFER),
    DISPATCH_INDIRECT_BUFFER(GL_DISPATCH_INDIRECT_BUFFER),
    QUERY_BUFFER(GL_QUERY_BUFFER),
    TRANSFORM_FEEDBACK_BUFFER(GL_TRANSFORM_FEEDBACK_BUFFER, true),
    UNIFORM_BUFFER(GL_UNIFORM_BUFFER, true),
    ATOMIC_COUNTER_BUFFER(GL_ATOMIC_COUNTER_BUFFER, true),
    SHADER_STORAGE_BUFFER(GL_SHADER_STORAGE_BUFFER, true);

    override fun glId() = glId
}