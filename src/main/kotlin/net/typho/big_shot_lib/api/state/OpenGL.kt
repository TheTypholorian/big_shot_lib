package net.typho.big_shot_lib.api.state

import net.typho.big_shot_lib.api.buffers.BufferType
import net.typho.big_shot_lib.api.buffers.BufferUsage
import net.typho.big_shot_lib.api.textures.InterpolationType
import net.typho.big_shot_lib.api.textures.TextureFormat
import net.typho.big_shot_lib.api.textures.WrappingType
import net.typho.big_shot_lib.api.util.IColor
import java.nio.ByteBuffer
import java.util.*

interface OpenGL {
    fun enable(flag: GlFlag)

    fun disable(flag: GlFlag)

    fun blendColor(color: IColor)

    fun blendEquation(eq: BlendEquation)

    fun blendFunc(src: BlendFactor, dst: BlendFactor)

    fun blendFuncSeparate(src: BlendFactor, dst: BlendFactor, srcA: BlendFactor, dstA: BlendFactor)

    fun colorMask(mask: ColorMask)

    fun cullFace(face: CullFace)

    fun depthMask(mask: Boolean)

    fun depthFunc(func: ComparisonMode)

    fun polygonMode(mode: PolygonMode)

    fun scissor(x: Int, y: Int, width: Int, height: Int)

    fun stencilFunc(func: StencilFunc)

    fun stencilMask(mask: Int)

    fun stencilOp(op: StencilOp)

    fun createBuffer(): Int

    fun bindBuffer(type: BufferType, glId: Int)

    fun bindBufferBase(type: BufferType, index: Int, glId: Int)

    fun bufferData(type: BufferType, buffer: ByteBuffer, usage: BufferUsage)

    fun deleteBuffer(glId: Int)

    fun createRenderBuffer(): Int

    fun bindRenderBuffer(glId: Int)

    fun deleteRenderBuffer(glId: Int)

    fun createTexture(): Int

    fun bindTexture(type: Int, glId: Int)

    fun textureInterpolation(type: Int, min: InterpolationType, mag: InterpolationType, glId: Int)

    fun textureWrapping(type: Int, s: WrappingType, t: WrappingType, glId: Int)

    /**
    glTexImage2D(
    GL_TEXTURE_2D,
    0,
    format.internalId,
    width,
    height,
    0,
    format.glId,
    format.type,
    buffer
    )
     */
    fun textureData2D(format: TextureFormat, width: Int, height: Int, buffer: ByteBuffer)

    fun deleteTexture(glId: Int)

    fun createVertexArray(): Int

    fun bindVertexArray(glId: Int)

    fun deleteVertexArray(glId: Int)

    companion object {
        @JvmField
        val INSTANCE: OpenGL = ServiceLoader.load(OpenGL.INSTANCE::class.java).findFirst().orElseThrow()
    }
}