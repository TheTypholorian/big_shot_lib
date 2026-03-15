package net.typho.big_shot_lib.api.client.opengl.buffers

import com.mojang.blaze3d.vertex.ByteBufferBuilder
import com.mojang.blaze3d.vertex.MeshData
import com.mojang.blaze3d.vertex.VertexSorting
import net.typho.big_shot_lib.api.client.opengl.state.GlBufferType
import net.typho.big_shot_lib.api.client.opengl.util.GlIndexType
import net.typho.big_shot_lib.api.client.opengl.util.GlShapeType
import net.typho.big_shot_lib.api.util.WrapperUtil
import org.lwjgl.system.NativeResource

open class Mesh(
    @JvmField
    val format: NeoVertexFormat,
    @JvmField
    val mode: GlShapeType,
    @JvmField
    val usage: GlBufferUsage,
    @JvmField
    val vao: GlVertexArray = GlVertexArray(),
    @JvmField
    val vbo: GlBuffer = GlBuffer(usage),
    @JvmField
    val ebo: GlBuffer = GlBuffer(usage)
) : NativeResource {
    @JvmField
    protected var indexCount = 0
    @JvmField
    protected var indexType: GlIndexType = GlIndexType.UINT

    override fun free() {
        vao.free()
        vbo.free()
        ebo.free()
    }

    fun draw() {
        vao.bind().use {
            it.drawElements(mode, indexCount, indexType)
        }
    }

    fun upload(built: MeshData) {
        indexCount = built.drawState().indexCount
        indexType = WrapperUtil.INSTANCE.getIndexType(built.drawState())

        vao.bind().use {
            vbo.bind(GlBufferType.ARRAY_BUFFER).use {
                it.upload(built.vertexBuffer())
                format.initVertexArrayState()
            }

            ebo.bindEbo().use {
                val buffer = built.indexBuffer()

                if (buffer != null) {
                    it.upload(buffer)
                } else {
                    indexType = mode.uploadIndices(indexCount, it)
                }
            }
        }
    }

    fun upload(vertices: (builder: Builder) -> Unit) {
        Builder().also(vertices).end()
    }

    fun upload(sorting: VertexSorting, vertices: (builder: Builder) -> Unit) {
        Builder().also(vertices).end(sorting)
    }

    inner class Builder(
        @JvmField
        val buffer: ByteBufferBuilder
    ) : NeoBufferBuilder(WrapperUtil.INSTANCE.createBufferBuilder(buffer, mode, format)) {
        constructor(size: Int = 1536) : this(ByteBufferBuilder(size))

        fun end(sorting: VertexSorting? = null): MeshData {
            val built = buildOrThrow()
            sorting?.let { built.sortQuads(buffer, it) }
            upload(built)
            buffer.close()
            return built
        }
    }
}