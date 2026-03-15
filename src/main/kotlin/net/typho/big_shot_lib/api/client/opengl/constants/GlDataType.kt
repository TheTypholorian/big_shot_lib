package net.typho.big_shot_lib.api.client.opengl.constants

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.glVertexAttribIPointer

enum class GlDataType(
    override val glId: Int,
    @JvmField
    val sizeBytes: Int
) : GlConstant {
    UNSIGNED_BYTE(GL_UNSIGNED_BYTE, UByte.SIZE_BYTES),
    BYTE(GL_BYTE, Byte.SIZE_BYTES),
    UNSIGNED_SHORT(GL_UNSIGNED_SHORT, UShort.SIZE_BYTES),
    SHORT(GL_SHORT, Short.SIZE_BYTES),
    UNSIGNED_INT(GL_UNSIGNED_INT, UInt.SIZE_BYTES),
    INT(GL_INT, Int.SIZE_BYTES),
    FLOAT(GL_FLOAT, Float.SIZE_BYTES),
    BYTES_2(GL_2_BYTES, 2),
    BYTES_3(GL_3_BYTES, 3),
    BYTES_4(GL_4_BYTES, 4),
    DOUBLE(GL_DOUBLE, Double.SIZE_BYTES);

    fun vertexAttribPointer(
        index: Int,
        size: Int,
        normalized: Boolean?,
        stride: Int,
        pointer: Long
    ) {
        if (normalized == null) {
            glVertexAttribIPointer(index, size, glId, stride, pointer)
        } else {
            glVertexAttribPointer(index, size, glId, normalized, stride, pointer)
        }
    }
}