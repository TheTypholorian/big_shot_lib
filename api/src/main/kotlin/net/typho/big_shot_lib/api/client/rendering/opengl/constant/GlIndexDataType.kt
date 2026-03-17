package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import org.lwjgl.opengl.GL11.*

enum class GlIndexDataType(
    override val glId: Int,
    @JvmField
    val sizeBytes: Int
) : GlConstant {
    BYTE(GL_UNSIGNED_BYTE, UByte.SIZE_BYTES),
    SHORT(GL_UNSIGNED_SHORT, UShort.SIZE_BYTES),
    INT(GL_UNSIGNED_INT, UInt.SIZE_BYTES);
}