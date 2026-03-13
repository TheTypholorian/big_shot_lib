package net.typho.big_shot_lib.api.client.opengl.state

import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL43.*
import org.lwjgl.opengl.GL44.*

enum class GlBufferType(
    @JvmField
    val state: GlStateType<Int>,
    @JvmField
    val bindingId: Int,
    @JvmField
    val isIndexed: Boolean = false
) {
    ARRAY_BUFFER(GlStateType(GL_ARRAY_BUFFER, 0, "ArrayBuffer"), GL_ARRAY_BUFFER_BINDING),
    ELEMENT_ARRAY_BUFFER(GlStateType(GL_ELEMENT_ARRAY_BUFFER, 0, "ElementArrayBuffer"), GL_ELEMENT_ARRAY_BUFFER_BINDING),
    PIXEL_PACK_BUFFER(GlStateType(GL_PIXEL_PACK_BUFFER, 0, "PixelPackBuffer"), GL_PIXEL_PACK_BUFFER_BINDING),
    PIXEL_UNPACK_BUFFER(GlStateType(GL_PIXEL_UNPACK_BUFFER, 0, "PixelUnpackBuffer"), GL_PIXEL_UNPACK_BUFFER_BINDING),
    TEXTURE_BUFFER(GlStateType(GL_TEXTURE_BUFFER, 0, "TextureBuffer"), GL_TEXTURE_BUFFER_BINDING),
    COPY_READ_BUFFER(GlStateType(GL_COPY_READ_BUFFER, 0, "CopyReadBuffer"), GL_COPY_READ_BUFFER_BINDING),
    COPY_WRITE_BUFFER(GlStateType(GL_COPY_WRITE_BUFFER, 0, "CopyWriteBuffer"), GL_COPY_WRITE_BUFFER_BINDING),
    DRAW_INDIRECT_BUFFER(GlStateType(GL_DRAW_INDIRECT_BUFFER, 0, "DrawIndirectBuffer"), GL_DRAW_INDIRECT_BUFFER_BINDING),
    DISPATCH_INDIRECT_BUFFER(GlStateType(GL_DISPATCH_INDIRECT_BUFFER, 0, "DispatchIndirectBuffer"), GL_DISPATCH_INDIRECT_BUFFER_BINDING),
    QUERY_BUFFER(GlStateType(GL_QUERY_BUFFER, 0, "QueryBuffer"), GL_QUERY_BUFFER_BINDING),

    TRANSFORM_FEEDBACK_BUFFER(GlStateType(GL_TRANSFORM_FEEDBACK_BUFFER, 0, "TransformFeedbackBuffer"), GL_TRANSFORM_FEEDBACK_BUFFER_BINDING, true),
    UNIFORM_BUFFER(GlStateType(GL_UNIFORM_BUFFER, 0, "UniformBuffer"), GL_UNIFORM_BUFFER_BINDING, true),
    ATOMIC_COUNTER_BUFFER(GlStateType(GL_ATOMIC_COUNTER_BUFFER, 0, "AtomicCounterBuffer"), GL_ATOMIC_COUNTER_BUFFER_BINDING, true),
    SHADER_STORAGE_BUFFER(GlStateType(GL_SHADER_STORAGE_BUFFER, 0, "ShaderStorageBuffer"), GL_SHADER_STORAGE_BUFFER_BINDING, true)
}