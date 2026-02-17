package net.typho.big_shot_lib.api.client.rendering.meshes

import net.typho.big_shot_lib.api.util.buffers.BufferUtil.putVec2f
import net.typho.big_shot_lib.api.util.buffers.BufferUtil.putVec3f
import org.joml.Vector2f
import org.joml.Vector3f
import java.nio.ByteBuffer

@JvmRecord
data class TexturedQuad(
    @JvmField
    val v1: Vector3f,
    @JvmField
    val v2: Vector3f,
    @JvmField
    val v3: Vector3f,
    @JvmField
    val v4: Vector3f,
    @JvmField
    val uv1: Vector2f,
    @JvmField
    val uv2: Vector2f,
    @JvmField
    val uv3: Vector2f,
    @JvmField
    val uv4: Vector2f,
) {
    companion object {
        const val SIZE_PADDING = 4 * 4 * Float.SIZE_BYTES + 2 * 4 * Float.SIZE_BYTES
        const val SIZE_NO_PADDING = 3 * 4 * Float.SIZE_BYTES + 2 * 4 * Float.SIZE_BYTES
    }

    fun putJavaOrder(buffer: ByteBuffer, padding: Boolean) {
        buffer.putVec3f(v1, padding)
        buffer.putVec3f(v2, padding)
        buffer.putVec3f(v3, padding)
        buffer.putVec3f(v4, padding)

        buffer.putVec2f(uv1)
        buffer.putVec2f(uv2)
        buffer.putVec2f(uv3)
        buffer.putVec2f(uv4)
    }

    fun putVertexOrder(buffer: ByteBuffer, padding: Boolean) {
        buffer.putVec3f(v1, padding).putVec2f(uv1)
        buffer.putVec3f(v2, padding).putVec2f(uv2)
        buffer.putVec3f(v3, padding).putVec2f(uv3)
        buffer.putVec3f(v4, padding).putVec2f(uv4)
    }
}
