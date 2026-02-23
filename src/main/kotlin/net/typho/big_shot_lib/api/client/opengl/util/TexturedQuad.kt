package net.typho.big_shot_lib.api.client.opengl.util

import com.mojang.blaze3d.vertex.VertexConsumer
import net.typho.big_shot_lib.api.client.opengl.buffers.NeoVertexConsumer
import net.typho.big_shot_lib.api.util.buffers.BufferUtil.putVec2f
import net.typho.big_shot_lib.api.util.buffers.BufferUtil.putVec3f
import org.joml.Vector2f
import org.joml.Vector3f

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

    fun buildGeometry(consumer: NeoVertexConsumer) {
        consumer.vertex(v1).textureUV(uv1)
        consumer.vertex(v2).textureUV(uv2)
        consumer.vertex(v3).textureUV(uv3)
        consumer.vertex(v4).textureUV(uv4)
    }

    fun buildGeometry(consumer: VertexConsumer) {
        consumer.addVertex(v1).setUv(uv1.x, uv1.y)
        consumer.addVertex(v2).setUv(uv2.x, uv2.y)
        consumer.addVertex(v3).setUv(uv3.x, uv3.y)
        consumer.addVertex(v4).setUv(uv4.x, uv4.y)
    }

    fun copy(): TexturedQuad {
        return TexturedQuad(
            Vector3f(v1),
            Vector3f(v2),
            Vector3f(v3),
            Vector3f(v4),

            Vector2f(uv1),
            Vector2f(uv2),
            Vector2f(uv3),
            Vector2f(uv4),
        )
    }

    fun offset(vec: Vector3f, dest: TexturedQuad = this): TexturedQuad {
        dest.v1.add(vec)
        dest.v2.add(vec)
        dest.v3.add(vec)
        dest.v4.add(vec)
        return dest
    }
}