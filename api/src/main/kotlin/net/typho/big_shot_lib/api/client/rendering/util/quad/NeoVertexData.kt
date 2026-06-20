package net.typho.big_shot_lib.api.client.rendering.util.quad

import com.mojang.blaze3d.vertex.VertexConsumer
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexConsumer
import net.typho.big_shot_lib.api.math.*
import net.typho.big_shot_lib.api.util.NeoColor
import net.typho.big_shot_lib.api.util.buffer.packInt

data class NeoVertexData(
    @JvmField
    val pos: IVec3<Float>,
    @JvmField
    val color: NeoColor? = null,
    @JvmField
    val textureUV: IVec2<Float>? = null,
    @JvmField
    val overlayUV: IVec2<Int>? = null,
    @JvmField
    val lightUV: IVec2<Int>? = null,
    @JvmField
    val normal: IVec3<Float>? = null
) {
    @JvmOverloads
    constructor(
        data: IntArray,
        offset: Int = 0
    ) : this(
        IVec3(
            Float.fromBits(data[offset]),
            Float.fromBits(data[offset + 1]),
            Float.fromBits(data[offset + 2])
        ),
        NeoColor.RGBA(data[offset + 3]),
        IVec2(
            Float.fromBits(data[offset + 4]),
            Float.fromBits(data[offset + 5])
        ),
        IVec2(
            data[offset + 6] ushr 16,
            data[offset + 6] and 0xFFFF
        ),
        null,
        IVec3(
            (data[offset + 7] ushr 24).toByte() / 127f,
            ((data[offset + 7] ushr 16) and 0xFF).toByte() / 127f,
            ((data[offset + 7] ushr 8) and 0xFF).toByte() / 127f
        )
    )

    constructor(
        copy: NeoVertexData,
        pos: IVec3<Float>? = null,
        color: NeoColor? = null,
        textureUV: IVec2<Float>? = null,
        overlayUV: IVec2<Int>? = null,
        lightUV: IVec2<Int>? = null,
        normal: IVec3<Float>? = null
    ) : this(
        pos ?: copy.pos,
        color ?: copy.color,
        textureUV ?: copy.textureUV,
        overlayUV ?: copy.overlayUV,
        lightUV ?: copy.lightUV,
        normal ?: copy.normal,
    )

    @OptIn(ExperimentalUnsignedTypes::class)
    @JvmOverloads
    fun packToInts(array: IntArray = IntArray(8), offset: Int = 0): IntArray {
        array[offset] = pos.x.toRawBits()
        array[offset + 1] = pos.y.toRawBits()
        array[offset + 2] = pos.z.toRawBits()
        color?.let {
            array[offset + 3] = it.toPackedRGBA()
        }
        textureUV?.let {
            array[offset + 4] = it.x.toRawBits()
            array[offset + 5] = it.y.toRawBits()
        }
        overlayUV?.let {
            array[offset + 6] = packInt(it.x.toShort(), it.y.toShort())
        }
        normal?.let {
            array[offset + 7] = packInt(
                (it.x * 127f).toInt().toByte(),
                (it.y * 127f).toInt().toByte(),
                (it.z * 127f).toInt().toByte(),
                0.toByte()
            )
        }
        return array
    }

    fun put(consumer: NeoVertexConsumer) {
        consumer.vertex(pos, color, textureUV, overlayUV, lightUV, normal)
    }

    abstract class Consumer : VertexConsumer {
        @JvmField
        protected var pos: IVec3<Float>? = null
        @JvmField
        protected var color: NeoColor? = null
        @JvmField
        protected var textureUV: IVec2<Float>? = null
        @JvmField
        protected var overlayUV: IVec2<Int>? = null
        @JvmField
        protected var lightUV: IVec2<Int>? = null
        @JvmField
        protected var normal: IVec3<Float>? = null

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
        ): VertexConsumer {
            flush()
            pos = IVec3(x, y, z)
            return this
        }

        override fun color(
            r: Int,
            g: Int,
            b: Int,
            a: Int
        ): VertexConsumer {
            color = NeoColor.RGBA(r, g, b, a)
            return this
        }

        override fun textureUV(
            u: Float,
            v: Float
        ): VertexConsumer {
            textureUV = IVec2(u, v)
            return this
        }

        override fun overlayUV(
            u: Int,
            v: Int
        ): VertexConsumer {
            overlayUV = IVec2(u, v)
            return this
        }

        override fun lightUV(
            u: Int,
            v: Int
        ): VertexConsumer {
            lightUV = IVec2(u, v)
            return this
        }

        override fun normal(
            x: Float,
            y: Float,
            z: Float
        ): VertexConsumer {
            normal = IVec3(x, y, z)
            return this
        }
    }
}