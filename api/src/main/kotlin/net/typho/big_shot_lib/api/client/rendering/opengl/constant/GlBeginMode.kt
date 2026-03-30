package net.typho.big_shot_lib.api.client.rendering.opengl.constant

import org.lwjgl.opengl.GL11.*

enum class GlBeginMode(
    override val glId: Int,
    @JvmField
    val indexData: IndexData? = null
) : GlConstant {
    POINTS(GL_POINTS),
    LINES(GL_LINES),
    LINE_LOOP(GL_LINE_LOOP),
    LINE_STRIP(GL_LINE_STRIP),
    TRIANGLES(GL_TRIANGLES),
    TRIANGLE_STRIP(GL_TRIANGLE_STRIP),
    TRIANGLE_FAN(GL_TRIANGLE_FAN),
    QUADS(GL_TRIANGLES, IndexData(4, intArrayOf(0, 1, 2, 2, 3, 0)));

    data class IndexData(
        @JvmField
        val stride: Int,
        @JvmField
        val offsets: IntArray
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is IndexData) return false

            if (stride != other.stride) return false
            if (!offsets.contentEquals(other.offsets)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = stride
            result = 31 * result + offsets.contentHashCode()
            return result
        }
    }
}