package net.typho.big_shot_lib.impl.state

import com.mojang.blaze3d.opengl.GlStateManager
import net.typho.big_shot_lib.BigShotLib
import net.typho.big_shot_lib.api.client.opengl.buffers.BufferType
import net.typho.big_shot_lib.api.client.opengl.buffers.BufferUsage
import net.typho.big_shot_lib.api.client.opengl.buffers.GlTextureCube
import net.typho.big_shot_lib.api.client.opengl.shaders.ShaderSourceType
import net.typho.big_shot_lib.api.client.opengl.shaders.variables.ShaderVariableType
import net.typho.big_shot_lib.api.client.opengl.state.*
import net.typho.big_shot_lib.api.client.opengl.util.*
import net.typho.big_shot_lib.api.errors.ShaderCompileException
import net.typho.big_shot_lib.api.errors.ShaderLinkException
import net.typho.big_shot_lib.api.util.IColor
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import org.joml.*
import org.lwjgl.opengl.ARBImaging.GL_BLEND_COLOR
import org.lwjgl.opengl.ARBImaging.GL_BLEND_EQUATION
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL14.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL40.*
import org.lwjgl.system.MemoryStack
import java.nio.ByteBuffer
import java.util.*

class OpenGLImpl : OpenGL {
    private val listeners = LinkedList<OpenGL.DebugListener>()

    fun debugPrint(method: String, vararg args: Any?) {
        listeners.forEach { it.accept(method, args) }
    }

    override fun addDebugListener(listener: OpenGL.DebugListener) {
        listeners.add(listener)
    }

    override fun recordRenderCall(task: Runnable) {
        BigShotLib.renderThreadQueue.add(task)
    }

    override fun enable(flag: GlFlag) {
        debugPrint("glEnable", flag)
        when (flag) {
            GlFlag.SCISSOR_TEST -> GlStateManager._enableScissorTest()
            GlFlag.DEPTH_TEST -> GlStateManager._enableDepthTest()
            GlFlag.BLEND -> GlStateManager._enableBlend()
            GlFlag.CULL_FACE -> GlStateManager._enableCull()
            GlFlag.POLYGON_OFFSET -> GlStateManager._enablePolygonOffset()
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
            GlFlag.POLYGON_OFFSET -> GlStateManager._disablePolygonOffset()
            GlFlag.COLOR_LOGIC_OP -> GlStateManager._disableColorLogicOp()
            else -> glDisable(flag.glId)
        }
    }

    override fun isEnabled(flag: GlFlag): Boolean {
        return glIsEnabled(flag.glId)
    }

    override fun activeTexture(unit: Int) {
        debugPrint("glActiveTexture", unit)
        GlStateManager._activeTexture(GL_TEXTURE0 + unit)
    }

    override fun attachFramebufferRenderBuffer(attachment: Int, glId: Int) {
        debugPrint("glFramebufferRenderBuffer", attachment, glId)
        glFramebufferRenderbuffer(
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

    override fun attachFramebufferTexture2D(attachment: Int, type: TextureType, glId: Int) {
        debugPrint("glFramebufferTexture2D", attachment, type, glId)
        GlStateManager._glFramebufferTexture2D(
            GL_FRAMEBUFFER,
            attachment,
            type.glId,
            glId,
            0
        )
    }

    override fun attachShaderSource(programId: Int, glId: Int) {
        debugPrint("glAttachShader", programId, glId)
        GlStateManager.glAttachShader(programId, glId)
    }

    override fun bindBuffer(type: BufferType, glId: Int?) {
        debugPrint("glBindBuffer", type, glId)
        GlStateManager._glBindBuffer(type.glId, glId ?: 0)
    }

    override fun getBoundBuffer(type: BufferType): Int {
        return glGetInteger(type.bindingId)
    }

    override fun bindBufferBase(
        type: BufferType,
        index: Int,
        glId: Int?
    ) {
        debugPrint("glBindBufferBase", type, index, glId)
        glBindBufferBase(type.glId, index, glId ?: 0)
    }

    override fun bindBufferRange(
        type: BufferType,
        index: Int,
        glId: Int?,
        offset: Long,
        length: Long
    ) {
        glBindBufferRange(type.glId, index, glId ?: 0, offset, length)
    }

    override fun bindFramebuffer(glId: Int?) {
        debugPrint("glBindFramebuffer", glId)
        GlStateManager._glBindFramebuffer(GL_FRAMEBUFFER, glId ?: 0)
    }

    override fun getBoundFramebuffer(): Int {
        return glGetInteger(GL_DRAW_FRAMEBUFFER_BINDING)
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

    override fun bindRenderBuffer(glId: Int?) {
        debugPrint("glBindRenderBuffer", glId)
        glBindRenderbuffer(GL_RENDERBUFFER, glId ?: 0)
    }

    override fun getBoundRenderBuffer(): Int {
        return glGetInteger(GL_RENDERBUFFER_BINDING)
    }

    override fun bindSampler(unit: Int, glId: Int?) {
        debugPrint("glBindSampler", unit, glId)
        glBindSampler(unit, glId ?: 0)
    }

    override fun bindShaderProgram(glId: Int?) {
        debugPrint("glUseProgram", glId)
        GlStateManager._glUseProgram(glId ?: 0)
    }

    override fun getBoundShaderProgram(): Int {
        return glGetInteger(GL_CURRENT_PROGRAM)
    }

    override fun bindTexture(type: TextureType, glId: Int?) {
        debugPrint("glBindTexture", type, glId)

        if (type == TextureType.TEXTURE_2D) {
            GlStateManager._bindTexture(glId ?: 0)
        } else {
            glBindTexture(type.glId, glId ?: 0)
        }
    }

    override fun getBoundTexture(type: TextureType): Int {
        return glGetInteger(type.bindingId)
    }

    override fun bindVertexArray(glId: Int?) {
        debugPrint("glBindVertexArray", glId)
        GlStateManager._glBindVertexArray(glId ?: 0)
    }

    override fun getBoundVertexArray(): Int {
        return glGetInteger(GL_VERTEX_ARRAY_BINDING)
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
        debugPrint("glVertexAttribPointer", index, size, type, normalized, stride, pointer)
        glVertexAttribPointer(index, size, type, normalized, stride, pointer)
    }

    override fun vertexAttribPointer(
        index: Int,
        size: Int,
        type: Int,
        normalized: Boolean,
        stride: Int,
        pointer: Long
    ) {
        debugPrint("glVertexAttribPointer", index, size, type, normalized, stride, pointer)
        glVertexAttribPointer(index, size, type, normalized, stride, pointer)
    }

    override fun vertexAttribIPointer(
        index: Int,
        size: Int,
        type: Int,
        stride: Int,
        pointer: ByteBuffer
    ) {
        debugPrint("glVertexAttribIPointer", index, size, type, stride, pointer)
        glVertexAttribIPointer(index, size, type, stride, pointer)
    }

    override fun vertexAttribIPointer(
        index: Int,
        size: Int,
        type: Int,
        stride: Int,
        pointer: Long
    ) {
        debugPrint("glVertexAttribIPointer", index, size, type, stride, pointer)
        glVertexAttribIPointer(index, size, type, stride, pointer)
    }

    override fun blendColor(color: IColor) {
        debugPrint("glBlendColor", color)
        glBlendColor(color.redF(), color.greenF(), color.blueF(), color.alphaF() ?: 1f)
    }

    override fun getBlendColor(): IColor {
        val color = FloatArray(4)
        glGetFloatv(GL_BLEND_COLOR, color)
        return IColor.RGBAF(color)
    }

    override fun blendEquation(eq: BlendEquation) {
        debugPrint("glBlendEquation", eq)
        glBlendEquation(eq.glId)
    }

    override fun getBlendEquation(): BlendEquation {
        val id = glGetInteger(GL_BLEND_EQUATION)
        return BlendEquation.entries.first { it.glId == id }
    }

    override fun blendFunc(
        src: BlendFactor,
        dst: BlendFactor
    ) {
        debugPrint("glBlendFunc", src, dst)
        GlStateManager._blendFuncSeparate(src.glId, dst.glId, src.glId, dst.glId)
    }

    override fun getBlendFunction(): BlendFunction.Basic {
        val src = glGetInteger(GL_BLEND_SRC)
        val dst = glGetInteger(GL_BLEND_DST)

        return BlendFunction.Basic(
            BlendFactor.entries.first { it.glId == src },
            BlendFactor.entries.first { it.glId == dst }
        )
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

    override fun getBlendFunctionSeparate(): BlendFunction.Separate {
        val src = glGetInteger(GL_BLEND_SRC_RGB)
        val dst = glGetInteger(GL_BLEND_DST_RGB)
        val srcA = glGetInteger(GL_BLEND_SRC_ALPHA)
        val dstA = glGetInteger(GL_BLEND_DST_ALPHA)

        return BlendFunction.Separate(
            BlendFactor.entries.first { it.glId == src },
            BlendFactor.entries.first { it.glId == dst },
            BlendFactor.entries.first { it.glId == srcA },
            BlendFactor.entries.first { it.glId == dstA }
        )
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

    override fun checkFramebufferStatus(): FramebufferStatus {
        debugPrint("glCheckFramebufferStatus")
        val glId = glCheckFramebufferStatus(GL_FRAMEBUFFER)
        return FramebufferStatus.entries.firstOrNull { it.glId == glId } ?: FramebufferStatus.UNKNOWN_ERROR
    }

    override fun viewport(x: Int, y: Int, width: Int, height: Int) {
        debugPrint("glViewport", x, y, width, height)
        GlStateManager._viewport(x, y, width, height)
    }

    override fun clear(vararg mask: Int) {
        debugPrint("glClear", mask)
        GlStateManager._clear(mask.fold(0) { a, b -> a or b })
    }

    override fun clearColor(color: IColor) {
        debugPrint("glClearColor", color)
        glClearColor(color.redF(), color.greenF(), color.blueF(), color.alphaF() ?: 1f)
    }

    override fun clearDepth(depth: Float) {
        debugPrint("glClearColor", depth)
        glClearDepth(depth.toDouble())
    }

    override fun clearStencil(stencil: Int) {
        debugPrint("glClearColor", stencil)
        glClearStencil(stencil)
    }

    override fun colorMask(mask: ColorMask) {
        debugPrint("glColorMask", mask)
        GlStateManager._colorMask(mask.red, mask.green, mask.blue, mask.alpha)
    }

    override fun getColorMask(): ColorMask {
        MemoryStack.stackPush().use { stack ->
            val mask = stack.malloc(4)
            glGetBooleanv(GL_COLOR_WRITEMASK, mask)
            return ColorMask(mask.get(0) == 1.toByte(), mask.get(1) == 1.toByte(), mask.get(2) == 1.toByte(), mask.get(3) == 1.toByte())
        }
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
        return glGenRenderbuffers()
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

    override fun getCullFace(): CullFace {
        val id = glGetInteger(GL_CULL_FACE_MODE)
        return CullFace.entries.first { it.glId == id }
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
        glDeleteRenderbuffers(glId)
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
        glDeleteVertexArrays(glId)
    }

    override fun depthFunc(func: ComparisonFunc) {
        debugPrint("glDepthFunc", func)
        GlStateManager._depthFunc(func.glId)
    }

    override fun getDepthFunc(): ComparisonFunc {
        val id = glGetInteger(GL_DEPTH_FUNC)
        return ComparisonFunc.entries.first { it.glId == id }
    }

    override fun depthMask(mask: Boolean) {
        debugPrint("glDepthMask", mask)
        GlStateManager._depthMask(mask)
    }

    override fun getDepthMask(): Boolean {
        return glGetBoolean(GL_DEPTH_WRITEMASK)
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

    override fun getPolygonMode(): PolygonMode {
        val mode = IntArray(2)
        glGetIntegerv(GL_POLYGON_MODE, mode)
        return PolygonMode.entries.first { it.glId == mode[0] }
    }

    override fun polygonOffset(offset: PolygonOffset) {
        GlStateManager._polygonOffset(offset.factor, offset.units)
    }

    override fun getPolygonOffset(): PolygonOffset {
        return PolygonOffset(
            glGetFloat(GL_POLYGON_OFFSET_FACTOR),
            glGetFloat(GL_POLYGON_OFFSET_UNITS)
        )
    }

    override fun resizeRenderBuffer(
        format: TextureFormat,
        width: Int,
        height: Int
    ) {
        debugPrint("glRenderBufferStorage", format, width, height)
        glRenderbufferStorage(
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
        GlStateManager.glShaderSource(glId, code)
    }

    override fun stencilFunc(func: StencilFunc) {
        debugPrint("glStencilFunc", func)
        glStencilFunc(func.func.glId, func.ref, func.mask)
    }

    override fun getStencilFunc(): StencilFunc {
        val func = glGetInteger(GL_STENCIL_FUNC)
        val ref = glGetInteger(GL_STENCIL_REF)
        val mask = glGetInteger(GL_STENCIL_VALUE_MASK)
        return StencilFunc(
            ComparisonFunc.entries.first { it.glId == func },
            ref,
            mask
        )
    }

    override fun stencilMask(mask: Int) {
        debugPrint("glStencilMask", mask)
        glStencilMask(mask)
    }

    override fun getStencilMask(): Int {
        return glGetInteger(GL_STENCIL_WRITEMASK)
    }

    override fun stencilOp(op: StencilOp) {
        debugPrint("glStencilOp", op)
        glStencilOp(op.stencilFail.glId, op.depthFail.glId, op.depthPass.glId)
    }

    override fun getStencilOp(): StencilOp {
        val stencilFail = glGetInteger(GL_STENCIL_FAIL)
        val depthFail = glGetInteger(GL_STENCIL_PASS_DEPTH_FAIL)
        val depthPass = glGetInteger(GL_STENCIL_PASS_DEPTH_PASS)
        return StencilOp(
            IntAction.entries.first { it.glId == stencilFail },
            IntAction.entries.first { it.glId == depthFail },
            IntAction.entries.first { it.glId == depthPass }
        )
    }

    override fun textureData1D(
        type: TextureType,
        format: TextureFormat,
        width: Int,
        buffer: ByteBuffer
    ) {
        debugPrint("glTexImage1D", type, format, width, buffer)
        glTexImage1D(
            type.glId,
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
        type: TextureType,
        format: TextureFormat,
        width: Int,
        size: Long
    ) {
        debugPrint("glTexImage1D", type, format, width, size)
        glTexImage1D(
            type.glId,
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
        type: TextureType,
        format: TextureFormat,
        width: Int,
        height: Int,
        buffer: ByteBuffer
    ) {
        debugPrint("glTexImage2D", type, format, width, height, buffer)
        glTexImage2D(
            type.glId,
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
        type: TextureType,
        format: TextureFormat,
        width: Int,
        height: Int,
        size: Long
    ) {
        debugPrint("glTexImage2D", type, format, width, height, size)
        glTexImage2D(
            type.glId,
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

    override fun textureData2DMultisample(
        type: TextureType,
        samples: Int,
        format: TextureFormat,
        width: Int,
        height: Int
    ) {
        debugPrint("glTexImage2DMultisample", type, samples, format, width, height)
        glTexImage2DMultisample(
            type.glId,
            samples,
            format.internalId,
            width,
            height,
            true
        )
    }

    override fun textureData2D(
        face: GlTextureCube.Face,
        format: TextureFormat,
        width: Int,
        height: Int,
        buffer: ByteBuffer
    ) {
        debugPrint("glTexImage2D", face, format, width, height, buffer)
        glTexImage2D(
            face.glId,
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
        face: GlTextureCube.Face,
        format: TextureFormat,
        width: Int,
        height: Int,
        size: Long
    ) {
        debugPrint("glTexImage2D", face, format, width, height, size)
        glTexImage2D(
            face.glId,
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
        type: TextureType,
        format: TextureFormat,
        width: Int,
        height: Int,
        depth: Int,
        buffer: ByteBuffer
    ) {
        debugPrint("glTexImage3D", type, format, width, height, depth, buffer)
        glTexImage3D(
            type.glId,
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
        type: TextureType,
        format: TextureFormat,
        width: Int,
        height: Int,
        depth: Int,
        size: Long
    ) {
        debugPrint("glTexImage3D", type, format, width, height, depth, size)
        glTexImage3D(
            type.glId,
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
        type: TextureType,
        min: InterpolationType,
        mag: InterpolationType
    ) {
        debugPrint("glTextureInterpolation", type, min, mag)
        glTexParameteri(type.glId, GL_TEXTURE_MIN_FILTER, min.glId)
        glTexParameteri(type.glId, GL_TEXTURE_MAG_FILTER, mag.glId)
    }

    override fun textureWrapping(
        type: TextureType,
        s: WrappingType
    ) {
        debugPrint("glTextureWrapping", type, s)
        glTexParameteri(type.glId, GL_TEXTURE_WRAP_S, s.glId)
    }

    override fun textureWrapping(
        type: TextureType,
        s: WrappingType,
        t: WrappingType
    ) {
        debugPrint("glTextureWrapping", type, s, t)
        glTexParameteri(type.glId, GL_TEXTURE_WRAP_S, s.glId)
        glTexParameteri(type.glId, GL_TEXTURE_WRAP_T, t.glId)
    }

    override fun textureWrapping(
        type: TextureType,
        s: WrappingType,
        t: WrappingType,
        r: WrappingType
    ) {
        debugPrint("glTextureWrapping", type, s, t, r)
        glTexParameteri(type.glId, GL_TEXTURE_WRAP_S, s.glId)
        glTexParameteri(type.glId, GL_TEXTURE_WRAP_T, t.glId)
        glTexParameteri(type.glId, GL_TEXTURE_WRAP_R, r.glId)
    }

    override fun textureComparisonMode(
        type: TextureType,
        mode: TextureComparisonMode
    ) {
        debugPrint("glTextureComparisonMode", type, mode)
        glTexParameteri(type.glId, GL_TEXTURE_COMPARE_MODE, mode.glId)
    }

    override fun textureComparisonFunc(
        type: TextureType,
        mode: ComparisonFunc
    ) {
        debugPrint("glTextureComparisonFunc", type, mode)
        glTexParameteri(type.glId, GL_TEXTURE_COMPARE_FUNC, mode.glId)
    }
}