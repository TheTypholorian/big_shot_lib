package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import net.typho.big_shot_lib.api.util.buffer.NeoBuffer
import org.lwjgl.opengl.GL11.*

enum class GlIndexDataType(
    override val glId: Int,
    @JvmField
    val sizeBytes: Int
) : GlConstant {
    BYTE(GL_UNSIGNED_BYTE, UByte.SIZE_BYTES) {
        override fun put(buffer: NeoBuffer, index: Long, value: Int) {
            buffer.put(index, value.toByte())
        }
    },
    SHORT(GL_UNSIGNED_SHORT, UShort.SIZE_BYTES) {
        override fun put(buffer: NeoBuffer, index: Long, value: Int) {
            buffer.put(index, value.toShort())
        }
    },
    INT(GL_UNSIGNED_INT, UInt.SIZE_BYTES) {
        override fun put(buffer: NeoBuffer, index: Long, value: Int) {
            buffer.put(index, value)
        }
    };

    abstract fun put(buffer: NeoBuffer, index: Long, value: Int)
}