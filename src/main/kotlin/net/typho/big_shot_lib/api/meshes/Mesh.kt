package net.typho.big_shot_lib.api.meshes

import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.ByteBufferBuilder
import com.mojang.blaze3d.vertex.VertexFormat
import net.typho.big_shot_lib.api.buffers.BufferType
import net.typho.big_shot_lib.api.buffers.BufferUsage
import net.typho.big_shot_lib.api.buffers.GlBuffer
import net.typho.big_shot_lib.api.util.Bindable
import org.lwjgl.system.NativeResource
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.stream.IntStream

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
    val vbo: GlBuffer = GlBuffer(BufferType.ARRAY, usage),
    @JvmField
    val ebo: GlBuffer = GlBuffer(BufferType.ELEMENT_ARRAY, usage)
) : Bindable, NativeResource {
    @JvmField
    protected var indexCount = 0
    @JvmField
    protected var indexType: VertexFormat.IndexType = VertexFormat.IndexType.INT

    override fun free() {
        vao.free()
        vbo.free()
        ebo.free()
    }

    override fun bind() {
        vao.bind()
    }

    override fun unbind() {
        vao.unbind()
    }

    fun draw() {
        vao.drawElements(mode, indexCount, indexType)
    }

    inner class Builder(
        @JvmField
        val builder: BufferBuilder = BufferBuilder(ByteBufferBuilder(1536), mode, format)
    ) : NeoVertexConsumer {
        override fun vertex(
            x: Float,
            y: Float,
            z: Float
        ): NeoVertexConsumer {
            builder.addVertex(x, y, z)
            return this
        }

        override fun color(
            r: Float,
            g: Float,
            b: Float,
            a: Float
        ): NeoVertexConsumer {
            builder.setColor(r, g, b, a)
            return this
        }

        override fun textureUV(u: Float, v: Float): NeoVertexConsumer {
            builder.setUv(u, v)
            return this
        }

        override fun overlayUV(u: Int, v: Int): NeoVertexConsumer {
            builder.setUv1(u, v)
            return this
        }

        override fun lightUV(u: Int, v: Int): NeoVertexConsumer {
            builder.setUv2(u, v)
            return this
        }

        override fun normal(
            x: Float,
            y: Float,
            z: Float
        ): NeoVertexConsumer {
            builder.setNormal(x, y, z)
            return this
        }

        fun end() {
            val built = builder.buildOrThrow()

            indexCount = built.drawState().indexCount
            indexType = built.drawState().indexType

            vao.bind()

            vbo.bind()
            vbo.upload(built.vertexBuffer())
            format.setupBufferState()
            vbo.unbind()

            ebo.bind()

            val buffer = built.indexBuffer()

            if (buffer != null) {
                ebo.upload(buffer)
            } else {
                val stream = when (mode) {
                    VertexFormat.Mode.QUADS -> IntStream.range(0, indexCount / 6)
                        .map { i -> i * 4 }
                        .flatMap { i -> IntStream.of(i, i + 1, i + 2, i + 2, i + 3, i) }
                    VertexFormat.Mode.LINES -> IntStream.range(0, indexCount / 6)
                        .map { i -> i * 4 }
                        .flatMap { i -> IntStream.of(i, i + 1, i + 2, i + 3, i + 2, i + 1) }
                    else -> IntStream.range(0, indexCount)
                }

                when (indexType) {
                    VertexFormat.IndexType.SHORT -> {
                        ebo.upload(
                            ByteBuffer.allocateDirect(indexCount * Short.SIZE_BYTES)
                                .order(ByteOrder.nativeOrder())
                                .asShortBuffer()
                                .put(stream.toArray().map { it.toShort() }.toShortArray())
                                .flip()
                        )
                    }

                    VertexFormat.IndexType.INT -> {
                        ebo.upload(
                            ByteBuffer.allocateDirect(indexCount * 4)
                                .order(ByteOrder.nativeOrder())
                                .asIntBuffer()
                                .put(stream.toArray())
                                .flip()
                        )
                    }
                }
            }

            vao.unbind()
        }
    }
}