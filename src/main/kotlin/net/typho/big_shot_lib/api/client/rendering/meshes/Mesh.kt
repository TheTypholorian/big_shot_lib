package net.typho.big_shot_lib.api.client.rendering.meshes

import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.ByteBufferBuilder
import com.mojang.blaze3d.vertex.MeshData
import com.mojang.blaze3d.vertex.VertexFormat
import net.typho.big_shot_lib.api.client.rendering.buffers.BufferType
import net.typho.big_shot_lib.api.client.rendering.buffers.BufferUsage
import net.typho.big_shot_lib.api.client.rendering.buffers.GlBuffer
import net.typho.big_shot_lib.api.client.rendering.services.GlUtil
import net.typho.big_shot_lib.api.client.rendering.util.GlBindable
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
    val vbo: GlBuffer = GlBuffer(BufferType.ARRAY_BUFFER, usage),
    @JvmField
    val ebo: GlBuffer = GlBuffer(BufferType.ELEMENT_ARRAY_BUFFER, usage)
) : GlBindable, NativeResource {
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

    fun upload(built: MeshData) {
        indexCount = built.drawState().indexCount
        indexType = built.drawState().indexType

        vao.bind()

        vbo.bind()
        vbo.upload(built.vertexBuffer())
        GlUtil.INSTANCE.initBufferState(format)
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
        ebo.unbind()
    }

    inner class Builder(
        @JvmField
        val buffer: ByteBufferBuilder = ByteBufferBuilder(1536)
    ) : NeoBufferBuilder(BufferBuilder(buffer, mode, format)) {
        fun end() {
            upload(buildOrThrow())
            buffer.close()
        }
    }
}