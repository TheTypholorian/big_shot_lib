package net.typho.big_shot_lib.api.client.opengl.shaders.uniforms

import net.typho.big_shot_lib.api.client.opengl.buffers.BufferType
import net.typho.big_shot_lib.api.client.opengl.buffers.GlBuffer
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL

class GlUniformBufferPoint(
    @JvmField
    val name: String,
    @JvmField
    val binding: Int
) {
    fun set(buffer: GlBuffer) {
        OpenGL.INSTANCE.bindBufferBase(BufferType.UNIFORM_BUFFER, binding, buffer.glId)
    }

    fun setRange(range: GlBuffer.Range) {
        OpenGL.INSTANCE.bindBufferRange(BufferType.UNIFORM_BUFFER, binding, range.buffer.glId, range.offset, range.length)
    }
}