package net.typho.big_shot_lib.api.client.rendering.util.quad

import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexConsumer
import net.minecraft.core.Direction
import java.util.function.UnaryOperator
import kotlin.collections.mapIndexed

interface NeoBakedQuad {
    val vertices: Array<NeoVertexData>
    val tintIndex: Int?
    val direction: Direction?
    val sprite: TextureAtlasSprite?
    val shade: Boolean

    val v0: NeoVertexData
        get() = vertices[0]
    val v1: NeoVertexData
        get() = vertices[1]
    val v2: NeoVertexData
        get() = vertices[2]
    val v3: NeoVertexData
        get() = vertices[3]

    fun put(consumer: VertexConsumer) {
        v0.put(consumer)
        v1.put(consumer)
        v2.put(consumer)
        v3.put(consumer)
    }

    fun withCalculatedNormals(): NeoBakedQuad {
        var normal = (v1.pos - v0.pos) cross (v3.pos - v0.pos)
        normal /= normal.length
        return withVertices { index, vertex ->
            NeoVertexData(
                vertex,
                normal = normal
            )
        }
    }

    fun withVertices(function: (index: Int, vertex: NeoVertexData) -> NeoVertexData): NeoBakedQuad {
        val parent = this
        val vertices = Array(4) { function(it, parent.vertices[it]) }
        return object : NeoBakedQuad {
            override val vertices: Array<NeoVertexData> = vertices
            override val tintIndex: Int?
                get() = parent.tintIndex
            override val direction: Direction?
                get() = parent.direction
            override val sprite: TextureAtlasSprite?
                get() = parent.sprite
            override val shade: Boolean
                get() = parent.shade
        }
    }

    fun withTintIndex(tintIndex: UnaryOperator<Int?>): NeoBakedQuad {
        val parent = this
        val tintIndex = tintIndex.apply(parent.tintIndex)
        return object : NeoBakedQuad {
            override val vertices: Array<NeoVertexData>
                get() = parent.vertices
            override val tintIndex: Int? = tintIndex
            override val direction: Direction?
                get() = parent.direction
            override val sprite: TextureAtlasSprite?
                get() = parent.sprite
            override val shade: Boolean
                get() = parent.shade
        }
    }

    fun withDirection(direction: UnaryOperator<Direction?>): NeoBakedQuad {
        val parent = this
        val direction = direction.apply(parent.direction)
        return object : NeoBakedQuad {
            override val vertices: Array<NeoVertexData>
                get() = parent.vertices
            override val tintIndex: Int?
                get() = parent.tintIndex
            override val direction: Direction? = direction
            override val sprite: TextureAtlasSprite?
                get() = parent.sprite
            override val shade: Boolean
                get() = parent.shade
        }
    }

    fun withSprite(sprite: UnaryOperator<TextureAtlasSprite?>): NeoBakedQuad {
        val parent = this
        val sprite = sprite.apply(parent.sprite)
        return object : NeoBakedQuad {
            override val vertices: Array<NeoVertexData>
                get() = parent.vertices
            override val tintIndex: Int?
                get() = parent.tintIndex
            override val direction: Direction?
                get() = parent.direction
            override val sprite: TextureAtlasSprite? = sprite
            override val shade: Boolean
                get() = parent.shade
        }
    }

    fun withShade(shade: UnaryOperator<Boolean>): NeoBakedQuad {
        val parent = this
        val shade = shade.apply(parent.shade)
        return object : NeoBakedQuad {
            override val vertices: Array<NeoVertexData>
                get() = parent.vertices
            override val tintIndex: Int?
                get() = parent.tintIndex
            override val direction: Direction?
                get() = parent.direction
            override val sprite: TextureAtlasSprite?
                get() = parent.sprite
            override val shade: Boolean = shade
        }
    }

    abstract class Consumer : NeoVertexData.Consumer() {
        private var vertices = arrayOfNulls<NeoVertexData>(4)
        private var index = 0
        @JvmField
        var tintIndex: Int? = null
        @JvmField
        var direction: Direction? = null
        @JvmField
        var sprite: TextureAtlasSprite? = null
        @JvmField
        var shade = false

        final override fun take(vertex: NeoVertexData) {
            vertices[index++] = vertex

            if (index == 4) {
                index = 0
                take(BasicBakedQuad(Array(4) { vertices[it]!! }, tintIndex, direction, sprite, shade))
            }
        }

        abstract fun take(quad: NeoBakedQuad)
    }
}