package net.typho.big_shot_lib.impl.util

import net.minecraft.client.model.geom.builders.UVPair
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.core.Direction
import net.typho.big_shot_lib.BigShotLib.toNeo
import net.typho.big_shot_lib.api.client.opengl.util.TextureUtil
import net.typho.big_shot_lib.api.client.util.quads.NeoAtlasSprite
import net.typho.big_shot_lib.api.client.util.quads.NeoBakedQuad
import net.typho.big_shot_lib.api.client.util.quads.NeoVertexData
import net.typho.big_shot_lib.api.util.IColor
import org.joml.Vector2f
import org.joml.Vector2fc
import org.joml.Vector2ic
import org.joml.Vector3fc

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
        override val pos: Vector3fc = quad.position(offset)
        override val color: IColor? = null
        override val textureUV: Vector2fc
            get() = Vector2f(
                UVPair.unpackU(quad.packedUV(offset)),
                UVPair.unpackV(quad.packedUV(offset))
            )
        override val overlayUV: Vector2ic? = null
        override val lightUV: Vector2ic? = null
        override val normal: Vector3fc? = null
    }
}