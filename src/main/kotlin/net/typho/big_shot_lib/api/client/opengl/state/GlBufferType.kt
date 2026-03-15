package net.typho.big_shot_lib.api.client.opengl.state

import net.typho.big_shot_lib.api.client.opengl.util.GlNamed
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL43.*
import org.lwjgl.opengl.GL44.*

enum class GlBufferType(
    override val glId: Int,
    @JvmField
    val bindingId: Int,
    @JvmField
    val state: GlStateType<Int>,
    @JvmField
    val isIndexed: Boolean = false
) : GlNamed {
    ARRAY_BUFFER(GL_ARRAY_BUFFER, GL_ARRAY_BUFFER_BINDING, GlStateType(GlStateTracker::arrayBufferBinding)),
    ELEMENT_ARRAY_BUFFER(GL_ELEMENT_ARRAY_BUFFER, GL_ELEMENT_ARRAY_BUFFER_BINDING, GlStateType(GlStateTracker::elementArrayBufferBinding)),
    PIXEL_PACK_BUFFER(GL_PIXEL_PACK_BUFFER, GL_PIXEL_PACK_BUFFER_BINDING, GlStateType(GlStateTracker::pixelPackBufferBinding)),
    PIXEL_UNPACK_BUFFER(GL_PIXEL_UNPACK_BUFFER, GL_PIXEL_UNPACK_BUFFER_BINDING, GlStateType(GlStateTracker::pixelUnpackBufferBinding)),
    TEXTURE_BUFFER(GL_TEXTURE_BUFFER, GL_TEXTURE_BUFFER_BINDING, GlStateType(GlStateTracker::textureBufferBinding)),
    COPY_READ_BUFFER(GL_COPY_READ_BUFFER, GL_COPY_READ_BUFFER_BINDING, GlStateType(GlStateTracker::copyReadBufferBinding)),
    COPY_WRITE_BUFFER(GL_COPY_WRITE_BUFFER, GL_COPY_WRITE_BUFFER_BINDING, GlStateType(GlStateTracker::copyWriteBufferBinding)),
    DRAW_INDIRECT_BUFFER(GL_DRAW_INDIRECT_BUFFER, GL_DRAW_INDIRECT_BUFFER_BINDING, GlStateType(GlStateTracker::drawIndirectBufferBinding)),
    DISPATCH_INDIRECT_BUFFER(GL_DISPATCH_INDIRECT_BUFFER, GL_DISPATCH_INDIRECT_BUFFER_BINDING, GlStateType(
        GlStateTracker::dispatchIndirectBufferBinding
    )),
    QUERY_BUFFER(GL_QUERY_BUFFER, GL_QUERY_BUFFER_BINDING, GlStateType(GlStateTracker::queryBufferBinding)),

    TRANSFORM_FEEDBACK_BUFFER(GL_TRANSFORM_FEEDBACK_BUFFER, GL_TRANSFORM_FEEDBACK_BUFFER_BINDING, GlStateType(
        GlStateTracker::transformFeedbackBufferBinding
    ), true),
    UNIFORM_BUFFER(GL_UNIFORM_BUFFER, GL_UNIFORM_BUFFER_BINDING, GlStateType(GlStateTracker::uniformBufferBinding), true),
    ATOMIC_COUNTER_BUFFER(GL_ATOMIC_COUNTER_BUFFER, GL_ATOMIC_COUNTER_BUFFER_BINDING, GlStateType(GlStateTracker::atomicCounterBufferBinding), true),
    SHADER_STORAGE_BUFFER(GL_SHADER_STORAGE_BUFFER, GL_SHADER_STORAGE_BUFFER_BINDING, GlStateType(GlStateTracker::shaderStorageBufferBinding), true)
}