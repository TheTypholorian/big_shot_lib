package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import net.typho.big_shot_lib.api.util.buffer.NeoBuffer
import org.lwjgl.opengl.GL11.*

enum class GlIndexDataType(
    override val glId: Int,
    @JvmField
    val sizeBytes: Int
) : GlConstant {
    BYTE(GL_UNSIGNED_BYTE, UByte.SIZE_BYTES) {
        override fun put(buffer: NeoBuffer, index: Long, value: Int): Long {
            buffer.put(index, value.toByte())
            return index + 1
        }
    },
    SHORT(GL_UNSIGNED_SHORT, UShort.SIZE_BYTES) {
        override fun put(buffer: NeoBuffer, index: Long, value: Int): Long {
            buffer.put(index, value.toShort())
            return index + 2
        }
    },
    INT(GL_UNSIGNED_INT, UInt.SIZE_BYTES) {
        override fun put(buffer: NeoBuffer, index: Long, value: Int): Long {
            buffer.put(index, value)
            return index + 4
        }
    };

    abstract fun put(buffer: NeoBuffer, index: Long, value: Int): Long
}