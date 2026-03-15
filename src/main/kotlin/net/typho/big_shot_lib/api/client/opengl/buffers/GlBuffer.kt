package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.state.GlBufferType
import net.typho.big_shot_lib.api.client.opengl.state.GlResourceType
import net.typho.big_shot_lib.api.client.opengl.state.GlStateTracker
import net.typho.big_shot_lib.api.client.opengl.util.BoundResource
import net.typho.big_shot_lib.api.client.opengl.util.GlBindable
import net.typho.big_shot_lib.api.client.opengl.util.GlResource
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import net.typho.big_shot_lib.api.util.buffers.BufferUploader
import java.nio.ByteBuffer

open class GlBuffer(
    @JvmField
    val usage: GlBufferUsage,
    glId: Int = GlResourceType.BUFFER.create()
) : GlResource(GlResourceType.BUFFER, glId), GlBindable.Typed<GlBufferType, GlBuffer.Bound> {
    override fun bind(type: GlBufferType, tracker: GlStateTracker): Bound {
        type.state.push(glId, tracker)

        return object : Bound {
            override val buffer = this@GlBuffer
            override val type = type

            override fun unbind() {
                type.state.pop(tracker)
            }

            override fun upload(buffer: ByteBuffer) {
                OpenGL.INSTANCE.bufferData(type.glId, buffer, usage)
            }

            override fun uploadNull() {
                OpenGL.INSTANCE.bufferData(type.glId, 0L, usage)
            }
        }
    }

    fun bindEbo(tracker: GlStateTracker = OpenGL.INSTANCE): Bound {
        val type = GlBufferType.ELEMENT_ARRAY_BUFFER
        type.state.raw.set(tracker, glId)

        return object : Bound {
            override val buffer = this@GlBuffer
            override val type = type

            override fun unbind() {
            }

            override fun upload(buffer: ByteBuffer) {
                OpenGL.INSTANCE.bufferData(type.glId, buffer, usage)
            }

            override fun uploadNull() {
                OpenGL.INSTANCE.bufferData(type.glId, 0L, usage)
            }
        }
    }

    fun bindBase(type: GlBufferType, index: Int) {
        OpenGL.INSTANCE.bindBufferBase(type.glId, index, glId)
    }

    fun unbindBase(type: GlBufferType, index: Int) {
        OpenGL.INSTANCE.bindBufferBase(type.glId, index, 0)
    }

    override fun toString(): String {
        return "${resourceType.name}(glId=$glId, type=$resourceType, usage=$usage)"
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

    interface Bound : BoundResource, BufferUploader {
        val buffer: GlBuffer
        val type: GlBufferType
    }
}