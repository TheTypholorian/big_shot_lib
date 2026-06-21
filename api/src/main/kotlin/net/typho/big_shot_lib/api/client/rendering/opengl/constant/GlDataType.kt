package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import net.typho.big_shot_lib.api.client.rendering.opengl.GlNamed
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.glVertexAttribIPointer

enum class GlDataType(
    override val glId: Int,
    @JvmField
    val sizeBytes: Int
) : GlNamed {
    UBYTE(GL_UNSIGNED_BYTE, UByte.SIZE_BYTES),
    BYTE(GL_BYTE, Byte.SIZE_BYTES),
    USHORT(GL_UNSIGNED_SHORT, UShort.SIZE_BYTES),
    SHORT(GL_SHORT, Short.SIZE_BYTES),
    UINT(GL_UNSIGNED_INT, UInt.SIZE_BYTES),
    INT(GL_INT, Int.SIZE_BYTES),
    FLOAT(GL_FLOAT, Float.SIZE_BYTES);

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