package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import org.lwjgl.opengl.GL11.*
import java.io.DataOutput

enum class GlIndexDataType(
    override val glId: Int,
    @JvmField
    val sizeBytes: Int
) : GlConstant {
    BYTE(GL_UNSIGNED_BYTE, UByte.SIZE_BYTES) {
        override fun write(output: DataOutput, value: Int) {
            output.writeByte(value)
        }
    },
    SHORT(GL_UNSIGNED_SHORT, UShort.SIZE_BYTES) {
        override fun write(output: DataOutput, value: Int) {
            output.writeShort(value)
        }
    },
    INT(GL_UNSIGNED_INT, UInt.SIZE_BYTES) {
        override fun write(output: DataOutput, value: Int) {
            output.writeInt(value)
        }
    };

    abstract fun write(output: DataOutput, value: Int)
}