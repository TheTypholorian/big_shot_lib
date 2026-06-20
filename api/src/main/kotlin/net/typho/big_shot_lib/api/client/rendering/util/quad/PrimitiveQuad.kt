package net.typho.big_shot_lib.api.client.rendering.util.quad

import com.mojang.blaze3d.vertex.VertexConsumer
import net.caffeinemc.mods.sodium.api.util.ColorARGB
import net.typho.big_shot_lib.api.client.rendering.util.PackedNormal

open class PrimitiveQuad(
    @JvmField
    val v0: PrimitiveVertex,
    @JvmField
    val v1: PrimitiveVertex,
    @JvmField
    val v2: PrimitiveVertex,
    @JvmField
    val v3: PrimitiveVertex
) {
    open fun apply(out: (vertex: PrimitiveVertex, index: Int) -> Unit) {
        out(v0, 0)
        out(v1, 1)
        out(v2, 2)
        out(v3, 3)
    }

    open fun apply(out: (vertex: PrimitiveVertex) -> Unit) {
        out(v0)
        out(v1)
        out(v2)
        out(v3)
    }

    open fun any(out: (vertex: PrimitiveVertex, index: Int) -> Boolean): Boolean {
        return out(v0, 0) || out(v1, 1) || out(v2, 2) || out(v3, 3)
    }

    open fun any(out: (vertex: PrimitiveVertex) -> Boolean): Boolean {
        return out(v0) || out(v1) || out(v2) || out(v3)
    }

    open fun copyWithOffset(x: Float, y: Float, z: Float): PrimitiveQuad {
        return PrimitiveQuad(
            PrimitiveVertex(v0, x, y, z),
            PrimitiveVertex(v1, x, y, z),
            PrimitiveVertex(v2, x, y, z),
            PrimitiveVertex(v3, x, y, z)
        )
    }

    open fun copyWithOffset(x: Int, y: Int, z: Int): PrimitiveQuad {
        return copyWithOffset(x.toFloat(), y.toFloat(), z.toFloat())
    }

    open class Consumer(
        @JvmField
        val out: (face: PrimitiveQuad) -> Unit,
        @JvmField
        val offsetX: Float = 0f,
        @JvmField
        val offsetY: Float = 0f,
        @JvmField
        val offsetZ: Float = 0f,
    ) : SimpleVertexConsumer {
        @JvmField
        protected var v0 = PrimitiveVertex()
        @JvmField
        protected var v1 = PrimitiveVertex()
        @JvmField
        protected var v2 = PrimitiveVertex()
        @JvmField
        protected var v3 = PrimitiveVertex()
        @JvmField
        protected var vertex = v0
        @JvmField
        protected var index = 0

        protected open fun create() = PrimitiveQuad(v0, v1, v2, v3)

        fun flush() {
            if (index == 4) {
                index = 0
                out(create())
                v0 = PrimitiveVertex()
                v1 = PrimitiveVertex()
                v2 = PrimitiveVertex()
                v3 = PrimitiveVertex()
            }
        }

        override fun vertex(
            x: Float,
            y: Float,
            z: Float
        ): VertexConsumer {
            flush()
            vertex = when (index) {
                0 -> v0
                1 -> v1
                2 -> v2
                3 -> v3
                else -> throw IndexOutOfBoundsException(index)
            }
            index++
            vertex.x = x + offsetX
            vertex.y = y + offsetY
            vertex.z = z + offsetZ
            return this
        }

        override fun color(
            r: Int,
            g: Int,
            b: Int,
            a: Int
        ): VertexConsumer {
            vertex.color = ColorARGB.pack(r, g, b, a)
            return this
        }

        override fun color(argb: Int): VertexConsumer {
            vertex.color = argb
            return this
        }

        override fun lightUV(
            u: Int,
            v: Int
        ): VertexConsumer {
            vertex.light = (u shl 16) or v
            return this
        }

        override fun lightUV(packed: Int): VertexConsumer {
            vertex.light = packed
            return this
        }

        override fun normal(
            x: Float,
            y: Float,
            z: Float
        ): VertexConsumer {
            vertex.normal = PackedNormal.pack(x, y, z)
            return this
        }

        override fun normal(x: Byte, y: Byte, z: Byte): VertexConsumer {
            vertex.normal = PackedNormal.pack(x.toInt(), y.toInt(), z.toInt())
            return this
        }

        override fun normal(packed: Int): VertexConsumer {
            vertex.normal = packed
            return this
        }

        override fun overlayUV(
            u: Int,
            v: Int
        ): VertexConsumer {
            return this
        }

        override fun textureUV(
            u: Float,
            v: Float
        ): VertexConsumer {
            vertex.u = u
            vertex.v = v
            return this
        }
    }
}