package net.typho.big_shot_lib.api.client.rendering.util.quad

import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexConsumer
import net.typho.big_shot_lib.api.math.vec.*
import net.typho.big_shot_lib.api.util.NeoColor

data class NeoVertexData(
    @JvmField
    val pos: AbstractVec3<Float>,
    @JvmField
    val color: NeoColor? = null,
    @JvmField
    val textureUV: AbstractVec2<Float>? = null,
    @JvmField
    val overlayUV: AbstractVec2<Int>? = null,
    @JvmField
    val lightUV: AbstractVec2<Int>? = null,
    @JvmField
    val normal: AbstractVec3<Float>? = null
) {
    constructor(
        data: IntArray,
        offset: Int
    ) : this(
        NeoVec3f(
            Float.fromBits(data[offset]),
            Float.fromBits(data[offset + 1]),
            Float.fromBits(data[offset + 2])
        ),
        NeoColor.RGBA(data[offset + 3]),
        NeoVec2f(
            Float.fromBits(data[offset + 4]),
            Float.fromBits(data[offset + 5])
        ),
        NeoVec2i(
            data[offset + 6] ushr 16,
            data[offset + 6] and 0xFFFF
        ),
        null,
        NeoVec3f(
            (data[offset + 7] ushr 24).toByte() / 127f,
            ((data[offset + 7] ushr 16) and 0xFF).toByte() / 127f,
            ((data[offset + 7] ushr 8) and 0xFF).toByte() / 127f
        )
    )

    constructor(
        copy: NeoVertexData,
        pos: AbstractVec3<Float>? = null,
        color: NeoColor? = null,
        textureUV: AbstractVec2<Float>? = null,
        overlayUV: AbstractVec2<Int>? = null,
        lightUV: AbstractVec2<Int>? = null,
        normal: AbstractVec3<Float>? = null
    ) : this(
        pos ?: copy.pos,
        color ?: copy.color,
        textureUV ?: copy.textureUV,
        overlayUV ?: copy.overlayUV,
        lightUV ?: copy.lightUV,
        normal ?: copy.normal,
    )

    fun put(consumer: NeoVertexConsumer) {
        consumer.vertex(pos)
        color?.let(consumer::color)
        textureUV?.let(consumer::textureUV)
        overlayUV?.let(consumer::overlayUV)
        lightUV?.let(consumer::lightUV)
        normal?.let(consumer::normal)
    }

    abstract class Consumer : NeoVertexConsumer() {
        @JvmField
        protected var pos: AbstractVec3<Float>? = null
        @JvmField
        protected var color: NeoColor? = null
        @JvmField
        protected var textureUV: AbstractVec2<Float>? = null
        @JvmField
        protected var overlayUV: AbstractVec2<Int>? = null
        @JvmField
        protected var lightUV: AbstractVec2<Int>? = null
        @JvmField
        protected var normal: AbstractVec3<Float>? = null

        abstract fun take(vertex: NeoVertexData)

        open fun flush() {
            pos?.let {
                take(NeoVertexData(it, color, textureUV, overlayUV, lightUV, normal))
            }
            pos = null
            color = null
            textureUV = null
            overlayUV = null
            lightUV = null
            normal = null
        }

        override fun vertex(
            x: Float,
            y: Float,
            z: Float
        ): NeoVertexConsumer {
            flush()
            pos = NeoVec3f(x, y, z)
            return this
        }

        override fun color(
            r: Int,
            g: Int,
            b: Int,
            a: Int
        ): NeoVertexConsumer {
            color = NeoColor.RGBA(r, g, b, a)
            return this
        }

        override fun textureUV(
            u: Float,
            v: Float
        ): NeoVertexConsumer {
            textureUV = NeoVec2f(u, v)
            return this
        }

        override fun overlayUV(
            u: Int,
            v: Int
        ): NeoVertexConsumer {
            overlayUV = NeoVec2i(u, v)
            return this
        }

        override fun lightUV(
            u: Int,
            v: Int
        ): NeoVertexConsumer {
            lightUV = NeoVec2i(u, v)
            return this
        }

        override fun normal(
            x: Float,
            y: Float,
            z: Float
        ): NeoVertexConsumer {
            normal = NeoVec3f(x, y, z)
            return this
        }
    }
}