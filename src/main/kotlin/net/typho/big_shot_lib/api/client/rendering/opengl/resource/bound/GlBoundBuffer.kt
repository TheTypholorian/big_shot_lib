package net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound

import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferAccess
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferUsage
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.state.GlStateStack
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlBuffer
import net.typho.big_shot_lib.api.util.buffer.NeoBuffer
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL32.glGetBufferParameteri64
import org.lwjgl.opengl.GL44.GL_BUFFER_STORAGE_FLAGS

interface GlBoundBuffer : GlBoundResource<GlBuffer> {
    val target: GlBufferTarget
    val size: Long
    val usage: GlBufferUsage
    val storageFlags: Int

    val isMapped: Boolean
    val access: GlBufferAccess
    val accessFlags: Int
    val mapOffset: Long
    val mapLength: Long

    fun bufferData(size: Long, usage: GlBufferUsage)

    fun bufferData(data: NeoBuffer, usage: GlBufferUsage)

    fun bufferSubData(offset: Long, data: NeoBuffer)

    fun mapBuffer(access: GlBufferAccess, size: Long): GlMappedBuffer

    fun mapBuffer(access: GlBufferAccess, size: Long, out: (buffer: NeoBuffer) -> Unit) {
        mapBuffer(access, size).also { out(it.buffer) }.free()
    }

    open class Basic(
        override val resource: GlBuffer,
        override val target: GlBufferTarget,
        override val handle: GlStateStack.Handle<Int>
    ) : GlBoundBuffer {
        override val size: Long
            get() = glGetBufferParameteri64(target.glId, GL_BUFFER_SIZE)
        override val usage: GlBufferUsage
            get() = GlNamed.getEnum<GlBufferUsage>(glGetBufferParameteri(target.glId, GL_BUFFER_USAGE))
        override val storageFlags: Int
            get() = glGetBufferParameteri(target.glId, GL_BUFFER_STORAGE_FLAGS)
        override val isMapped: Boolean
            get() = glGetBufferParameteri(target.glId, GL_BUFFER_MAPPED) == GL_TRUE
        override val access: GlBufferAccess
            get() = GlNamed.getEnum<GlBufferAccess>(glGetBufferParameteri(target.glId, GL_BUFFER_ACCESS))
        override val accessFlags: Int
            get() = glGetBufferParameteri(target.glId, GL_BUFFER_ACCESS_FLAGS)
        override val mapOffset: Long
            get() = glGetBufferParameteri64(target.glId, GL_BUFFER_MAP_OFFSET)
        override val mapLength: Long
            get() = glGetBufferParameteri64(target.glId, GL_BUFFER_MAP_LENGTH)

        override fun bufferData(
            size: Long,
            usage: GlBufferUsage
        ) {
            glBufferData(target.glId, size, usage.glId)
        }

        override fun bufferData(data: NeoBuffer, usage: GlBufferUsage) {
            nglBufferData(target.glId, data.size, data.address, usage.glId)
        }

        override fun bufferSubData(offset: Long, data: NeoBuffer) {
            nglBufferSubData(target.glId, offset, data.size, data.address)
        }

        override fun mapBuffer(
            access: GlBufferAccess,
            size: Long
        ): GlMappedBuffer {
            return GlMappedBuffer(NeoBuffer.Impl(glMapBuffer(target.glId, access.glId, size, null) ?: throw NullPointerException("Failed to map buffer $target")), target)
        }
    }
}