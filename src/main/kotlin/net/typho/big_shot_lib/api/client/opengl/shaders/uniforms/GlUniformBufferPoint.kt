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
        set(buffer.type, buffer.glId)
    }

    fun set(type: BufferType, glId: Int) {
        if (!type.isIndexed) {
            throw IllegalArgumentException("Buffer type must be indexed, $type is not")
        }

        OpenGL.INSTANCE.bindBufferBase(type, binding, glId)
    }

    fun setRange(type: BufferType, glId: Int, offset: Long, length: Long) {
        if (!type.isIndexed) {
            throw IllegalArgumentException("Buffer type must be indexed, $type is not")
        }

        OpenGL.INSTANCE.bindBufferRange(type, binding, glId, offset, length)
    }
}