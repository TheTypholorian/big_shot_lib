package net.typho.big_shot_lib.api.client.opengl.util

import org.lwjgl.opengl.GL11.*

enum class GlIndexType(
    @JvmField
    val glId: Int,
    @JvmField
    val sizeBytes: Int
) : GlNamed {
    UBYTE(GL_UNSIGNED_BYTE, UByte.SIZE_BYTES),
    USHORT(GL_UNSIGNED_SHORT, UShort.SIZE_BYTES),
    UINT(GL_UNSIGNED_INT, UInt.SIZE_BYTES);

    override fun glId() = glId
}