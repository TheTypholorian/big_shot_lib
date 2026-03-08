package net.typho.big_shot_lib.api.client.opengl.buffers

import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.MeshData

open class NeoBufferBuilder(
    @JvmField
    val inner: BufferBuilder
) : NeoVertexConsumer {
    override fun vertex(
        x: Float,
        y: Float,
        z: Float
    ): NeoVertexConsumer {
        inner.addVertex(x, y, z)
        return this
    }

    override fun color(
        r: Int,
        g: Int,
        b: Int,
        a: Int
    ): NeoVertexConsumer {
        inner.setColor(r, g, b, a)
        return this
    }

    override fun textureUV(
        u: Float,
        v: Float
    ): NeoVertexConsumer {
        inner.setUv(u, v)
        return this
    }

    override fun overlayUV(
        u: Int,
        v: Int
    ): NeoVertexConsumer {
        inner.setUv1(u, v)
        return this
    }

    override fun lightUV(
        u: Int,
        v: Int
    ): NeoVertexConsumer {
        inner.setUv2(u, v)
        return this
    }

    override fun normal(
        x: Float,
        y: Float,
        z: Float
    ): NeoVertexConsumer {
        inner.setNormal(x, y, z)
        return this
    }

    fun build(): MeshData? = inner.build()

    fun buildOrThrow(): MeshData = inner.buildOrThrow()
}