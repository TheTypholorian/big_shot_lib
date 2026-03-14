package net.typho.big_shot_lib.api.client.opengl.util

import net.typho.big_shot_lib.api.BigShotApi.loadService
import net.typho.big_shot_lib.api.client.opengl.buffers.GlBufferUsage
import net.typho.big_shot_lib.api.client.opengl.shaders.variables.ShaderVariableType
import net.typho.big_shot_lib.api.client.opengl.state.GlStateTracker
import net.typho.big_shot_lib.api.util.NeoColor
import net.typho.big_shot_lib.api.util.resources.NeoIdentifier
import org.joml.*
import java.nio.ByteBuffer

interface OpenGL : GlStateTracker {
    companion object {
        @JvmField
        val INSTANCE: OpenGL = OpenGL::class.loadService()
    }

    fun recordRenderCall(task: Runnable)

    /**
     * `glObjectLabel(type, glId, label)
     */
    fun debugLabel(type: Int, glId: Int, label: String)

    /**
     * `glGenBuffers()`
     */
    fun createBuffer(): Int

    /**
     * `glBindBufferBase(type, index, glId ?: 0)`
     */
    fun bindBufferBase(type: Int, index: Int, glId: Int?)

    /**
     * `glBindBufferRange(type, index, glId ?: 0, offset, length)`
     */
    fun bindBufferRange(type: Int, index: Int, glId: Int?, offset: Long, length: Long)

    /**
     * `glBufferData(type, buffer, usage)`
     */
    fun bufferData(type: Int, buffer: ByteBuffer, usage: GlBufferUsage)

    /**
     * `glBufferData(type, size, usage)`
     */
    fun bufferData(type: Int, size: Long, usage: GlBufferUsage)

    /**
     * `glDeleteBuffers(glId)`
     */
    fun deleteBuffer(glId: Int)

    /**
     * `glGenRenderbuffers()`
     */
    fun createRenderBuffer(): Int

    /**
     * ```
     * glRenderbufferStorage(
     *     GL_RENDERBUFFER
     *     format.internalId,
     *     width,
     *     height
     * )
     * ```
     */
    fun resizeRenderBuffer(format: TextureFormat, width: Int, height: Int)

    /**
     * `glDeleteRenderbuffers(glId)`
     */
    fun deleteRenderBuffer(glId: Int)

    /**
     * `glGenTextures()`
     */
    fun createTexture(): Int

    /**
     * `glBindSampler(unit, glId ?: 0)`
     */
    fun bindSampler(unit: Int, glId: Int?)

    /**
     * `glActiveTexture(unit)`
     */
    fun activeTexture(unit: Int)

    /**
     * ```
     * glGetTexParameteri(target, pname)
     * ```
     */
    fun getTextureParameter(target: Int, pname: Int): Int

    /**
     * ```
     * glTexParameteri(target, pname, param)
     * ```
     */
    fun textureParameter(target: Int, pname: Int, param: Int)

    /**
     * ```
     * glTexImage1D(
     *     type,
     *     0,
     *     format.internalId,
     *     width,
     *     0,
     *     format,
     *     format.type,
     *     buffer
     * )
     * ```
     */
    fun textureData1D(type: Int, format: TextureFormat, width: Int, buffer: ByteBuffer)

    /**
     * ```
     * glTexImage1D(
     *     type,
     *     0,
     *     format.internalId,
     *     width,
     *     0,
     *     format,
     *     format.type,
     *     size
     * )
     * ```
     */
    fun textureData1D(type: Int, format: TextureFormat, width: Int, size: Long)

    /**
     * ```
     * glTexImage2D(
     *     type,
     *     0,
     *     format.internalId,
     *     width,
     *     height,
     *     0,
     *     format,
     *     format.type,
     *     buffer
     * )
     * ```
     */
    fun textureData2D(type: Int, format: TextureFormat, width: Int, height: Int, buffer: ByteBuffer)

    /**
     * ```
     * glTexImage2D(
     *     type,
     *     0,
     *     format.internalId,
     *     width,
     *     height,
     *     0,
     *     format,
     *     format.type,
     *     size
     * )
     * ```
     */
    fun textureData2D(type: Int, format: TextureFormat, width: Int, height: Int, size: Long)

    /**
     * ```
     * glTexImage2DMultisample(
     *     type,
     *     samples,
     *     format.internalId,
     *     width,
     *     height,
     *     true
     * )
     * ```
     */
    fun textureData2DMultisample(type: Int, samples: Int, format: TextureFormat, width: Int, height: Int)

    /**
     * ```
     * glTexImage3D(
     *     type,
     *     0,
     *     format.internalId,
     *     width,
     *     height,
     *     depth,
     *     0,
     *     format,
     *     format.type,
     *     buffer
     * )
     * ```
     */
    fun textureData3D(type: Int, format: TextureFormat, width: Int, height: Int, depth: Int, buffer: ByteBuffer)

    /**
     * ```
     * glTexImage3D(
     *     type,
     *     0,
     *     format.internalId,
     *     width,
     *     height,
     *     depth,
     *     0,
     *     format,
     *     format.type,
     *     size
     * )
     * ```
     */
    fun textureData3D(type: Int, format: TextureFormat, width: Int, height: Int, depth: Int, size: Long)

    /**
     * `glDeleteTextures(glId)`
     */
    fun deleteTexture(glId: Int)

    /**
     * `glGenFramebuffers()`
     */
    fun createFramebuffer(): Int

    /**
     * ```
     * glFramebufferTexture(
     *     GL_FRAMEBUFFER,
     *     attachment,
     *     glId,
     *     0
     * )
     * ```
     */
    fun attachFramebufferTexture(attachment: Int, glId: Int)

    /**
     * ```
     * glFramebufferTexture2D(
     *     GL_FRAMEBUFFER,
     *     attachment,
     *     type,
     *     glId,
     *     0
     * )
     * ```
     */
    fun attachFramebufferTexture2D(attachment: Int, type: Int, glId: Int)

    /**
     * ```
     * glFramebufferRenderbuffer(
     *     GL_FRAMEBUFFER,
     *     attachment,
     *     GL_RENDERBUFFER,
     *     glId
     * )
     * ```
     */
    fun attachFramebufferRenderBuffer(attachment: Int, glId: Int)

    /**
     * `glDrawBuffers(buffers)`
     */
    fun drawBuffers(vararg buffers: Int)

    /**
     * `glCheckFramebufferStatus(GL_FRAMEBUFFER)`
     */
    fun checkFramebufferStatus(): FramebufferStatus

    /**
     * `glClearColor(color.redF(), color.greenF(), color.blueF(), color.alphaF() ?: 1f)`
     */
    fun clearColor(color: NeoColor)

    /**
     * `glClearDepth(depth)`
     */
    fun clearDepth(depth: Float)

    /**
     * `glClearStencil(stencil)`
     */
    fun clearStencil(stencil: Int)

    /**
     * `glClear(mask)`
     */
    fun clear(vararg mask: Int)

    /**
     * `glDeleteFramebuffers(glId)`
     */
    fun deleteFramebuffer(glId: Int)

    /**
     * `glGenVertexArrays()`
     */
    fun createVertexArray(): Int

    /**
     * `glEnableVertexAttribArray(index)`
     */
    fun enableVertexAttribArray(index: Int)

    /**
     * `glDisableVertexAttribArray(index)`
     */
    fun disableVertexAttribArray(index: Int)

    /**
     * `glVertexAttribPointer(index, size, type, normalized, stride, pointer)`
     */
    fun vertexAttribPointer(index: Int, size: Int, type: Int, normalized: Boolean, stride: Int, pointer: ByteBuffer)

    /**
     * `glVertexAttribPointer(index, size, type, normalized, stride, pointer)`
     */
    fun vertexAttribPointer(index: Int, size: Int, type: Int, normalized: Boolean, stride: Int, pointer: Long)

    /**
     * `glVertexAttribIPointer(index, size, type, stride, pointer)`
     */
    fun vertexAttribIPointer(index: Int, size: Int, type: Int, stride: Int, pointer: ByteBuffer)

    /**
     * `glVertexAttribIPointer(index, size, type, stride, pointer)`
     */
    fun vertexAttribIPointer(index: Int, size: Int, type: Int, stride: Int, pointer: Long)

    /**
     * `glDeleteVertexArrays(glId)`
     */
    fun deleteVertexArray(glId: Int)

    /**
     * `glCreateProgram()`
     */
    fun createShaderProgram(): Int

    /**
     * `glLinkProgram(glId)`
     * @throws net.typho.big_shot_lib.api.errors.ShaderLinkException
     */
    fun linkShaderProgram(glId: Int, name: NeoIdentifier)

    /**
     * `glDeleteProgram(glId)`
     */
    fun deleteShaderProgram(glId: Int)

    /**
     * `glCreateShader(type)`
     */
    fun createShaderSource(type: Int): Int

    /**
     * `glShaderSource(glId, code)`
     */
    fun shaderSourceCode(glId: Int, code: String)

    /**
     * `glCompileShader(glId)`
     * @throws net.typho.big_shot_lib.api.errors.ShaderCompileException
     */
    fun compileShaderSource(glId: Int, type: Int, name: NeoIdentifier)

    /**
     * `glAttachShader(programId, glId)`
     */
    fun attachShaderSource(programId: Int, glId: Int)

    /**
     * `glDetachShader(programId, glId)`
     */
    fun detachShaderSource(programId: Int, glId: Int)

    /**
     * `glDeleteShader(glId)`
     */
    fun deleteShaderSource(glId: Int)

    /**
     * `glGetUniformLocation(programId, name)`
     */
    fun getUniformLocation(programId: Int, name: CharSequence): Int

    /**
     * `glUniform1f(location, f1)`
     */
    fun setUniformValue(location: Int, f1: Float)

    /**
     * `glUniform2f(location, f1, f2)`
     */
    fun setUniformValue(location: Int, f1: Float, f2: Float)

    /**
     * `glUniform3f(location, f1, f2, f3)`
     */
    fun setUniformValue(location: Int, f1: Float, f2: Float, f3: Float)

    /**
     * `glUniform4f(location, f1, f2, f3, f4)`
     */
    fun setUniformValue(location: Int, f1: Float, f2: Float, f3: Float, f4: Float)

    /**
     * `glUniform1i(location, i1)`
     */
    fun setUniformValue(location: Int, i1: Int)

    /**
     * `glUniform2i(location, i1, i2)`
     */
    fun setUniformValue(location: Int, i1: Int, i2: Int)

    /**
     * `glUniform3i(location, i1, i2, i3)`
     */
    fun setUniformValue(location: Int, i1: Int, i2: Int, i3: Int)

    /**
     * `glUniform4i(location, i1, i2, i3, i4)`
     */
    fun setUniformValue(location: Int, i1: Int, i2: Int, i3: Int, i4: Int)

    /**
     * `glUniform1i(location, if (b1) 1 else 0)`
     */
    fun setUniformValue(location: Int, b1: Boolean)

    /**
     * `glUniform2i(location, if (b1) 1 else 0, if (b2) 1 else 0)`
     */
    fun setUniformValue(location: Int, b1: Boolean, b2: Boolean)

    /**
     * `glUniform3i(location, if (b1) 1 else 0, if (b2) 1 else 0, if (b3) 1 else 0)`
     */
    fun setUniformValue(location: Int, b1: Boolean, b2: Boolean, b3: Boolean)

    /**
     * `glUniform4i(location, if (b1) 1 else 0, if (b2) 1 else 0, if (b3) 1 else 0), if (b4) 1 else 0))`
     */
    fun setUniformValue(location: Int, b1: Boolean, b2: Boolean, b3: Boolean, b4: Boolean)

    /**
     * `glUniform1d(location, d1)`
     */
    fun setUniformValue(location: Int, d1: Double)

    /**
     * `glUniform2d(location, d1, d2)`
     */
    fun setUniformValue(location: Int, d1: Double, d2: Double)

    /**
     * `glUniform3d(location, d1, d2, d3)`
     */
    fun setUniformValue(location: Int, d1: Double, d2: Double, d3: Double)

    /**
     * `glUniform4d(location, d1, d2, d3, d4)`
     */
    fun setUniformValue(location: Int, d1: Double, d2: Double, d3: Double, d4: Double)

    /**
     * `glUniformMatrix2fv(location, transpose, mat.get(FloatArray(4)))`
     */
    fun setUniformValue(location: Int, mat: Matrix2f, transpose: Boolean = false)

    /**
     * `glUniformMatrix3fv(location, transpose, mat.get(FloatArray(9)))`
     */
    fun setUniformValue(location: Int, mat: Matrix3f, transpose: Boolean = false)

    /**
     * `glUniformMatrix4fv(location, transpose, mat.get(FloatArray(16)))`
     */
    fun setUniformValue(location: Int, mat: Matrix4f, transpose: Boolean = false)

    /**
     * `glUniformMatrix3x2fv(location, transpose, mat.get(FloatArray(6)))`
     */
    fun setUniformValue(location: Int, mat: Matrix3x2f, transpose: Boolean = false)

    /**
     * `glUniformMatrix4x3fv(location, transpose, mat.get(FloatArray(12)))`
     */
    fun setUniformValue(location: Int, mat: Matrix4x3f, transpose: Boolean = false)

    fun getUniformInfo(programId: Int, index: Int): Pair<String, ShaderVariableType>
}