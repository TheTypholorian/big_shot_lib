package net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferAccess
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBufferUsage
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlBuffer
import net.typho.big_shot_lib.api.util.buffer.NeoBuffer
import org.lwjgl.system.NativeResource
import java.io.DataOutput

sealed class GlBufferWriter(
    @JvmField
    val glBuffer: GlBuffer,
    @JvmField
    val target: GlBufferTarget
) : NativeResource {
    abstract val buffer: NeoBuffer

    fun write(index: Long = 0L): DataOutput = buffer.write(index)

    class Regular(
        glBuffer: GlBuffer,
        target: GlBufferTarget,
        length: Long,
        @JvmField
        val usage: GlBufferUsage
    ) : GlBufferWriter(glBuffer, target) {
        override val buffer: NeoBuffer = NeoBuffer.Native(length)

        override fun free() {
            glBuffer.bind(target).use {
                it.bufferData(buffer, usage)
                (buffer as NeoBuffer.Native).free()
            }
        }
    }

    class RegularRange(
        glBuffer: GlBuffer,
        target: GlBufferTarget,
        @JvmField
        val offset: Long,
        length: Long
    ) : GlBufferWriter(glBuffer, target) {
        override val buffer: NeoBuffer = NeoBuffer.Native(length)

        override fun free() {
            glBuffer.bind(target).use {
                it.bufferSubData(offset, buffer)
                (buffer as NeoBuffer.Native).free()
            }
        }
    }

    class Mapped(
        glBuffer: GlBuffer,
        target: GlBufferTarget,
        length: Long
    ) : GlBufferWriter(glBuffer, target) {
        @JvmField
        val bound = glBuffer.bind(target)
        override val buffer: NeoBuffer = NeoBuffer.Native(bound.mapBuffer(GlBufferAccess.WRITE_ONLY, length), length)

        override fun free() {
            bound.unmapBuffer()
            bound.unbind()
        }
    }

    class MappedRange(
        glBuffer: GlBuffer,
        target: GlBufferTarget,
        offset: Long,
        length: Long
    ) : GlBufferWriter(glBuffer, target) {
        @JvmField
        val bound = glBuffer.bind(target)
        override val buffer: NeoBuffer = NeoBuffer.Native(bound.mapBufferRange(GlBufferAccess.WRITE_ONLY, offset, length), length)

        override fun free() {
            bound.unmapBuffer()
            bound.unbind()
        }
    }

    enum class Mode(
        @JvmField
        val canLazyUpload: Boolean
    ) {
        REGULAR(true) {
            override fun create(glBuffer: GlBuffer, target: GlBufferTarget, length: Long, usage: GlBufferUsage): GlBufferWriter {
                return Regular(glBuffer, target, length, usage)
            }
        },
        MAPPED(false) {
            override fun create(
                glBuffer: GlBuffer,
                target: GlBufferTarget,
                length: Long,
                usage: GlBufferUsage
            ): GlBufferWriter {
                glBuffer.bind(target).use { bound ->
                    bound.bufferData(length, usage)
                    return Mapped(glBuffer, target, length)
                }
            }
        };

        abstract fun create(glBuffer: GlBuffer, target: GlBufferTarget, length: Long, usage: GlBufferUsage): GlBufferWriter
    }
}