package net.typho.big_shot_lib.api.client.rendering.util.quad

import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.resources.model.geometry.BakedQuad
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexConsumer
import net.minecraft.core.Direction
import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.client.InternalClientUtil

interface NeoBakedQuad {
    val vertices: Array<NeoVertexData>

    val v0: NeoVertexData
        get() = vertices[0]
    val v1: NeoVertexData
        get() = vertices[1]
    val v2: NeoVertexData
        get() = vertices[2]
    val v3: NeoVertexData
        get() = vertices[3]

    fun put(consumer: NeoVertexConsumer) {
        v0.put(consumer)
        v1.put(consumer)
        v2.put(consumer)
        v3.put(consumer)
    }

    companion object {
        const val NO_TINT = -1

        @JvmStatic
        @JvmName("create")
        operator fun invoke(
            vertices: Array<NeoVertexData>,
            tintIndex: Int,
            direction: Direction,
            sprite: TextureAtlasSprite,
            shade: Boolean
        ) = InternalClientUtil.INSTANCE.createBakedQuad(vertices, tintIndex, direction, sprite, shade)
    }

    abstract class Consumer(
        @JvmField
        var sprite: TextureAtlasSprite
    ) : NeoVertexData.Consumer() {
        constructor(atlas: TextureAtlas) : this(atlas.getSprite(Identifier.minecraft("missingno")))

        private var vertices = arrayOfNulls<NeoVertexData>(4)
        private var index = 0
        @JvmField
        var tintIndex: Int = NO_TINT
        @JvmField
        var direction: Direction = Direction.DOWN
        @JvmField
        var shade = false

        final override fun take(vertex: NeoVertexData) {
            vertices[index++] = vertex

            if (index == 4) {
                index = 0
                take(NeoBakedQuad(Array(4) { vertices[it]!! }, tintIndex, direction, sprite, shade))
            }
        }

        abstract fun take(quad: BakedQuad)
    }
}