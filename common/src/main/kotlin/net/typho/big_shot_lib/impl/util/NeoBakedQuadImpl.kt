package net.typho.big_shot_lib.impl.util

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.core.Direction
import net.typho.big_shot_lib.BigShotLib.toNeo
import net.typho.big_shot_lib.api.client.opengl.buffers.NeoVertexConsumer
import net.typho.big_shot_lib.api.client.opengl.util.TextureUtil
import net.typho.big_shot_lib.api.client.util.quads.NeoAtlasSprite
import net.typho.big_shot_lib.api.client.util.quads.NeoBakedQuad
import net.typho.big_shot_lib.api.client.util.quads.NeoVertexData
import net.typho.big_shot_lib.api.util.IColor
import org.joml.*
import org.lwjgl.system.MemoryStack

data class NeoBakedQuadImpl(
    @JvmField
    val quad: BakedQuad
) : NeoBakedQuad {
    override val vertices: Array<NeoVertexData> = arrayOf(
        VertexDataImpl(0),
        VertexDataImpl(8),
        VertexDataImpl(16),
        VertexDataImpl(24)
    )
    override val tintIndex: Int?
        get() = if (quad.isTinted) quad.tintIndex else null
    override val direction: Direction?
        get() = quad.direction
    override val sprite: NeoAtlasSprite
        get() = NeoAtlasSpriteImpl(TextureUtil.INSTANCE.getAtlas(quad.sprite.atlasLocation().toNeo()), quad.sprite)
    override val shade: Boolean
        get() = quad.shade

    inner class VertexDataImpl(
        @JvmField
        val offset: Int
    ) : NeoVertexData {
        override val pos: Vector3fc
            get() = Vector3f(
                Float.fromBits(quad.vertices[offset]),
                Float.fromBits(quad.vertices[offset + 1]),
                Float.fromBits(quad.vertices[offset + 2]),
            )
        override val color: IColor
            get() = IColor.RGBA(quad.vertices[offset + 3])
        override val textureUV: Vector2fc
            get() = Vector2f(
                Float.fromBits(quad.vertices[offset + 4]),
                Float.fromBits(quad.vertices[offset + 5])
            )
        override val overlayUV: Vector2ic
            get() {
                val packed = quad.vertices[offset + 6]
                return Vector2i(
                    packed ushr 16,
                    packed and 0xFFFF
                )
            }
        override val lightUV: Vector2ic? = null
        override val normal: Vector3fc
            get() {
                val packed = quad.vertices[offset + 7]
                return Vector3f(
                    (packed ushr 24).toByte() / 127f,
                    ((packed ushr 16) and 0xFF).toByte() / 127f,
                    ((packed ushr 8) and 0xFF).toByte() / 127f
                )
            }

        override fun put(consumer: NeoVertexConsumer) {
            MemoryStack.stackPush().use { stack ->
                val buffer = stack.malloc(DefaultVertexFormat.BLOCK.vertexSize)

                buffer.asIntBuffer().put(quad.vertices, offset, 8)

                consumer.vertex(buffer.getFloat(0), buffer.getFloat(4), buffer.getFloat(8))
                consumer.color(IColor.RGBA(buffer.getInt(12)))
                consumer.textureUV(buffer.getFloat(16), buffer.getFloat(20))
                consumer.overlayUV(buffer.getInt(24))
                consumer.normal(
                    buffer.get(28) / 127f,
                    buffer.get(29) / 127f,
                    buffer.get(30) / 127f
                )
            }
        }
    }
}