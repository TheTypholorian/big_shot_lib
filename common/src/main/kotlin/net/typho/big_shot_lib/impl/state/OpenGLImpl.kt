package net.typho.big_shot_lib.impl.state

import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.Minecraft
import net.typho.big_shot_lib.api.client.rendering.buffers.BufferType
import net.typho.big_shot_lib.api.client.rendering.buffers.BufferUsage
import net.typho.big_shot_lib.api.client.rendering.errors.ShaderCompileException
import net.typho.big_shot_lib.api.client.rendering.errors.ShaderLinkException
import net.typho.big_shot_lib.api.client.rendering.shaders.ShaderSourceType
import net.typho.big_shot_lib.api.client.rendering.shaders.variables.ShaderVariableType
import net.typho.big_shot_lib.api.client.rendering.state.*
import net.typho.big_shot_lib.api.client.rendering.textures.InterpolationType
import net.typho.big_shot_lib.api.client.rendering.textures.TextureComparisonMode
import net.typho.big_shot_lib.api.client.rendering.textures.TextureFormat
import net.typho.big_shot_lib.api.client.rendering.textures.WrappingType
import net.typho.big_shot_lib.api.util.IColor
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import org.joml.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL14.GL_TEXTURE0
import org.lwjgl.opengl.GL14.glBlendColor
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL40.*
import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer

class OpenGLImpl : OpenGL {
    override fun enable(flag: GlFlag) {
        when (flag) {
            GlFlag.SCISSOR_TEST -> GlStateManager._enableScissorTest()
            GlFlag.DEPTH_TEST -> GlStateManager._enableDepthTest()
            GlFlag.BLEND -> GlStateManager._enableBlend()
            GlFlag.CULL_FACE -> GlStateManager._enableCull()
            GlFlag.POLYGON_OFFSET_FILL -> GlStateManager._enablePolygonOffset()
            GlFlag.COLOR_LOGIC_OP -> GlStateManager._enableColorLogicOp()
            else -> glEnable(flag.glId)
        }
    }

    override fun disable(flag: GlFlag) {
        when (flag) {
            GlFlag.SCISSOR_TEST -> GlStateManager._disableScissorTest()
            GlFlag.DEPTH_TEST -> GlStateManager._disableDepthTest()
            GlFlag.BLEND -> GlStateManager._disableBlend()
            GlFlag.CULL_FACE -> GlStateManager._disableCull()
            GlFlag.POLYGON_OFFSET_FILL -> GlStateManager._disablePolygonOffset()
            GlFlag.COLOR_LOGIC_OP -> GlStateManager._disableColorLogicOp()
            else -> glDisable(flag.glId)
        }
    }

    override fun activeTexture(unit: Int) {
        GlStateManager._activeTexture(GL_TEXTURE0 + unit)
    }

    override fun attachFramebufferRenderBuffer(attachment: Int, glId: Int) {
        GlStateManager._glFramebufferRenderbuffer(
            GL_FRAMEBUFFER,
            attachment,
            GL_RENDERBUFFER,
            glId
        )
    }

    override fun drawBuffers(vararg buffers: Int) {
        glDrawBuffers(buffers)
    }

    override fun attachFramebufferTexture2D(attachment: Int, type: Int, glId: Int) {
        GlStateManager._glFramebufferTexture2D(
            GL_FRAMEBUFFER,
            attachment,
            type,
            glId,
            0
        )
    }

    override fun attachShaderSource(programId: Int, glId: Int) {
        GlStateManager.glAttachShader(programId, glId)
    }

    override fun bindBuffer(type: BufferType, glId: Int) {
        GlStateManager._glBindBuffer(type.glId, glId)
    }

    override fun bindBufferBase(
        type: BufferType,
        index: Int,
        glId: Int
    ) {
        glBindBufferBase(type.glId, index, glId)
    }

    override fun bindFramebuffer(glId: Int) {
        GlStateManager._glBindFramebuffer(GL_FRAMEBUFFER, glId)
    }

    override fun attachFramebufferTexture(attachment: Int, glId: Int) {
        glFramebufferTexture(
            GL_FRAMEBUFFER,
            attachment,
            glId,
            0
        )
    }

    override fun bindRenderBuffer(glId: Int) {
        GlStateManager._glBindRenderbuffer(GL_RENDERBUFFER, glId)
    }

    override fun bindShaderProgram(glId: Int) {
        GlStateManager._glUseProgram(glId)
    }

    override fun bindTexture(type: Int, glId: Int) {
        GlStateManager._bindTexture(glId)
    }

    override fun bindVertexArray(glId: Int) {
        GlStateManager._glBindVertexArray(glId)
    }

    override fun enableVertexAttribArray(index: Int) {
        glEnableVertexAttribArray(index)
    }

    override fun disableVertexAttribArray(index: Int) {
        glDisableVertexAttribArray(index)
    }

    override fun vertexAttribPointer(
        index: Int,
        size: Int,
        type: Int,
        normalized: Boolean,
        stride: Int,
        pointer: ByteBuffer
    ) {
        glVertexAttribPointer(index, size, type, normalized, size, pointer)
    }

    override fun vertexAttribPointer(
        index: Int,
        size: Int,
        type: Int,
        normalized: Boolean,
        stride: Int,
        pointer: Long
    ) {
        glVertexAttribPointer(index, size, type, normalized, size, pointer)
    }

    override fun vertexAttribIPointer(
        index: Int,
        size: Int,
        type: Int,
        stride: Int,
        pointer: ByteBuffer
    ) {
        glVertexAttribIPointer(index, size, type, size, pointer)
    }

    override fun vertexAttribIPointer(
        index: Int,
        size: Int,
        type: Int,
        stride: Int,
        pointer: Long
    ) {
        glVertexAttribIPointer(index, size, type, size, pointer)
    }

    override fun blendColor(color: IColor) {
        glBlendColor(color.redF(), color.greenF(), color.blueF(), color.alphaF() ?: 1f)
    }

    override fun blendEquation(eq: BlendEquation) {
        GlStateManager._blendEquation(eq.glId)
    }

    override fun blendFunc(
        src: BlendFactor,
        dst: BlendFactor
    ) {
        GlStateManager._blendFunc(src.glId, dst.glId)
    }

    override fun blendFuncSeparate(
        src: BlendFactor,
        dst: BlendFactor,
        srcA: BlendFactor,
        dstA: BlendFactor
    ) {
        GlStateManager._blendFuncSeparate(src.glId, dst.glId, srcA.glId, dstA.glId)
    }

    override fun bufferData(
        type: BufferType,
        buffer: ByteBuffer,
        usage: BufferUsage
    ) {
        GlStateManager._glBufferData(type.glId, buffer, usage.glId)
    }

    override fun bufferData(
        type: BufferType,
        size: Long,
        usage: BufferUsage
    ) {
        GlStateManager._glBufferData(type.glId, size, usage.glId)
    }

    override fun checkFramebufferStatus(): Int {
        return GlStateManager.glCheckFramebufferStatus(GL_FRAMEBUFFER)
    }

    override fun viewport(x: Int, y: Int, width: Int, height: Int) {
        GlStateManager._viewport(x, y, width, height)
    }

    override fun clear(vararg mask: Int) {
        GlStateManager._clear(mask.fold(0) { a, b -> a or b }, Minecraft.ON_OSX)
    }

    override fun clearColor(color: IColor) {
        GlStateManager._clearColor(color.redF(), color.greenF(), color.blueF(), color.alphaF() ?: 1f)
    }

    override fun clearDepth(depth: Float) {
        GlStateManager._clearDepth(depth.toDouble())
    }

    override fun clearStencil(stencil: Int) {
        GlStateManager._clearStencil(stencil)
    }

    override fun colorMask(mask: ColorMask) {
        GlStateManager._colorMask(mask.red, mask.green, mask.blue, mask.alpha)
    }

    override fun compileShaderSource(glId: Int, type: ShaderSourceType, name: ResourceIdentifier) {
        GlStateManager.glCompileShader(glId)

        val status = GlStateManager.glGetShaderi(glId, GL_COMPILE_STATUS)

        if (status == GL_FALSE) {
            throw ShaderCompileException("Error compiling ${type.name.lowercase()} shader $name:\n${glGetShaderInfoLog(glId).trim()}")
        }
    }

    override fun createBuffer(): Int {
        return GlStateManager._glGenBuffers()
    }

    override fun createFramebuffer(): Int {
        return GlStateManager.glGenFramebuffers()
    }

    override fun createRenderBuffer(): Int {
        return GlStateManager.glGenRenderbuffers()
    }

    override fun createShaderProgram(): Int {
        return GlStateManager.glCreateProgram()
    }

    override fun createShaderSource(type: ShaderSourceType): Int {
        return GlStateManager.glCreateShader(type.glId)
    }

    override fun createTexture(): Int {
        return GlStateManager._genTexture()
    }

    override fun createVertexArray(): Int {
        return GlStateManager._glGenVertexArrays()
    }

    override fun cullFace(face: CullFace) {
        glCullFace(face.glId)
    }

    override fun deleteBuffer(glId: Int) {
        GlStateManager._glDeleteBuffers(glId)
    }

    override fun deleteFramebuffer(glId: Int) {
        GlStateManager._glDeleteFramebuffers(glId)
    }

    override fun deleteRenderBuffer(glId: Int) {
        GlStateManager._glDeleteRenderbuffers(glId)
    }

    override fun deleteShaderProgram(glId: Int) {
        GlStateManager.glDeleteProgram(glId)
    }

    override fun deleteShaderSource(glId: Int) {
        GlStateManager.glDeleteShader(glId)
    }

    override fun deleteTexture(glId: Int) {
        GlStateManager._deleteTexture(glId)
    }

    override fun deleteVertexArray(glId: Int) {
        GlStateManager._glDeleteVertexArrays(glId)
    }

    override fun depthFunc(func: ComparisonFunc) {
        GlStateManager._depthFunc(func.glId)
    }

    override fun depthMask(mask: Boolean) {
        GlStateManager._depthMask(mask)
    }

    override fun detachShaderSource(programId: Int, glId: Int) {
        glDetachShader(programId, glId)
    }

    override fun getUniformLocation(programId: Int, name: CharSequence): Int {
        return GlStateManager._glGetUniformLocation(programId, name)
    }

    override fun linkShaderProgram(glId: Int, name: ResourceIdentifier) {
        GlStateManager.glLinkProgram(glId)

        val status = GlStateManager.glGetProgrami(glId, GL_LINK_STATUS)

        if (status == GL_FALSE) {
            throw ShaderLinkException("Error linking shader program $name:\n${glGetProgramInfoLog(glId).trim()}")
        }
    }

    override fun polygonMode(mode: PolygonMode) {
        GlStateManager._polygonMode(GL_FRONT_AND_BACK, mode.glId)
    }

    override fun resizeRenderBuffer(
        format: TextureFormat,
        width: Int,
        height: Int
    ) {
        GlStateManager._glRenderbufferStorage(
            GL_RENDERBUFFER,
            format.internalId,
            width,
            height
        )
    }

    override fun scissor(x: Int, y: Int, width: Int, height: Int) {
        GlStateManager._scissorBox(x, y, width, height)
    }

    override fun setUniformValue(location: Int, f1: Float) {
        glUniform1f(location, f1)
    }

    override fun setUniformValue(location: Int, f1: Float, f2: Float) {
        glUniform2f(location, f1, f2)
    }

    override fun setUniformValue(location: Int, f1: Float, f2: Float, f3: Float) {
        glUniform3f(location, f1, f2, f3)
    }

    override fun setUniformValue(
        location: Int,
        f1: Float,
        f2: Float,
        f3: Float,
        f4: Float
    ) {
        glUniform4f(location, f1, f2, f3, f4)
    }

    override fun setUniformValue(location: Int, i1: Int) {
        glUniform1i(location, i1)
    }

    override fun setUniformValue(location: Int, i1: Int, i2: Int) {
        glUniform2i(location, i1, i2)
    }

    override fun setUniformValue(location: Int, i1: Int, i2: Int, i3: Int) {
        glUniform3i(location, i1, i2, i3)
    }

    override fun setUniformValue(location: Int, i1: Int, i2: Int, i3: Int, i4: Int) {
        glUniform4i(location, i1, i2, i3, i4)
    }

    override fun setUniformValue(location: Int, b1: Boolean) {
        setUniformValue(location, if (b1) 1 else 0)
    }

    override fun setUniformValue(location: Int, b1: Boolean, b2: Boolean) {
        setUniformValue(location, if (b1) 1 else 0, if (b2) 1 else 0)
    }

    override fun setUniformValue(location: Int, b1: Boolean, b2: Boolean, b3: Boolean) {
        setUniformValue(location, if (b1) 1 else 0, if (b2) 1 else 0, if (b3) 1 else 0)
    }

    override fun setUniformValue(
        location: Int,
        b1: Boolean,
        b2: Boolean,
        b3: Boolean,
        b4: Boolean
    ) {
        setUniformValue(location, if (b1) 1 else 0, if (b2) 1 else 0, if (b3) 1 else 0, if (b4) 1 else 0)
    }

    override fun setUniformValue(location: Int, d1: Double) {
        glUniform1d(location, d1)
    }

    override fun setUniformValue(location: Int, d1: Double, d2: Double) {
        glUniform2d(location, d1, d2)
    }

    override fun setUniformValue(location: Int, d1: Double, d2: Double, d3: Double) {
        glUniform3d(location, d1, d2, d3)
    }

    override fun setUniformValue(
        location: Int,
        d1: Double,
        d2: Double,
        d3: Double,
        d4: Double
    ) {
        glUniform4d(location, d1, d2, d3, d4)
    }

    override fun setUniformValue(location: Int, mat: Matrix2f, transpose: Boolean) {
        glUniformMatrix2fv(location, transpose, mat.get(FloatArray(4)))
    }

    override fun setUniformValue(location: Int, mat: Matrix3f, transpose: Boolean) {
        glUniformMatrix3fv(location, transpose, mat.get(FloatArray(9)))
    }

    override fun setUniformValue(location: Int, mat: Matrix4f, transpose: Boolean) {
        glUniformMatrix4fv(location, transpose, mat.get(FloatArray(16)))
    }

    override fun setUniformValue(location: Int, mat: Matrix3x2f, transpose: Boolean) {
        glUniformMatrix3x2fv(location, transpose, mat.get(FloatArray(6)))
    }

    override fun setUniformValue(location: Int, mat: Matrix4x3f, transpose: Boolean) {
        glUniformMatrix4x3fv(location, transpose, mat.get(FloatArray(12)))
    }

    override fun getUniformInfo(
        programId: Int,
        index: Int
    ): Pair<String, ShaderVariableType> {
        MemoryStack.stackPush().use { stack ->
            val size = stack.mallocInt(1)
            val type = stack.mallocInt(1)
            val name = glGetActiveUniform(programId, index, size, type)
            return name to ShaderVariableType.entries.first { it.glId == type.get(0) }
        }
    }

    override fun shaderSourceCode(glId: Int, code: String) {
        GlStateManager.glShaderSource(glId, listOf(code))
    }

    override fun stencilFunc(func: StencilFunc) {
        GlStateManager._stencilFunc(func.func.glId, func.ref, func.mask)
    }

    override fun stencilMask(mask: Int) {
        GlStateManager._stencilMask(mask)
    }

    override fun stencilOp(op: StencilOp) {
        GlStateManager._stencilOp(op.stencilFail.glId, op.depthFail.glId, op.depthPass.glId)
    }

    override fun textureData1D(
        type: Int,
        format: TextureFormat,
        width: Int,
        buffer: ByteBuffer
    ) {
        glTexImage1D(
            type,
            0,
            format.internalId,
            width,
            0,
            format.glId,
            format.type,
            buffer
        )
    }

    override fun textureData1D(
        type: Int,
        format: TextureFormat,
        width: Int,
        size: Long
    ) {
        glTexImage1D(
            type,
            0,
            format.internalId,
            width,
            0,
            format.glId,
            format.type,
            size
        )
    }

    override fun textureData2D(
        type: Int,
        format: TextureFormat,
        width: Int,
        height: Int,
        buffer: ByteBuffer
    ) {
        glTexImage2D(
            type,
            0,
            format.internalId,
            width,
            height,
            0,
            format.glId,
            format.type,
            buffer
        )
    }

    override fun textureData2D(
        type: Int,
        format: TextureFormat,
        width: Int,
        height: Int,
        size: Long
    ) {
        glTexImage2D(
            type,
            0,
            format.internalId,
            width,
            height,
            0,
            format.glId,
            format.type,
            size
        )
    }

    override fun textureData3D(
        type: Int,
        format: TextureFormat,
        width: Int,
        height: Int,
        depth: Int,
        buffer: ByteBuffer
    ) {
        glTexImage3D(
            type,
            0,
            format.internalId,
            width,
            height,
            depth,
            0,
            format.glId,
            format.type,
            buffer
        )
    }

    override fun textureData3D(
        type: Int,
        format: TextureFormat,
        width: Int,
        height: Int,
        depth: Int,
        size: Long
    ) {
        glTexImage3D(
            type,
            0,
            format.internalId,
            width,
            height,
            depth,
            0,
            format.glId,
            format.type,
            size
        )
    }

    override fun textureInterpolation(
        type: Int,
        min: InterpolationType,
        mag: InterpolationType
    ) {
        glTexParameteri(type, GL_TEXTURE_MIN_FILTER, min.glId)
        glTexParameteri(type, GL_TEXTURE_MAG_FILTER, mag.glId)
    }

    override fun textureWrapping(
        type: Int,
        s: WrappingType
    ) {
        glTexParameteri(type, GL_TEXTURE_WRAP_S, s.glId)
    }

    override fun textureWrapping(
        type: Int,
        s: WrappingType,
        t: WrappingType
    ) {
        glTexParameteri(type, GL_TEXTURE_WRAP_S, s.glId)
        glTexParameteri(type, GL_TEXTURE_WRAP_T, t.glId)
    }

    override fun textureWrapping(
        type: Int,
        s: WrappingType,
        t: WrappingType,
        r: WrappingType
    ) {
        glTexParameteri(type, GL_TEXTURE_WRAP_S, s.glId)
        glTexParameteri(type, GL_TEXTURE_WRAP_T, t.glId)
        glTexParameteri(type, GL_TEXTURE_WRAP_R, r.glId)
    }

    override fun textureComparisonMode(
        type: Int,
        mode: TextureComparisonMode
    ) {
        glTexParameteri(type, GL_TEXTURE_COMPARE_MODE, mode.glId)
    }

    override fun textureComparisonFunc(
        type: Int,
        mode: ComparisonFunc
    ) {
        glTexParameteri(type, GL_TEXTURE_COMPARE_FUNC, mode.glId)
    }
}