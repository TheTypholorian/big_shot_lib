package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.state.GlStateManager
import net.typho.big_shot_lib.api.client.opengl.util.GlBinder
import net.typho.big_shot_lib.api.client.opengl.util.GlIndexedBindable
import net.typho.big_shot_lib.api.client.opengl.util.GlResource
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import net.typho.big_shot_lib.api.errors.InvalidEnumException
import net.typho.big_shot_lib.api.util.buffers.BufferUploader
import java.nio.ByteBuffer

open class GlBuffer(
    glId: Int,
    @JvmField
    val type: BufferType,
    @JvmField
    val usage: BufferUsage
) : GlResource(glId, GlStateManager.buffers[type] ?: GlBinder.simple { OpenGL.INSTANCE.bindBuffer(type, it) }), BufferUploader, GlIndexedBindable {
    constructor(type: BufferType, usage: BufferUsage) : this(OpenGL.INSTANCE.createBuffer(), type, usage)

    override fun bindBase(index: Int) {
        if (!type.isIndexed) {
            throw InvalidEnumException("$this is not indexed")
        }

        OpenGL.INSTANCE.bindBufferBase(type, index, glId)
    }

    override fun unbindBase(index: Int) {
        if (!type.isIndexed) {
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

    fun cast(type: BufferType) = if (this.type == type) this else GlBuffer(glId, type, usage)

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