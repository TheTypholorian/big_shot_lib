package net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlBuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlProgram
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlUniform
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlTextureBinding

interface GlBoundProgram : GlBoundResource<GlProgram> {
    fun setUniform(name: String, value: GlUniform.() -> Unit)

    fun setUniformBuffer(name: String, glId: Int)

    fun setUniformBufferRange(name: String, glId: Int, offset: Long, length: Long)

    fun setUniformBuffer(name: String, buffer: GlBuffer) = setUniformBuffer(name, buffer.glId)

    fun setUniformBufferRange(name: String, buffer: GlBuffer, offset: Long, length: Long) = setUniformBufferRange(name, buffer.glId, offset, length)

    fun setShaderStorageBuffer(name: String, glId: Int)

    fun setShaderStorageBufferRange(name: String, glId: Int, offset: Long, length: Long)

    fun setShaderStorageBuffer(name: String, buffer: GlBuffer) = setShaderStorageBuffer(name, buffer.glId)

    fun setShaderStorageBufferRange(name: String, buffer: GlBuffer, offset: Long, length: Long) = setShaderStorageBufferRange(name, buffer.glId, offset, length)

    fun setTexture(unit: Int, binding: GlTextureBinding)

    fun setTextureArray(unit: Int, name: String, vararg bindings: GlTextureBinding)
}