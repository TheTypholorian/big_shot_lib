package net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound

import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferAccess
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferUsage
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundResource.Companion.assertBound
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlBuffer
import net.typho.big_shot_lib.api.client.rendering.opengl.state.GlStateStack
import net.typho.big_shot_lib.api.util.buffer.NeoBuffer
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL32.glGetBufferParameteri64
import org.lwjgl.opengl.GL44.GL_BUFFER_STORAGE_FLAGS
import org.lwjgl.system.MemoryUtil

interface GlBoundBuffer : GlBoundResource<GlBuffer> {
    val isResizable: Boolean

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

    fun getBufferData(offset: Long, data: NeoBuffer)

    fun getBufferData(offset: Long, size: Long = this.size): NeoBuffer {
        val buffer = NeoBuffer.Native(size)
        getBufferData(offset, buffer)
        return buffer
    }

    fun mapBuffer(access: GlBufferAccess, length: Long): Long

    fun mapBufferRange(access: GlBufferAccess, offset: Long, length: Long): Long

    fun unmapBuffer()

    open class Basic(
        override val resource: GlBuffer,
        override val target: GlBufferTarget,
        override val handle: GlStateStack.Handle<Int>
    ) : GlBoundBuffer {
        override val isResizable: Boolean = true
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

        override fun toString(): String {
            return "Bound($resource to $target)"
        }

        override fun bufferData(
            size: Long,
            usage: GlBufferUsage
        ) {
            assertBound {
                glBufferData(target.glId, size, usage.glId)
            }
        }

        override fun bufferData(data: NeoBuffer, usage: GlBufferUsage) {
            assertBound {
                nglBufferData(target.glId, data.size, data.address, usage.glId)
            }
        }

        override fun bufferSubData(offset: Long, data: NeoBuffer) {
            assertBound {
                nglBufferSubData(target.glId, offset, data.size, data.address)
            }
        }

        override fun getBufferData(offset: Long, data: NeoBuffer) {
            assertBound {
                nglGetBufferSubData(target.glId, offset, data.size, data.address)
            }
        }

        override fun mapBuffer(
            access: GlBufferAccess,
            length: Long
        ): Long {
            assertBound {
                val pointer = nglMapBuffer(target.glId, access.glId)

                if (pointer == MemoryUtil.NULL) {
                    throw NullPointerException("Failed to map buffer $target")
                }

                return pointer
            }
        }

        override fun mapBufferRange(
            access: GlBufferAccess,
            offset: Long,
            length: Long
        ): Long {
            assertBound {
                val pointer = nglMapBufferRange(target.glId, offset, length, access.glId)

                if (pointer == MemoryUtil.NULL) {
                    throw NullPointerException("Failed to map buffer $target")
                }

                return pointer
            }
        }

        override fun unmapBuffer() {
            assertBound {
                glUnmapBuffer(target.glId)
            }
        }
    }

    open class NonResizable(
        resource: GlBuffer,
        target: GlBufferTarget,
        handle: GlStateStack.Handle<Int>,
        override val size: Long,
        override val usage: GlBufferUsage
    ) : Basic(resource, target, handle) {
        override val isResizable: Boolean = false

        override fun bufferData(size: Long, usage: GlBufferUsage) {
            if (size != this.size || usage != this.usage) {
                throw UnsupportedOperationException("Buffer cannot be resized")
            }

            super.bufferData(size, usage)
        }

        override fun bufferData(data: NeoBuffer, usage: GlBufferUsage) {
            if (data.size != this.size || usage != this.usage) {
                throw UnsupportedOperationException("Buffer cannot be resized")
            }

            super.bufferData(data, usage)
        }
    }
}