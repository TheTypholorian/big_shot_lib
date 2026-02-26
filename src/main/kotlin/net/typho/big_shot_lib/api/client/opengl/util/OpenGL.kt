package net.typho.big_shot_lib.api.client.opengl.util

import net.typho.big_shot_lib.api.BigShotApi.loadService
import net.typho.big_shot_lib.api.client.opengl.buffers.BufferType
import net.typho.big_shot_lib.api.client.opengl.buffers.BufferUsage
import net.typho.big_shot_lib.api.client.opengl.buffers.GlTextureCube
import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderSourceType
import net.typho.big_shot_lib.api.client.opengl.shaders.variables.ShaderVariableType
import net.typho.big_shot_lib.api.client.opengl.state.*
import net.typho.big_shot_lib.api.util.IColor
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import org.joml.*
import java.nio.ByteBuffer

interface OpenGL {
    companion object {
        @JvmField
        val INSTANCE: OpenGL = OpenGL::class.loadService()
    }

    fun interface DebugListener {
        fun accept(method: String, vararg args: Any)
    }

    fun addDebugListener(listener: DebugListener)

    fun recordRenderCall(task: Runnable)

    /**
     * `glEnable(flag)`
     */
    fun enable(flag: GlFlag)

    /**
     * `glDisable(flag)`
     */
    fun disable(flag: GlFlag)

    /**
     * `glIsEnabled(flag)`
     */
    fun isEnabled(flag: GlFlag): Boolean

    /**
     * `glBlendColor(color.redF(), color.greenF(), color.blueF(), color.alphaF() ?: 1f)`
     */
    fun blendColor(color: IColor)

    /**
     * `glGetFloatv(GL_BLEND_COLOR, color)`
     */
    fun getBlendColor(): IColor

    /**
     * `glBlendEquation(eq)`
     */
    fun blendEquation(eq: BlendEquation)

    /**
     * `glGetInteger(GL_BLEND_EQUATION)`
     */
    fun getBlendEquation(): BlendEquation

    /**
     * `glBlendFunc(src, dst)`
     */
    fun blendFunc(src: BlendFactor, dst: BlendFactor)

    /**
     * ```
     * glGetInteger(GL_BLEND_SRC);
     * glGetInteger(GL_BLEND_DST);
     * ```
     */
    fun getBlendFunction(): BlendFunction.Basic

    /**
     * `glBlendFuncSeparate(src, dst, srcA, dstA)`
     */
    fun blendFuncSeparate(src: BlendFactor, dst: BlendFactor, srcA: BlendFactor, dstA: BlendFactor)

    /**
     * ```
     * glGetInteger(GL_BLEND_SRC_RGB);
     * glGetInteger(GL_BLEND_DST_RGB);
     * glGetInteger(GL_BLEND_SRC_ALPHA);
     * glGetInteger(GL_BLEND_DST_ALPHA);
     * ```
     */
    fun getBlendFunctionSeparate(): BlendFunction.Separate

    /**
     * `glColorMask(mask.red, mask.green, mask.blue, mask.alpha)`
     */
    fun colorMask(mask: ColorMask)

    /**
     * `glGetBooleanv(GL_COLOR_WRITEMASK, mask)`
     */
    fun getColorMask(): ColorMask

    /**
     * `glCullFace(face)`
     */
    fun cullFace(face: CullFace)

    /**
     * `glGetInteger(GL_CULL_FACE_MODE)`
     */
    fun getCullFace(): CullFace

    /**
     * `glDepthMask(mask)`
     */
    fun depthMask(mask: Boolean)

    /**
     * `glGetBoolean(GL_DEPTH_WRITEMASK)`
     */
    fun getDepthMask(): Boolean

    /**
     * `glDepthFunc(func)`
     */
    fun depthFunc(func: ComparisonFunc)

    /**
     * `glGetInteger(GL_DEPTH_FUNC)`
     */
    fun getDepthFunc(): ComparisonFunc

    /**
     * `glPolygonMode(mode)`
     */
    fun polygonMode(mode: PolygonMode)

    /**
     * `glGetIntegerv(GL_POLYGON_MODE, mode)`
     */
    fun getPolygonMode(): PolygonMode

    /**
     * `glScissor(x, y, width, height)`
     */
    fun scissor(x: Int, y: Int, width: Int, height: Int)

    /**
     * `glStencilFunc(func.func, func.ref, func.mask)`
     */
    fun stencilFunc(func: StencilFunc)

    /**
     * ```
     * glGetInteger(GL_STENCIL_FUNC);
     * glGetInteger(GL_STENCIL_REF);
     * glGetInteger(GL_STENCIL_VALUE_MASK);
     * ```
     */
    fun getStencilFunc(): StencilFunc

    /**
     * `glStencilMask(mask)`
     */
    fun stencilMask(mask: Int)

    /**
     * `glGetInteger(GL_STENCIL_WRITEMASK)`
     */
    fun getStencilMask(): Int

    /**
     * `glStencilOp(op.stencilFail, op.depthFail, op.depthPass)`
     */
    fun stencilOp(op: StencilOp)

    /**
     * ```
     * glGetInteger(GL_STENCIL_FAIL);
     * glGetInteger(GL_STENCIL_PASS_DEPTH_FAIL);
     * glGetInteger(GL_STENCIL_PASS_DEPTH_PASS);
     * ```
     */
    fun getStencilOp(): StencilOp

    /**
     * `glGenBuffers()`
     */
    fun createBuffer(): Int

    /**
     * `glBindBuffer(type, glId ?: 0)`
     */
    fun bindBuffer(type: BufferType, glId: Int?)

    /**
     * `glGetInteger(type.bindingId)`
     */
    fun getBoundBuffer(type: BufferType): Int

    /**
     * `glBindBufferBase(type, index, glId ?: 0)`
     */
    fun bindBufferBase(type: BufferType, index: Int, glId: Int?)

    /**
     * `glBindBufferRange(type, index, glId ?: 0, offset, length)`
     */
    fun bindBufferRange(type: BufferType, index: Int, glId: Int?, offset: Long, length: Long)

    /**
     * `glBufferData(type, buffer, usage)`
     */
    fun bufferData(type: BufferType, buffer: ByteBuffer, usage: BufferUsage)

    /**
     * `glBufferData(type, size, usage)`
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
     * `glBindRenderbuffer(GL_RENDERBUFFER, glId ?: 0)`
     */
    fun bindRenderBuffer(glId: Int?)

    /**
     * `glGetInteger(GL_RENDERBUFFER_BINDING)`
     */
    fun getBoundRenderBuffer(): Int

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
     * `glBindTexture(type, glId ?: 0)`
     */
    fun bindTexture(type: TextureType, glId: Int?)

    /**
     * `glBindSampler(unit, glId ?: 0)`
     */
    fun bindSampler(unit: Int, glId: Int?)

    /**
     * `glGetInteger(type.bindingId)`
     */
    fun getBoundTexture(type: TextureType): Int

    /**
     * `glActiveTexture(unit)`
     */
    fun activeTexture(unit: Int)

    /**
     * ```
     * glTexParameteri(type, GL_TEXTURE_MIN_FILTER, min)
     * glTexParameteri(type, GL_TEXTURE_MAG_FILTER, mag)
     * ```
     */
    fun textureInterpolation(type: TextureType, min: InterpolationType, mag: InterpolationType)

    /**
     * ```
     * glTexParameteri(type, GL_TEXTURE_WRAP_S, s)
     * ```
     */
    fun textureWrapping(type: TextureType, s: WrappingType)

    /**
     * ```
     * glTexParameteri(type, GL_TEXTURE_WRAP_S, s)
     * glTexParameteri(type, GL_TEXTURE_WRAP_T, t)
     * ```
     */
    fun textureWrapping(type: TextureType, s: WrappingType, t: WrappingType)

    /**
     * ```
     * glTexParameteri(type, GL_TEXTURE_WRAP_S, s)
     * glTexParameteri(type, GL_TEXTURE_WRAP_T, t)
     * glTexParameteri(type, GL_TEXTURE_WRAP_R, r)
     * ```
     */
    fun textureWrapping(type: TextureType, s: WrappingType, t: WrappingType, r: WrappingType)

    /**
     * ```
     * glTexParameteri(type, GL_TEXTURE_COMPARISON_MODE, mode)
     * ```
     */
    fun textureComparisonMode(type: TextureType, mode: TextureComparisonMode)

    /**
     * ```
     * glTexParameteri(type, GL_TEXTURE_COMPARISON_FUNC, mode)
     * ```
     */
    fun textureComparisonFunc(type: TextureType, mode: ComparisonFunc)

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
    fun textureData1D(type: TextureType, format: TextureFormat, width: Int, buffer: ByteBuffer)

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
    fun textureData1D(type: TextureType, format: TextureFormat, width: Int, size: Long)

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
    fun textureData2D(type: TextureType, format: TextureFormat, width: Int, height: Int, buffer: ByteBuffer)

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
    fun textureData2D(type: TextureType, format: TextureFormat, width: Int, height: Int, size: Long)

    /**
     * ```
     * glTexImage2D(
     *     face,
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
    fun textureData2D(face: GlTextureCube.Face, format: TextureFormat, width: Int, height: Int, buffer: ByteBuffer)

    /**
     * ```
     * glTexImage2D(
     *     face,
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
    fun textureData2D(face: GlTextureCube.Face, format: TextureFormat, width: Int, height: Int, size: Long)

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
    fun textureData3D(type: TextureType, format: TextureFormat, width: Int, height: Int, depth: Int, buffer: ByteBuffer)

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
    fun textureData3D(type: TextureType, format: TextureFormat, width: Int, height: Int, depth: Int, size: Long)

    /**
     * `glDeleteTextures(glId)`
     */
    fun deleteTexture(glId: Int)

    /**
     * `glGenFramebuffers()`
     */
    fun createFramebuffer(): Int

    /**
     * `glBindFramebuffer(glId ?: 0)`
     */
    fun bindFramebuffer(glId: Int?)

    /**
     * `glGetInteger(GL_DRAW_FRAMEBUFFER_BINDING)`
     */
    fun getBoundFramebuffer(): Int

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
    fun attachFramebufferTexture2D(attachment: Int, type: TextureType, glId: Int)

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
    fun checkFramebufferStatus(): Int

    /**
     * `glViewport(x, y, width, height)`
     */
    fun viewport(x: Int, y: Int, width: Int, height: Int)

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

    /**
     * `glDeleteFramebuffers(glId)`
     */
    fun deleteFramebuffer(glId: Int)

    /**
     * `glGenVertexArrays()`
     */
    fun createVertexArray(): Int

    /**
     * `glBindVertexArray(glId ?: 0)`
     */
    fun bindVertexArray(glId: Int?)

    /**
     * `glGetInteger(GL_VERTEX_ARRAY_BINDING)`
     */
    fun getBoundVertexArray(): Int

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
     * `glUseProgram(glId ?: 0)`
     */
    fun bindShaderProgram(glId: Int?)

    /**
     * `glGetInteger(GL_CURRENT_PROGRAM)`
     */
    fun getBoundShaderProgram(): Int

    /**
     * `glLinkProgram(glId)`
     * @throws net.typho.big_shot_lib.api.errors.ShaderLinkException
     */
    fun linkShaderProgram(glId: Int, name: ResourceIdentifier)

    /**
     * `glDeleteProgram(glId)`
     */
    fun deleteShaderProgram(glId: Int)

    /**
     * `glCreateShader(type)`
     */
    fun createShaderSource(type: ShaderSourceType): Int

    /**
     * `glShaderSource(glId, code)`
     */
    fun shaderSourceCode(glId: Int, code: String)

    /**
     * `glCompileShader(glId)`
     * @throws net.typho.big_shot_lib.api.errors.ShaderCompileException
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