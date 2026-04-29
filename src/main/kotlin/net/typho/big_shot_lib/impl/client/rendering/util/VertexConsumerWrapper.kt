package net.typho.big_shot_lib.impl.client.rendering.util

import com.mojang.blaze3d.vertex.VertexConsumer
import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexConsumer
import net.typho.big_shot_lib.api.util.NeoColor
import com.mojang.blaze3d.vertex.PoseStack
import org.joml.Matrix4f
import org.joml.Matrix4fc

class VertexConsumerWrapper(
    @JvmField
    val inner: NeoVertexConsumer
) : VertexConsumer {
    //? if >=1.21.11 {
    /*override fun setLineWidth(f: Float): VertexConsumer {
        // TODO implement once all support pre-1.21.11 is dropped
        return this
    }
    *///? }

    //? if >=1.21 {
    /*override fun addVertex(
        f: Float,
        g: Float,
        h: Float
    ): VertexConsumer {
        inner.vertex(f, g, h)
        return this
    }

    override fun setColor(
        i: Int,
        j: Int,
        k: Int,
        l: Int
    ): VertexConsumer {
        inner.color(i, j, k, l)
        return this
    }

    override fun setUv(f: Float, g: Float): VertexConsumer {
        inner.textureUV(f, g)
        return this
    }

    override fun setUv1(i: Int, j: Int): VertexConsumer {
        inner.overlayUV(i, j)
        return this
    }

    override fun setUv2(i: Int, j: Int): VertexConsumer {
        inner.lightUV(i, j)
        return this
    }

    override fun setNormal(
        f: Float,
        g: Float,
        h: Float
    ): VertexConsumer {
        inner.normal(f, g, h)
        return this
    }

    override fun addVertex(
        pose: PoseStack.Pose,
        f: Float,
        g: Float,
        h: Float
    ): VertexConsumer {
        inner.vertex(pose, f, g, h)
        return this
    }

    override fun addVertex(
        //? if <1.21.11 {
        matrix4f: Matrix4f,
        //? } else {
        /*matrix4f: Matrix4fc,
        *///? }
        f: Float,
        g: Float,
        h: Float
    ): VertexConsumer {
        inner.vertex(matrix4f, f, g, h)
        return this
    }

    override fun setColor(
        f: Float,
        g: Float,
        h: Float,
        i: Float
    ): VertexConsumer {
        inner.color(f, g, h, i)
        return this
    }

    override fun setColor(i: Int): VertexConsumer {
        inner.color(i)
        return this
    }

    override fun setNormal(
        pose: PoseStack.Pose,
        f: Float,
        g: Float,
        h: Float
    ): VertexConsumer {
        inner.normal(pose, f, g, h)
        return this
    }

    override fun setLight(i: Int): VertexConsumer {
        inner.lightUV(i)
        return this
    }

    override fun setOverlay(i: Int): VertexConsumer {
        inner.overlayUV(i)
        return this
    }
    *///? } else {
    var defaultColor: NeoColor? = null

    override fun vertex(
        d: Double,
        e: Double,
        f: Double
    ): VertexConsumer {
        inner.vertex(d.toFloat(), e.toFloat(), f.toFloat())
        defaultColor?.let { inner.color(it) }
        return this
    }

    override fun color(
        i: Int,
        j: Int,
        k: Int,
        l: Int
    ): VertexConsumer {
        inner.color(i, j, k, l)
        return this
    }

    override fun uv(f: Float, g: Float): VertexConsumer {
        inner.textureUV(f, g)
        return this
    }

    override fun overlayCoords(i: Int, j: Int): VertexConsumer {
        inner.overlayUV(i, j)
        return this
    }

    override fun uv2(i: Int, j: Int): VertexConsumer {
        inner.lightUV(i, j)
        return this
    }

    override fun normal(
        f: Float,
        g: Float,
        h: Float
    ): VertexConsumer {
        inner.normal(f, g, h)
        return this
    }

    override fun endVertex() {
        inner.endVertex()
    }

    override fun defaultColor(i: Int, j: Int, k: Int, l: Int) {
        defaultColor = NeoColor.RGBA(i, j, k, l)
    }

    override fun unsetDefaultColor() {
        defaultColor = null
    }
    //? }
}