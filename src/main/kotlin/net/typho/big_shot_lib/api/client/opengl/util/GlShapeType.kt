package net.typho.big_shot_lib.api.client.opengl.util

import net.typho.big_shot_lib.api.util.buffers.BufferUploader
import org.lwjgl.opengl.GL11.*
import java.nio.ByteBuffer
import java.nio.ByteOrder

enum class GlShapeType(
    override val glId: Int,
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

    fun makeIndices(count: Int): IntArray {
        return when (this) {
            QUADS -> {
                val array = IntArray(count)

                var j = 0
                var k = 0
                for (i in 0 until array.size) {
                    array[k] = j
                    array[k + 1] = j + 1
                    array[k + 2] = j + 2
                    array[k + 3] = j + 2
                    array[k + 4] = j + 3
                    array[k + 5] = j

                    j += 4
                    k += 6
                }

                array
            }
            LINES -> {
                val array = IntArray(count)

                var j = 0
                var k = 0
                for (i in 0 until array.size) {
                    array[k] = j
                    array[k + 1] = j + 1
                    array[k + 2] = j + 2
                    array[k + 3] = j + 3
                    array[k + 4] = j + 2
                    array[k + 5] = j + 1

                    j += 4
                    k += 6
                }

                array
            }
            else -> IntArray(count) { it }
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
        val buffer = ByteBuffer.allocateDirect(count * type.sizeBytes)
            .order(ByteOrder.nativeOrder())

        when (type) {
            GlIndexType.UBYTE -> makeIndices(count).forEach { buffer.put(it.toByte()) }
            GlIndexType.USHORT -> makeIndices(count).forEach { buffer.putShort(it.toShort()) }
            GlIndexType.UINT -> makeIndices(count).forEach { buffer.putInt(it) }
        }

        out.upload(buffer.flip())
    }
}