package net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferAccess
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferUsage
import net.typho.big_shot_lib.api.util.buffer.NeoBuffer
import org.lwjgl.system.NativeResource

sealed class GlBufferWriter(
    @JvmField
    val glBuffer: GlBoundBuffer,
    @JvmField
    val buffer: NeoBuffer
) : NativeResource {
    fun write(index: Long = 0L) = buffer.write(index)

    class Regular(
        glBuffer: GlBoundBuffer,
        length: Long,
        @JvmField
        val usage: GlBufferUsage
    ) : GlBufferWriter(glBuffer, NeoBuffer.Native(length)) {
        override fun free() {
            glBuffer.bufferData(buffer, usage)
            (buffer as NeoBuffer.Native).free()
        }
    }

    class RegularRange(
        glBuffer: GlBoundBuffer,
        @JvmField
        val offset: Long,
        length: Long
    ) : GlBufferWriter(glBuffer, NeoBuffer.Native(length)) {
        override fun free() {
            glBuffer.bufferSubData(offset, buffer)
            (buffer as NeoBuffer.Native).free()
        }
    }

    class Mapped(
        glBuffer: GlBoundBuffer,
        length: Long
    ) : GlBufferWriter(glBuffer, NeoBuffer.Native(glBuffer.mapBuffer(GlBufferAccess.WRITE_ONLY, length), length)) {
        override fun free() {
            glBuffer.unmapBuffer()
        }
    }

    class MappedRange(
        glBuffer: GlBoundBuffer,
        offset: Long,
        length: Long
    ) : GlBufferWriter(glBuffer, NeoBuffer.Native(glBuffer.mapBufferRange(GlBufferAccess.WRITE_ONLY, offset, length), length)) {
        override fun free() {
            glBuffer.unmapBuffer()
        }
    }

    enum class Mode {
        REGULAR {
            override fun create(glBuffer: GlBoundBuffer, length: Long, usage: GlBufferUsage): GlBufferWriter {
                return Regular(glBuffer, length, usage)
            }
        },
        MAPPED {
            override fun create(
                glBuffer: GlBoundBuffer,
                length: Long,
                usage: GlBufferUsage
            ): GlBufferWriter {
                glBuffer.bufferData(length, usage)
                return Mapped(glBuffer, length)
            }
        };

        abstract fun create(glBuffer: GlBoundBuffer, length: Long, usage: GlBufferUsage): GlBufferWriter
    }
}