package net.typho.big_shot_lib.api.client.opengl.buffers

import net.typho.big_shot_lib.api.client.opengl.state.GlBufferType
import net.typho.big_shot_lib.api.client.opengl.state.GlResourceType
import net.typho.big_shot_lib.api.client.opengl.util.GlResource
import net.typho.big_shot_lib.api.client.opengl.util.OpenGL
import net.typho.big_shot_lib.api.util.buffers.BufferUploader
import org.lwjgl.system.NativeResource
import java.nio.ByteBuffer

open class GlBuffer(
    @JvmField
    val usage: BufferUsage,
    glId: Int = GlResourceType.Buffer.create()
) : GlResource<GlResourceType.Buffer>(GlResourceType.Buffer, glId) {
    fun bind(type: GlBufferType): Bound {
        type.state.push(glId)

        return object : Bound {
            override val buffer = this@GlBuffer
            override val type = type

            override fun free() {
                type.state.pop()
            }

            override fun upload(buffer: ByteBuffer) {
                OpenGL.INSTANCE.bufferData(type.state.glId, buffer, usage)
            }

            override fun uploadNull() {
                OpenGL.INSTANCE.bufferData(type.state.glId, 0L, usage)
            }
        }
    }

    fun bindBase(type: GlBufferType, index: Int) {
        OpenGL.INSTANCE.bindBufferBase(type.state.glId, index, glId)
    }

    fun unbindBase(type: GlBufferType, index: Int) {
        OpenGL.INSTANCE.bindBufferBase(type.state.glId, index, 0)
    }

    override fun toString(): String {
        return "${type.name}(glId=$glId, type=$type, usage=$usage)"
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

    interface Bound : NativeResource, BufferUploader {
        val buffer: GlBuffer
        val type: GlBufferType
    }
}