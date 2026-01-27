package net.typho.big_shot_lib.meshes

import com.mojang.blaze3d.vertex.VertexConsumer

object EmptyVertexConsumer : VertexConsumer, IVertexConsumer {
    override fun addVertex(
        p0: Float,
        p1: Float,
        p2: Float
    ): VertexConsumer {
        return this
    }

    override fun setColor(
        p0: Int,
        p1: Int,
        p2: Int,
        p3: Int
    ): VertexConsumer {
        return this
    }

    override fun setUv(p0: Float, p1: Float): VertexConsumer {
        return this
    }

    override fun setUv1(p0: Int, p1: Int): VertexConsumer {
        return this
    }

    override fun setUv2(p0: Int, p1: Int): VertexConsumer {
        return this
    }

    override fun setNormal(
        p0: Float,
        p1: Float,
        p2: Float
    ): VertexConsumer {
        return this
    }

    override fun vertex(
        x: Float,
        y: Float,
        z: Float
    ): IVertexConsumer {
        return this
    }

    override fun color(
        r: Float,
        g: Float,
        b: Float,
        a: Float
    ): IVertexConsumer {
        return this
    }

    override fun textureUV(u: Float, v: Float): IVertexConsumer {
        return this
    }

    override fun overlayUV(u: Int, v: Int): IVertexConsumer {
        return this
    }

    override fun lightUV(u: Int, v: Int): IVertexConsumer {
        return this
    }

    override fun normal(
        x: Float,
        y: Float,
        z: Float
    ): IVertexConsumer {
        return this
    }
}