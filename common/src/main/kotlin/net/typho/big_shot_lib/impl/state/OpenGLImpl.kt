package net.typho.big_shot_lib.impl.state

import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.Minecraft
import net.typho.big_shot_lib.api.BigShotApi
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
    fun debugPrint(name: String, vararg args: Any?) {
        BigShotApi.LOGGER.info(
            "$name(${
                args.joinToString(", ") {
                    when (it) {
                        null -> "null"
                        is Array<*> -> it.contentDeepToString()
                        is BooleanArray -> it.contentToString()
                        is ByteArray -> it.contentToString()
                        is CharArray -> it.contentToString()
                        is ShortArray -> it.contentToString()
                        is IntArray -> it.contentToString()
                        is LongArray -> it.contentToString()
                        is FloatArray -> it.contentToString()
                        is DoubleArray -> it.contentToString()
                        else -> it.toString()
                    }
                }
            })"
        )
    }

    override fun enable(flag: GlFlag) {
        debugPrint("glEnable", flag)
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
        debugPrint("glDisable", flag)
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
        debugPrint("glActiveTexture", unit)
        GlStateManager._activeTexture(GL_TEXTURE0 + unit)
    }

    override fun attachFramebufferRenderBuffer(attachment: Int, glId: Int) {
        debugPrint("glFramebufferRenderBuffer", attachment, glId)
        GlStateManager._glFramebufferRenderbuffer(
            GL_FRAMEBUFFER,
            attachment,
            GL_RENDERBUFFER,
            glId
        )
    }

    override fun drawBuffers(vararg buffers: Int) {
        debugPrint("glDrawBuffers", buffers)
        glDrawBuffers(buffers)
    }

    override fun attachFramebufferTexture2D(attachment: Int, type: Int, glId: Int) {
        debugPrint("glFramebufferTexture2D", attachment, type, glId)
        GlStateManager._glFramebufferTexture2D(
            GL_FRAMEBUFFER,
            attachment,
            type,
            glId,
            0
        )
    }

    override fun attachShaderSource(programId: Int, glId: Int) {
        debugPrint("glAttachShader", programId, glId)
        GlStateManager.glAttachShader(programId, glId)
    }

    override fun bindBuffer(type: BufferType, glId: Int) {
        debugPrint("glBindBuffer", type, glId)
        GlStateManager._glBindBuffer(type.glId, glId)
    }

    override fun bindBufferBase(
        type: BufferType,
        index: Int,
        glId: Int
    ) {
        debugPrint("glBindBufferBase", type, index, glId)
        glBindBufferBase(type.glId, index, glId)
    }

    override fun bindFramebuffer(glId: Int) {
        debugPrint("glBindFramebuffer", glId)
        GlStateManager._glBindFramebuffer(GL_FRAMEBUFFER, glId)
    }

    override fun attachFramebufferTexture(attachment: Int, glId: Int) {
        debugPrint("glFramebufferTexture", attachment, glId)
        glFramebufferTexture(
            GL_FRAMEBUFFER,
            attachment,
            glId,
            0
        )
    }

    override fun bindRenderBuffer(glId: Int) {
        debugPrint("glBindRenderBuffer", glId)
        GlStateManager._glBindRenderbuffer(GL_RENDERBUFFER, glId)
    }

    override fun bindShaderProgram(glId: Int) {
        debugPrint("glUseProgram", glId)
        GlStateManager._glUseProgram(glId)
    }

    override fun bindTexture(type: Int, glId: Int) {
        debugPrint("glBindTexture", type, glId)
        GlStateManager._bindTexture(glId)
    }

    override fun bindVertexArray(glId: Int) {
        debugPrint("glBindVertexArray", glId)
        GlStateManager._glBindVertexArray(glId)
    }

    override fun enableVertexAttribArray(index: Int) {
        debugPrint("glEnableVertexAttribArray", index)
        glEnableVertexAttribArray(index)
    }

    override fun disableVertexAttribArray(index: Int) {
        debugPrint("glDisableVertexAttribArray", index)
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
        debugPrint("glVertexAttribPointer", index, size, type, normalized, size, pointer)
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
        debugPrint("glVertexAttribPointer", index, size, type, normalized, size, pointer)
        glVertexAttribPointer(index, size, type, normalized, size, pointer)
    }

    override fun vertexAttribIPointer(
        index: Int,
        size: Int,
        type: Int,
        stride: Int,
        pointer: ByteBuffer
    ) {
        debugPrint("glVertexAttribIPointer", index, size, type, size, pointer)
        glVertexAttribIPointer(index, size, type, size, pointer)
    }

    override fun vertexAttribIPointer(
        index: Int,
        size: Int,
        type: Int,
        stride: Int,
        pointer: Long
    ) {
        debugPrint("glVertexAttribIPointer", index, size, type, size, pointer)
        glVertexAttribIPointer(index, size, type, size, pointer)
    }

    override fun blendColor(color: IColor) {
        debugPrint("glBlendColor", color)
        glBlendColor(color.redF(), color.greenF(), color.blueF(), color.alphaF() ?: 1f)
    }

    override fun blendEquation(eq: BlendEquation) {
        debugPrint("glBlendEquation", eq)
        GlStateManager._blendEquation(eq.glId)
    }

    override fun blendFunc(
        src: BlendFactor,
        dst: BlendFactor
    ) {
        debugPrint("glBlendFunc", src, dst)
        GlStateManager._blendFunc(src.glId, dst.glId)
    }

    override fun blendFuncSeparate(
        src: BlendFactor,
        dst: BlendFactor,
        srcA: BlendFactor,
        dstA: BlendFactor
    ) {
        debugPrint("glBlendFuncSeparate", src, dst, srcA, dstA)
        GlStateManager._blendFuncSeparate(src.glId, dst.glId, srcA.glId, dstA.glId)
    }

    override fun bufferData(
        type: BufferType,
        buffer: ByteBuffer,
        usage: BufferUsage
    ) {
        debugPrint("glBufferData", type, buffer, usage)
        GlStateManager._glBufferData(type.glId, buffer, usage.glId)
    }

    override fun bufferData(
        type: BufferType,
        size: Long,
        usage: BufferUsage
    ) {
        debugPrint("glBufferData", type, size, usage)
        GlStateManager._glBufferData(type.glId, size, usage.glId)
    }

    override fun checkFramebufferStatus(): Int {
        debugPrint("glCheckFramebufferStatus")
        return GlStateManager.glCheckFramebufferStatus(GL_FRAMEBUFFER)
    }

    override fun viewport(x: Int, y: Int, width: Int, height: Int) {
        debugPrint("glViewport", x, y, width, height)
        GlStateManager._viewport(x, y, width, height)
    }

    override fun clear(vararg mask: Int) {
        debugPrint("glClear", mask)
        GlStateManager._clear(mask.fold(0) { a, b -> a or b }, Minecraft.ON_OSX)
    }

    override fun clearColor(color: IColor) {
        debugPrint("glClearColor", color)
        GlStateManager._clearColor(color.redF(), color.greenF(), color.blueF(), color.alphaF() ?: 1f)
    }

    override fun clearDepth(depth: Float) {
        debugPrint("glClearColor", depth)
        GlStateManager._clearDepth(depth.toDouble())
    }

    override fun clearStencil(stencil: Int) {
        debugPrint("glClearColor", stencil)
        GlStateManager._clearStencil(stencil)
    }

    override fun colorMask(mask: ColorMask) {
        debugPrint("glColorMask", mask)
        GlStateManager._colorMask(mask.red, mask.green, mask.blue, mask.alpha)
    }

    override fun compileShaderSource(glId: Int, type: ShaderSourceType, name: ResourceIdentifier) {
        debugPrint("glCompileShader", glId, type, name)
        GlStateManager.glCompileShader(glId)

        val status = GlStateManager.glGetShaderi(glId, GL_COMPILE_STATUS)

        if (status == GL_FALSE) {
            throw ShaderCompileException("Error compiling ${type.name.lowercase()} shader $name:\n${glGetShaderInfoLog(glId).trim()}")
        }
    }

    override fun createBuffer(): Int {
        debugPrint("glGenBuffers")
        return GlStateManager._glGenBuffers()
    }

    override fun createFramebuffer(): Int {
        debugPrint("glGenFramebuffers")
        return GlStateManager.glGenFramebuffers()
    }

    override fun createRenderBuffer(): Int {
        debugPrint("glGenRenderBuffers")
        return GlStateManager.glGenRenderbuffers()
    }

    override fun createShaderProgram(): Int {
        debugPrint("glCreateProgram")
        return GlStateManager.glCreateProgram()
    }

    override fun createShaderSource(type: ShaderSourceType): Int {
        debugPrint("glCreateShader", type)
        return GlStateManager.glCreateShader(type.glId)
    }

    override fun createTexture(): Int {
        debugPrint("glGenTextures")
        return GlStateManager._genTexture()
    }

    override fun createVertexArray(): Int {
        debugPrint("glGenVertexArrays")
        return GlStateManager._glGenVertexArrays()
    }

    override fun cullFace(face: CullFace) {
        debugPrint("glCullFace", face)
        glCullFace(face.glId)
    }

    override fun deleteBuffer(glId: Int) {
        debugPrint("glDeleteBuffers", glId)
        GlStateManager._glDeleteBuffers(glId)
    }

    override fun deleteFramebuffer(glId: Int) {
        debugPrint("glDeleteFramebuffers", glId)
        GlStateManager._glDeleteFramebuffers(glId)
    }

    override fun deleteRenderBuffer(glId: Int) {
        debugPrint("glDeleteRenderBuffers", glId)
        GlStateManager._glDeleteRenderbuffers(glId)
    }

    override fun deleteShaderProgram(glId: Int) {
        debugPrint("glDeleteProgram", glId)
        GlStateManager.glDeleteProgram(glId)
    }

    override fun deleteShaderSource(glId: Int) {
        debugPrint("glDeleteShader", glId)
        GlStateManager.glDeleteShader(glId)
    }

    override fun deleteTexture(glId: Int) {
        debugPrint("glDeleteTexture", glId)
        GlStateManager._deleteTexture(glId)
    }

    override fun deleteVertexArray(glId: Int) {
        debugPrint("glDeleteVertexArrays", glId)
        GlStateManager._glDeleteVertexArrays(glId)
    }

    override fun depthFunc(func: ComparisonFunc) {
        debugPrint("glDepthFunc", func)
        GlStateManager._depthFunc(func.glId)
    }

    override fun depthMask(mask: Boolean) {
        debugPrint("glDepthMask", mask)
        GlStateManager._depthMask(mask)
    }

    override fun detachShaderSource(programId: Int, glId: Int) {
        debugPrint("glDetachShader", programId, glId)
        glDetachShader(programId, glId)
    }

    override fun getUniformLocation(programId: Int, name: CharSequence): Int {
        debugPrint("glGetUniformLocation", programId, name)
        return GlStateManager._glGetUniformLocation(programId, name)
    }

    override fun linkShaderProgram(glId: Int, name: ResourceIdentifier) {
        debugPrint("glLinkProgram", glId, name)
        GlStateManager.glLinkProgram(glId)

        val status = GlStateManager.glGetProgrami(glId, GL_LINK_STATUS)

        if (status == GL_FALSE) {
            throw ShaderLinkException("Error linking shader program $name:\n${glGetProgramInfoLog(glId).trim()}")
        }
    }

    override fun polygonMode(mode: PolygonMode) {
        debugPrint("glPolygonMode", GL_FRONT_AND_BACK, mode)
        GlStateManager._polygonMode(GL_FRONT_AND_BACK, mode.glId)
    }

    override fun resizeRenderBuffer(
        format: TextureFormat,
        width: Int,
        height: Int
    ) {
        debugPrint("glRenderBufferStorage", format, width, height)
        GlStateManager._glRenderbufferStorage(
            GL_RENDERBUFFER,
            format.internalId,
            width,
            height
        )
    }

    override fun scissor(x: Int, y: Int, width: Int, height: Int) {
        debugPrint("glScissor", x, y, width, height)
        GlStateManager._scissorBox(x, y, width, height)
    }

    // TODO debug prints for uniforms

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
        debugPrint("glShaderSource", glId, code)
        GlStateManager.glShaderSource(glId, listOf(code))
    }

    override fun stencilFunc(func: StencilFunc) {
        debugPrint("glStencilFunc", func)
        GlStateManager._stencilFunc(func.func.glId, func.ref, func.mask)
    }

    override fun stencilMask(mask: Int) {
        debugPrint("glStencilMask", mask)
        GlStateManager._stencilMask(mask)
    }

    override fun stencilOp(op: StencilOp) {
        debugPrint("glStencilOp", op)
        GlStateManager._stencilOp(op.stencilFail.glId, op.depthFail.glId, op.depthPass.glId)
    }

    override fun textureData1D(
        type: Int,
        format: TextureFormat,
        width: Int,
        buffer: ByteBuffer
    ) {
        debugPrint("glTexImage1D", type, format, width, buffer)
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
        debugPrint("glTexImage1D", type, format, width, size)
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
        debugPrint("glTexImage2D", type, format, width, height, buffer)
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
        debugPrint("glTexImage2D", type, format, width, height, size)
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
        debugPrint("glTexImage3D", type, format, width, height, depth, buffer)
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
        debugPrint("glTexImage3D", type, format, width, height, depth, size)
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
        debugPrint("glTextureInterpolation", min, mag)
        glTexParameteri(type, GL_TEXTURE_MIN_FILTER, min.glId)
        glTexParameteri(type, GL_TEXTURE_MAG_FILTER, mag.glId)
    }

    override fun textureWrapping(
        type: Int,
        s: WrappingType
    ) {
        debugPrint("glTextureWrapping", s)
        glTexParameteri(type, GL_TEXTURE_WRAP_S, s.glId)
    }

    override fun textureWrapping(
        type: Int,
        s: WrappingType,
        t: WrappingType
    ) {
        debugPrint("glTextureWrapping", s, t)
        glTexParameteri(type, GL_TEXTURE_WRAP_S, s.glId)
        glTexParameteri(type, GL_TEXTURE_WRAP_T, t.glId)
    }

    override fun textureWrapping(
        type: Int,
        s: WrappingType,
        t: WrappingType,
        r: WrappingType
    ) {
        debugPrint("glTextureWrapping", s, t, r)
        glTexParameteri(type, GL_TEXTURE_WRAP_S, s.glId)
        glTexParameteri(type, GL_TEXTURE_WRAP_T, t.glId)
        glTexParameteri(type, GL_TEXTURE_WRAP_R, r.glId)
    }

    override fun textureComparisonMode(
        type: Int,
        mode: TextureComparisonMode
    ) {
        debugPrint("glTextureComparisonMode", type, mode)
        glTexParameteri(type, GL_TEXTURE_COMPARE_MODE, mode.glId)
    }

    override fun textureComparisonFunc(
        type: Int,
        mode: ComparisonFunc
    ) {
        debugPrint("glTextureComparisonFunc", type, mode)
        glTexParameteri(type, GL_TEXTURE_COMPARE_FUNC, mode.glId)
    }
}