package net.typho.big_shot_lib.api.client.opengl.shaders.uniforms

import net.typho.big_shot_lib.api.client.opengl.buffers.GlBuffer
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER

class GlUniformBufferPoint(
    @JvmField
    val name: String,
    @JvmField
    val binding: Int
) {
    fun set(buffer: GlBuffer) {
        OpenGL.INSTANCE.bindBufferBase(GL_UNIFORM_BUFFER, binding, buffer.glId)
    }

    fun setRange(range: GlBuffer.Range) {
        OpenGL.INSTANCE.bindBufferRange(GL_UNIFORM_BUFFER, binding, range.buffer.glId, range.offset, range.length)
    }
}