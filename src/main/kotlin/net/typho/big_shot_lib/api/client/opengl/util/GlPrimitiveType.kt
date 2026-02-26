package net.typho.big_shot_lib.api.client.opengl.util

import org.lwjgl.opengl.GL11.*

enum class GlPrimitiveType(
    override val glId: Int,
    @JvmField
    val sizeBytes: Int
) : GlNamed {
    FLOAT(GL_FLOAT, Float.SIZE_BYTES),
    UBYTE(GL_UNSIGNED_BYTE, UByte.SIZE_BYTES),
    BYTE(GL_BYTE, Byte.SIZE_BYTES),
    USHORT(GL_UNSIGNED_SHORT, UShort.SIZE_BYTES),
    SHORT(GL_SHORT, Short.SIZE_BYTES),
    UINT(GL_UNSIGNED_INT, UInt.SIZE_BYTES),
    INT(GL_INT, Int.SIZE_BYTES);

    fun vertexAttribPointer(
        index: Int,
        size: Int,
        normalized: Boolean?,
        stride: Int,
        pointer: Long
    ) {
        if (normalized == null) {
            OpenGL.INSTANCE.vertexAttribIPointer(index, size, glId, stride, pointer)
        } else {
            OpenGL.INSTANCE.vertexAttribPointer(index, size, glId, normalized, stride, pointer)
        }
    }
}