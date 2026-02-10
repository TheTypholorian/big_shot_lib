package net.typho.big_shot_lib.api.buffers

import net.typho.big_shot_lib.api.errors.InvalidEnumException
import net.typho.big_shot_lib.api.state.OpenGL
import net.typho.big_shot_lib.api.util.BufferUploader
import net.typho.big_shot_lib.api.util.GlResource
import java.nio.ByteBuffer

open class GlBuffer(
    glId: Int,
    @JvmField
    val type: BufferType,
    @JvmField
    val usage: BufferUsage
) : GlResource.Indexed(glId), BufferUploader {
    constructor(type: BufferType, usage: BufferUsage) : this(OpenGL.INSTANCE.createBuffer(), type, usage)

    override fun bind(glId: Int) {
        OpenGL.INSTANCE.bindBuffer(type, glId)
    }

    override fun bindBase(index: Int, glId: Int) {
        if (!type.isIndexed) {
            throw InvalidEnumException("$this is not indexed")
        }

        OpenGL.INSTANCE.bindBufferBase(type, index, glId)
    }

    override fun free() {
        OpenGL.INSTANCE.deleteBuffer(glId)
    }

    override fun upload(buffer: ByteBuffer) {
        OpenGL.INSTANCE.bufferData(type, buffer, usage)
    }

    override fun uploadNull() {
        OpenGL.INSTANCE.bufferData(type, 0L, usage)
    }

    override fun toString(): String {
        return "${javaClass.simpleName}(glId=$glId, type=$type, usage=$usage)"
    }
}