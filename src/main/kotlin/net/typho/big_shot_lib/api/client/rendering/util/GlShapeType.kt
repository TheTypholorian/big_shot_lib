package net.typho.big_shot_lib.api.client.rendering.util

import net.typho.big_shot_lib.api.util.buffers.BufferUploader
import org.lwjgl.opengl.GL11.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.stream.IntStream

enum class GlShapeType(
    @JvmField
    val glId: Int,
    @JvmField
    val length: Int,
    @JvmField
    val stride: Int,
    @JvmField
    val connected: Boolean
) : GlNamed {
    LINES(GL_TRIANGLES, 2, 2, false),
    LINE_STRIP(GL_TRIANGLE_STRIP, 2, 1, true),
    DEBUG_LINES(GL_LINES, 2, 2, false),
    DEBUG_LINE_STRIP(GL_LINE_STRIP, 2, 1, true),
    TRIANGLES(GL_TRIANGLES, 3, 3, false),
    TRIANGLE_STRIP(GL_TRIANGLE_STRIP, 3, 1, true),
    TRIANGLE_FAN(GL_TRIANGLE_FAN, 3, 1, true),
    QUADS(GL_TRIANGLES, 4, 4, false);

    override fun glId() = glId

    fun streamIndices(count: Int): IntStream {
        return when (this) {
            QUADS -> IntStream.range(0, count / 6)
                .map { i -> i * 4 }
                .flatMap { i -> IntStream.of(i, i + 1, i + 2, i + 2, i + 3, i) }
            LINES -> IntStream.range(0, count / 6)
                .map { i -> i * 4 }
                .flatMap { i -> IntStream.of(i, i + 1, i + 2, i + 3, i + 2, i + 1) }
            else -> IntStream.range(0, count)
        }
    }

    fun uploadIndices(count: Int, out: BufferUploader): GlIndexType {
        val type = when (count) {
            count and 0xFF -> GlIndexType.UBYTE
            count and 0xFFFF -> GlIndexType.USHORT
            else -> GlIndexType.UINT
        }
        uploadIndices(
            count,
            type,
            out
        )
        return type
    }

    fun uploadIndices(count: Int, type: GlIndexType, out: BufferUploader) {
        when (type) {
            GlIndexType.UBYTE -> out.upload(
                ByteBuffer.allocateDirect(count * type.sizeBytes)
                    .order(ByteOrder.nativeOrder())
                    .put(streamIndices(count).toArray().map { it.toByte() }.toByteArray())
                    .flip()
            )
            GlIndexType.USHORT -> out.upload(
                ByteBuffer.allocateDirect(count * type.sizeBytes)
                    .order(ByteOrder.nativeOrder())
                    .asShortBuffer()
                    .put(streamIndices(count).toArray().map { it.toShort() }.toShortArray())
                    .flip()
            )
            GlIndexType.UINT -> out.upload(
                ByteBuffer.allocateDirect(count * type.sizeBytes)
                    .order(ByteOrder.nativeOrder())
                    .asIntBuffer()
                    .put(streamIndices(count).toArray())
                    .flip()
            )
        }
    }
}