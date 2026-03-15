package net.typho.big_shot_lib.api.client.rendering.quad

import net.typho.big_shot_lib.api.math.NeoDirection

@JvmRecord
data class BasicBakedQuad(
    override val vertices: Array<NeoVertexData>,
    override val tintIndex: Int?,
    override val direction: NeoDirection?,
    override val sprite: NeoAtlasSprite,
    override val shade: Boolean
) : NeoBakedQuad {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BasicBakedQuad

        if (tintIndex != other.tintIndex) return false
        if (shade != other.shade) return false
        if (!vertices.contentEquals(other.vertices)) return false
        if (direction != other.direction) return false
        if (sprite != other.sprite) return false

        return true
    }

    override fun hashCode(): Int {
        var result = tintIndex ?: 0
        result = 31 * result + shade.hashCode()
        result = 31 * result + vertices.contentHashCode()
        result = 31 * result + (direction?.hashCode() ?: 0)
        result = 31 * result + sprite.hashCode()
        return result
    }
}