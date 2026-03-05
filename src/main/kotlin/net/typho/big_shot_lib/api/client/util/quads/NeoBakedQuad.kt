package net.typho.big_shot_lib.api.client.util.quads

import net.minecraft.core.Direction
import net.typho.big_shot_lib.api.client.opengl.buffers.NeoVertexConsumer
import java.util.function.UnaryOperator

interface NeoBakedQuad {
    val vertices: Array<NeoVertexData>
    val tintIndex: Int?
    val direction: Direction?
    val sprite: NeoAtlasSprite
    val shade: Boolean

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

    fun withVertices(function: (index: Int, vertex: NeoVertexData) -> NeoVertexData): NeoBakedQuad {
        val parent = this
        val vertices = parent.vertices.mapIndexed(function).toTypedArray()
        return object : NeoBakedQuad {
            override val vertices: Array<NeoVertexData> = vertices
            override val tintIndex: Int?
                get() = parent.tintIndex
            override val direction: Direction?
                get() = parent.direction
            override val sprite: NeoAtlasSprite
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
            override val sprite: NeoAtlasSprite
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
            override val sprite: NeoAtlasSprite
                get() = parent.sprite
            override val shade: Boolean
                get() = parent.shade
        }
    }

    fun withSprite(sprite: UnaryOperator<NeoAtlasSprite>): NeoBakedQuad {
        val parent = this
        val sprite = sprite.apply(parent.sprite)
        return object : NeoBakedQuad {
            override val vertices: Array<NeoVertexData>
                get() = parent.vertices
            override val tintIndex: Int?
                get() = parent.tintIndex
            override val direction: Direction?
                get() = parent.direction
            override val sprite: NeoAtlasSprite = sprite
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
            override val sprite: NeoAtlasSprite
                get() = parent.sprite
            override val shade: Boolean = shade
        }
    }
}