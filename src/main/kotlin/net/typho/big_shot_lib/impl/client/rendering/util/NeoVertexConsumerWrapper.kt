package net.typho.big_shot_lib.impl.client.rendering.util

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexConsumer

class NeoVertexConsumerWrapper(
    @JvmField
    val inner: VertexConsumer
) : NeoVertexConsumer() {
    override fun vertex(
        x: Float,
        y: Float,
        z: Float
    ): NeoVertexConsumer {
        //? if >=1.21 {

        /*inner.addVertex(x, y, z)
        *///? } else {
        inner.vertex(x.toDouble(), y.toDouble(), z.toDouble())
        //? }
        return this
    }

    override fun color(
        r: Int,
        g: Int,
        b: Int,
        a: Int
    ): NeoVertexConsumer {
        //? if >=1.21 {
        /*inner.setColor(r, g, b, a)
        *///? } else {
        inner.color(r, g, b, a)
        //? }
        return this
    }

    override fun textureUV(
        u: Float,
        v: Float
    ): NeoVertexConsumer {
        //? if >=1.21 {
        /*inner.setUv(u, v)
        *///? } else {
        inner.uv(u, v)
        //? }
        return this
    }

    override fun overlayUV(
        u: Int,
        v: Int
    ): NeoVertexConsumer {
        //? if >=1.21 {
        /*inner.setUv1(u, v)
        *///? } else {
        inner.overlayCoords(u, v)
        //? }
        return this
    }

    override fun lightUV(
        u: Int,
        v: Int
    ): NeoVertexConsumer {
        //? if >=1.21 {
        /*inner.setUv2(u, v)
        *///? } else {
        inner.uv2(u, v)
        //? }
        return this
    }

    override fun normal(
        x: Float,
        y: Float,
        z: Float
    ): NeoVertexConsumer {
        //? if >=1.21 {
        /*inner.setNormal(x, y, z)
        *///? } else {
        inner.normal(x, y, z)
        //? }
        return this
    }

    override fun normal(
        pose: PoseStack.Pose,
        x: Float,
        y: Float,
        z: Float
    ): NeoVertexConsumer {
        //? if >=1.21 {
        /*inner.setNormal(pose, x, y, z)
        *///? } else if >=1.20.5 {
        /*inner.normal(pose, x, y, z)
        *///? } else {
        inner.normal(pose.normal(), x, y, z)
        //? }
        return this
    }

    //? if <1.21 {
    override fun _endVertex() {
        inner.endVertex()
    }
    //? }
}