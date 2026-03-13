package net.typho.big_shot_lib.api.client.opengl.buffers

import com.mojang.blaze3d.vertex.ByteBufferBuilder
import com.mojang.blaze3d.vertex.MeshData
import com.mojang.blaze3d.vertex.VertexSorting
import net.typho.big_shot_lib.api.client.opengl.GlBufferResourceType
import net.typho.big_shot_lib.api.client.opengl.util.GlBindable
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
    val usage: BufferUsage,
    @JvmField
    val vao: GlVertexArray = GlVertexArray(),
    @JvmField
    val vbo: GlBuffer = GlBuffer(GlBufferResourceType.ARRAY_BUFFER, usage),
    @JvmField
    val ebo: GlBuffer = GlBuffer(GlBufferResourceType.ELEMENT_ARRAY_BUFFER, usage)
) : GlBindable, NativeResource {
    @JvmField
    protected var indexCount = 0
    @JvmField
    protected var indexType: GlIndexType = GlIndexType.UINT

    override fun free() {
        vao.free()
        vbo.free()
        ebo.free()
    }

    override fun bind(pushStack: Boolean) {
        vao.bind()
    }

    override fun unbind(popStack: Boolean) {
        vao.unbind()
    }

    fun draw() {
        vao.drawElements(mode, indexCount, indexType)
    }

    fun upload(built: MeshData) {
        indexCount = built.drawState().indexCount
        indexType = WrapperUtil.INSTANCE.getIndexType(built.drawState())

        vao.bind()

        vbo.bind()
        vbo.upload(built.vertexBuffer())
        format.initVertexArrayState()
        vbo.unbind()

        ebo.bind(pushStack = false)

        val buffer = built.indexBuffer()

        if (buffer != null) {
            ebo.upload(buffer)
        } else {
            indexType = mode.uploadIndices(indexCount, ebo)
        }

        vao.unbind()
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