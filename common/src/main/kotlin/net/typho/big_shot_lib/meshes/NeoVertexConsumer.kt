package net.typho.big_shot_lib.meshes

import com.mojang.blaze3d.vertex.VertexConsumer

open class NeoVertexConsumer(val inner: IVertexConsumer) : VertexConsumer {
    override fun addVertex(
        p0: Float,
        p1: Float,
        p2: Float
    ): VertexConsumer {
        inner.vertex(p0, p1, p2)
        return this
    }

    override fun setColor(
        p0: Int,
        p1: Int,
        p2: Int,
        p3: Int
    ): VertexConsumer {
        inner.color(p0, p1, p2, p3)
        return this
    }

    override fun setUv(p0: Float, p1: Float): VertexConsumer {
        inner.textureUV(p0, p1)
        return this
    }

    override fun setUv1(p0: Int, p1: Int): VertexConsumer {
        inner.overlayUV(p0, p1)
        return this
    }

    override fun setUv2(p0: Int, p1: Int): VertexConsumer {
        inner.lightUV(p0, p1)
        return this
    }

    override fun setNormal(
        p0: Float,
        p1: Float,
        p2: Float
    ): VertexConsumer {
        inner.normal(p0, p1, p2)
        return this
    }
}