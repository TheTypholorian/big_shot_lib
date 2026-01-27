package net.typho.big_shot_lib.meshes

import com.mojang.blaze3d.vertex.VertexConsumer

class BuiltinVertexConsumer(val inner: VertexConsumer) : IVertexConsumer {
    override fun vertex(
        x: Float,
        y: Float,
        z: Float
    ): IVertexConsumer {
        inner.addVertex(x, y, z)
        return this
    }

    override fun color(
        r: Float,
        g: Float,
        b: Float,
        a: Float
    ): IVertexConsumer {
        inner.setColor(r, g, b, a)
        return this
    }

    override fun textureUV(u: Float, v: Float): IVertexConsumer {
        inner.setUv(u, v)
        return this
    }

    override fun overlayUV(u: Int, v: Int): IVertexConsumer {
        inner.setUv1(u, v)
        return this
    }

    override fun lightUV(u: Int, v: Int): IVertexConsumer {
        inner.setUv2(u, v)
        return this
    }

    override fun normal(
        x: Float,
        y: Float,
        z: Float
    ): IVertexConsumer {
        inner.setNormal(x, y, z)
        return this
    }
}