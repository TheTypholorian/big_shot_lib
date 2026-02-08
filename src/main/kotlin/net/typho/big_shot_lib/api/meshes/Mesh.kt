package net.typho.big_shot_lib.api.meshes

import com.mojang.blaze3d.vertex.ByteBufferBuilder
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.blaze3d.vertex.VertexFormatElement
import net.typho.big_shot_lib.api.buffers.BufferType
import net.typho.big_shot_lib.api.buffers.BufferUsage
import net.typho.big_shot_lib.api.buffers.GlBuffer
import org.lwjgl.system.MemoryUtil.*

open class Mesh(
    @JvmField
    val format: VertexFormat,
    @JvmField
    val mode: VertexFormat.Mode,
    @JvmField
    val usage: BufferUsage,
    @JvmField
    val vao: GlVertexArray = GlVertexArray(),
    @JvmField
    val vbo: GlBuffer = GlBuffer(BufferType.ARRAY, usage)
) {
    @JvmField
    protected var vertices = 0

    init {
        vbo.bind()
        format.setupBufferState()
        vbo.unbind()
    }

    fun drawArrays() {
        vao.drawArrays(mode, vertices)
    }

    inner class Builder(
        @JvmField
        val builder: ByteBufferBuilder = ByteBufferBuilder(1536)
    ) : NeoVertexConsumer {
        private var pointer: Long = 0
        private var vertices: Int = 0

        override fun vertex(
            x: Float,
            y: Float,
            z: Float
        ): NeoVertexConsumer {
            vertices++
            pointer = builder.reserve(format.vertexSize)
            val ptr = pointer + format.offsetsByElement[VertexFormatElement.POSITION.id]
            memPutFloat(ptr, x)
            memPutFloat(ptr + Float.SIZE_BYTES, y)
            memPutFloat(ptr + 2 * Float.SIZE_BYTES, z)
            return this
        }

        override fun color(
            r: Float,
            g: Float,
            b: Float,
            a: Float
        ): NeoVertexConsumer {
            if (format.contains(VertexFormatElement.COLOR)) {
                val ptr = pointer + format.offsetsByElement[VertexFormatElement.COLOR.id]
                memPutByte(ptr, (r * 255).toInt().toByte())
                memPutByte(ptr + 1, (g * 255).toInt().toByte())
                memPutByte(ptr + 2, (b * 255).toInt().toByte())
                memPutByte(ptr + 3, (a * 255).toInt().toByte())
            }

            return this
        }

        override fun textureUV(u: Float, v: Float): NeoVertexConsumer {
            if (format.contains(VertexFormatElement.UV0)) {
                val ptr = pointer + format.offsetsByElement[VertexFormatElement.UV0.id]
                memPutFloat(ptr, u)
                memPutFloat(ptr + Float.SIZE_BYTES, v)
            }

            return this
        }

        override fun overlayUV(u: Int, v: Int): NeoVertexConsumer {
            if (format.contains(VertexFormatElement.UV1)) {
                val ptr = pointer + format.offsetsByElement[VertexFormatElement.UV1.id]
                memPutShort(ptr, u.toShort())
                memPutShort(ptr + Short.SIZE_BYTES, v.toShort())
            }

            return this
        }

        override fun lightUV(u: Int, v: Int): NeoVertexConsumer {
            if (format.contains(VertexFormatElement.UV2)) {
                val ptr = pointer + format.offsetsByElement[VertexFormatElement.UV2.id]
                memPutShort(ptr, u.toShort())
                memPutShort(ptr + Short.SIZE_BYTES, v.toShort())
            }

            return this
        }

        override fun normal(
            x: Float,
            y: Float,
            z: Float
        ): NeoVertexConsumer {
            if (format.contains(VertexFormatElement.NORMAL)) {
                val ptr = pointer + format.offsetsByElement[VertexFormatElement.NORMAL.id]
                memPutFloat(ptr, x)
                memPutFloat(ptr + Float.SIZE_BYTES, y)
                memPutFloat(ptr + 2 * Float.SIZE_BYTES, z)
            }

            return this
        }

        fun end() {
            this@Mesh.vertices = vertices

            builder.build()?.let { result ->
                vbo.bind()
                vbo.upload(result.byteBuffer())
                vbo.unbind()
            }
        }
    }
}