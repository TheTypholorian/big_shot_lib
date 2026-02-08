package net.typho.big_shot_lib.api.buffers

import net.typho.big_shot_lib.api.BufferUploader
import net.typho.big_shot_lib.api.GlResource
import net.typho.big_shot_lib.api.StateManager
import net.typho.big_shot_lib.api.errors.InvalidEnumException
import java.nio.ByteBuffer

open class GlBuffer(
    glId: Int,
    @JvmField
    val type: BufferType,
    @JvmField
    val usage: BufferUsage
) : GlResource.Indexed(glId), BufferUploader {
    constructor(type: BufferType, usage: BufferUsage) : this(StateManager.INSTANCE.createBuffer(), type, usage)

    override fun bind(glId: Int) {
        StateManager.INSTANCE.bindBuffer(type, glId)
    }

    override fun bindBase(index: Int, glId: Int) {
        if (!type.isIndexed) {
            throw InvalidEnumException("$this is not indexed")
        }

        StateManager.INSTANCE.bindBufferBase(type, index, glId)
    }

    override fun free() {
        StateManager.INSTANCE.deleteBuffer(glId)
    }

    override fun upload(buffer: ByteBuffer) {
        StateManager.INSTANCE.bufferData(type, buffer, usage)
    }

    override fun toString(): String {
        return "${javaClass.simpleName}(glId=$glId, type=$type, usage=$usage)"
    }
}