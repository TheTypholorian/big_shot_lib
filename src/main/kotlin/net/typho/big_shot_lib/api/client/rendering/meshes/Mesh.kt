package net.typho.big_shot_lib.api.client.rendering.meshes

import com.mojang.blaze3d.vertex.ByteBufferBuilder
import com.mojang.blaze3d.vertex.MeshData
import net.typho.big_shot_lib.api.client.rendering.buffers.BufferType
import net.typho.big_shot_lib.api.client.rendering.buffers.BufferUsage
import net.typho.big_shot_lib.api.client.rendering.buffers.GlBuffer
import net.typho.big_shot_lib.api.client.rendering.util.GlBindable
import net.typho.big_shot_lib.api.client.rendering.util.GlIndexType
import net.typho.big_shot_lib.api.client.rendering.util.GlShapeType
import net.typho.big_shot_lib.api.services.WrapperUtil
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
    val vbo: GlBuffer = GlBuffer(BufferType.ARRAY_BUFFER, usage),
    @JvmField
    val ebo: GlBuffer = GlBuffer(BufferType.ELEMENT_ARRAY_BUFFER, usage)
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

        ebo.bind()

        val buffer = built.indexBuffer()

        if (buffer != null) {
            ebo.upload(buffer)
        } else {
            mode.uploadIndices(indexCount, ebo)
        }

        vao.unbind()
        ebo.unbind()
    }

    inner class Builder(
        @JvmField
        val buffer: ByteBufferBuilder = ByteBufferBuilder(1536)
    ) : NeoBufferBuilder(WrapperUtil.INSTANCE.createBufferBuilder(buffer, mode, format)) {
        fun end() {
            upload(buildOrThrow())
            buffer.close()
        }
    }
}