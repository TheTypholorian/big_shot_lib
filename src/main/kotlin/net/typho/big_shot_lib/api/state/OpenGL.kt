package net.typho.big_shot_lib.api.state

import net.typho.big_shot_lib.api.buffers.BufferType
import net.typho.big_shot_lib.api.buffers.BufferUsage
import net.typho.big_shot_lib.api.errors.ShaderCompileException
import net.typho.big_shot_lib.api.errors.ShaderLinkException
import net.typho.big_shot_lib.api.shaders.ShaderSourceType
import net.typho.big_shot_lib.api.textures.InterpolationType
import net.typho.big_shot_lib.api.textures.TextureFormat
import net.typho.big_shot_lib.api.textures.WrappingType
import net.typho.big_shot_lib.api.util.IColor
import net.typho.big_shot_lib.api.util.ResourceIdentifier
import org.joml.*
import java.nio.ByteBuffer
import java.util.*

interface OpenGL {
    companion object {
        @JvmField
        val INSTANCE: OpenGL = ServiceLoader.load(OpenGL::class.java).findFirst().orElseThrow()
    }

    /**
     * `glEnable(flag.glId)`
     */
    fun enable(flag: GlFlag)

    /**
     * `glDisable(flag.glId)`
     */
    fun disable(flag: GlFlag)

    /**
     * `glBlendColor(color.redF(), color.greenF(), color.blueF(), color.alphaF() ?: 1f)`
     */
    fun blendColor(color: IColor)

    /**
     * `glBlendEquation(eq.glId)`
     */
    fun blendEquation(eq: BlendEquation)

    /**
     * `glBlendFunc(src.glId, dst.glId)`
     */
    fun blendFunc(src: BlendFactor, dst: BlendFactor)

    /**
     * `glBlendFuncSeparate(src.glId, dst.glId, srcA.glId, dstA.glId)`
     */
    fun blendFuncSeparate(src: BlendFactor, dst: BlendFactor, srcA: BlendFactor, dstA: BlendFactor)

    /**
     * `glColorMask(mask.red, mask.green, mask.blue, mask.alpha)`
     */
    fun colorMask(mask: ColorMask)

    /**
     * `glCullFace(face.glId)`
     */
    fun cullFace(face: CullFace)

    /**
     * `glDepthMask(mask)`
     */
    fun depthMask(mask: Boolean)

    /**
     * `glDepthFunc(func.glId)`
     */
    fun depthFunc(func: ComparisonMode)

    /**
     * `glPolygonMode(mode.glId)`
     */
    fun polygonMode(mode: PolygonMode)

    /**
     * `glScissor(x, y, width, height)`
     */
    fun scissor(x: Int, y: Int, width: Int, height: Int)

    /**
     * `glStencilFunc(func.func.glId, func.ref, func.mask)`
     */
    fun stencilFunc(func: StencilFunc)

    /**
     * `glStencilMask(mask)`
     */
    fun stencilMask(mask: Int)

    /**
     * `glStencilOp(op.stencilFail.glId, op.depthFail.glId, op.depthPass.glId)`
     */
    fun stencilOp(op: StencilOp)

    /**
     * `glGenBuffers()`
     */
    fun createBuffer(): Int

    /**
     * `glBindBuffer(type.glId, glId)`
     */
    fun bindBuffer(type: BufferType, glId: Int)

    /**
     * `glBindBufferBase(type.glId, index, glId)`
     */
    fun bindBufferBase(type: BufferType, index: Int, glId: Int)

    /**
     * `glBufferData(type.glId, buffer, usage.glId)`
     */
    fun bufferData(type: BufferType, buffer: ByteBuffer, usage: BufferUsage)

    /**
     * `glBufferData(type.glId, size, usage.glId)`
     */
    fun bufferData(type: BufferType, size: Long, usage: BufferUsage)

    /**
     * `glDeleteBuffers(glId)`
     */
    fun deleteBuffer(glId: Int)

    /**
     * `glGenRenderbuffers()`
     */
    fun createRenderBuffer(): Int

    /**
     * `glBindRenderbuffer(GL_RENDERBUFFER, glId)`
     */
    fun bindRenderBuffer(glId: Int)

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
     * `glBindTexture(type, glId)`
     */
    fun bindTexture(type: Int, glId: Int)

    /**
     * `glActiveTexture(unit)`
     */
    fun activeTexture(unit: Int)

    /**
     * ```
     * glTexParameteri(type, GL_TEXTURE_MIN_FILTER, min.glId)
     * glTexParameteri(type, GL_TEXTURE_MAG_FILTER, mag.glId)
     * ```
     */
    fun textureInterpolation(type: Int, min: InterpolationType, mag: InterpolationType)

    /**
     * ```
     * glTexParameteri(type, GL_TEXTURE_WRAP_S, s.glId)
     * glTexParameteri(type, GL_TEXTURE_WRAP_T, t.glId)
     * ```
     */
    fun textureWrapping(type: Int, s: WrappingType, t: WrappingType)

    /**
     * ```
     * glTexImage2D(
     *     GL_TEXTURE_2D,
     *     0,
     *     format.internalId,
     *     width,
     *     height,
     *     0,
     *     format.glId,
     *     format.type,
     *     buffer
     * )
     * ```
     */
    fun textureData2D(format: TextureFormat, width: Int, height: Int, buffer: ByteBuffer)

    /**
     * ```
     * glTexImage2D(
     *     GL_TEXTURE_2D,
     *     0,
     *     format.internalId,
     *     width,
     *     height,
     *     0,
     *     format.glId,
     *     format.type,
     *     size
     * )
     * ```
     */
    fun textureData2D(format: TextureFormat, width: Int, height: Int, size: Long)

    /**
     * `glDeleteTextures(glId)`
     */
    fun deleteTexture(glId: Int)

    /**
     * `glGenFramebuffers()`
     */
    fun createFramebuffer(): Int

    /**
     * `glBindFramebuffer(glId)`
     */
    fun bindFramebuffer(glId: Int)

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
    fun attachFramebufferTexture(attachment: Int, type: Int, glId: Int)

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
     * `glCheckFramebufferStatus(GL_FRAMEBUFFER)`
     */
    fun checkFramebufferStatus(): Int

    /**
     * `glDeleteFramebuffers(glId)`
     */
    fun deleteFramebuffer(glId: Int)

    /**
     * `glGenVertexArrays()`
     */
    fun createVertexArray(): Int

    /**
     * `glBindVertexArray(glId)`
     */
    fun bindVertexArray(glId: Int)

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
     * `glUseProgram(glId)`
     */
    fun bindShaderProgram(glId: Int)

    /**
     * `glLinkProgram(glId)`
     * @throws ShaderLinkException
     */
    fun linkShaderProgram(glId: Int, name: ResourceIdentifier)

    /**
     * `glDeleteProgram(glId)`
     */
    fun deleteShaderProgram(glId: Int)

    /**
     * `glCreateShader(type.glId)`
     */
    fun createShaderSource(type: ShaderSourceType): Int

    /**
     * `glShaderSource(glId, code)`
     */
    fun shaderSourceCode(glId: Int, code: String)

    /**
     * `glCompileShader(glId)`
     * @throws ShaderCompileException
     */
    fun compileShaderSource(glId: Int, type: ShaderSourceType, name: ResourceIdentifier)

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

    /**
     * `glUniformMatrix2dv(location, transpose, mat.get(DoubleArray(4)))`
     */
    fun setUniformValue(location: Int, mat: Matrix2d, transpose: Boolean = false)

    /**
     * `glUniformMatrix3dv(location, transpose, mat.get(DoubleArray(9)))`
     */
    fun setUniformValue(location: Int, mat: Matrix3d, transpose: Boolean = false)

    /**
     * `glUniformMatrix4dv(location, transpose, mat.get(DoubleArray(16)))`
     */
    fun setUniformValue(location: Int, mat: Matrix4d, transpose: Boolean = false)

    /**
     * `glUniformMatrix3x2dv(location, transpose, mat.get(DoubleArray(6)))`
     */
    fun setUniformValue(location: Int, mat: Matrix3x2d, transpose: Boolean = false)

    /**
     * `glUniformMatrix4x3dv(location, transpose, mat.get(DoubleArray(12)))`
     */
    fun setUniformValue(location: Int, mat: Matrix4x3d, transpose: Boolean = false)

    /**
     * `glClearColor(color.redF(), color.greenF(), color.blueF(), color.alphaF() ?: 1f)`
     */
    fun clearColor(color: IColor)

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
}