package net.typho.big_shot_lib.api.client.rendering.buffers

import net.typho.big_shot_lib.api.client.rendering.state.GlStateStack
import net.typho.big_shot_lib.api.client.rendering.state.OpenGL
import net.typho.big_shot_lib.api.client.rendering.util.GlIndexedBindable
import net.typho.big_shot_lib.api.client.rendering.util.GlResource
import net.typho.big_shot_lib.api.errors.InvalidEnumException
import net.typho.big_shot_lib.api.util.buffers.BufferUploader
import java.nio.ByteBuffer

open class GlBuffer(
    glId: Int,
    @JvmField
    val type: BufferType,
    @JvmField
    val usage: BufferUsage
) : GlResource(glId, GlStateStack.buffers[type]!!), BufferUploader, GlIndexedBindable {
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

    override fun toString(): String {
        return "${javaClass.simpleName}(glId=$glId, type=$type, usage=$usage)"
    }
}