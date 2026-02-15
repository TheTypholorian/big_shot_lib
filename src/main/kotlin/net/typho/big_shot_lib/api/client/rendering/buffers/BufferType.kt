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
    ARRAY(GL_ARRAY_BUFFER),
    ELEMENT_ARRAY(GL_ELEMENT_ARRAY_BUFFER),
    PIXEL_PACK(GL_PIXEL_PACK_BUFFER),
    PIXEL_UNPACK(GL_PIXEL_UNPACK_BUFFER),
    TEXTURE(GL_TEXTURE_BUFFER),
    COPY_READ(GL_COPY_READ_BUFFER),
    COPY_WRITE(GL_COPY_WRITE_BUFFER),
    DRAW_INDIRECT(GL_DRAW_INDIRECT_BUFFER),
    DISPATCH_INDIRECT(GL_DISPATCH_INDIRECT_BUFFER),
    QUERY(GL_QUERY_BUFFER),
    TRANSFORM_FEEDBACK(GL_TRANSFORM_FEEDBACK_BUFFER, true),
    UNIFORM(GL_UNIFORM_BUFFER, true),
    ATOMIC_COUNTER(GL_ATOMIC_COUNTER_BUFFER, true),
    SHADER_STORAGE(GL_SHADER_STORAGE_BUFFER, true);

    override fun glId() = glId
}