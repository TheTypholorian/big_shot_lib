package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.state.GlResourceType
import net.typho.big_shot_lib.api.client.opengl.util.GlIndexedBindable
import net.typho.big_shot_lib.api.client.opengl.util.GlResource
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import net.typho.big_shot_lib.api.errors.InvalidEnumException
import net.typho.big_shot_lib.api.util.buffers.BufferUploader
import java.nio.ByteBuffer

open class GlBuffer(
    type: GlResourceType.Buffer,
    @JvmField
    val usage: BufferUsage,
    glId: Int = type.create()
) : GlResource(type, glId), BufferUploader, GlIndexedBindable {
    override fun bindBase(index: Int) {
        if (type !is GlResourceType.Buffer.Indexed) {
            throw InvalidEnumException("$this is not indexed")
        }

        OpenGL.INSTANCE.bindBufferBase(type, index, glId)
    }

    override fun unbindBase(index: Int) {
        if (type !is GlResourceType.Buffer.Indexed) {
            throw InvalidEnumException("$this is not indexed")
        }

        OpenGL.INSTANCE.bindBufferBase(type, index, 0)
    }

    override fun free() {
        OpenGL.INSTANCE.deleteBuffer(glId)
    }

    override fun upload(buffer: ByteBuffer) {
        bind()
        OpenGL.INSTANCE.bufferData(type, buffer, usage)
        unbind()
    }

    override fun uploadNull() {
        bind()
        OpenGL.INSTANCE.bufferData(type, 0L, usage)
        unbind()
    }

    fun cast(type: GlBufferResourceType) = if (this.type == type) this else GlBuffer(glId, type, usage)

    override fun toString(): String {
        return "${javaClass.simpleName}(glId=$glId, type=$type, usage=$usage)"
    }

    @JvmRecord
    data class Range(
        @JvmField
        val buffer: GlBuffer,
        @JvmField
        val offset: Long,
        @JvmField
        val length: Long
    )
}