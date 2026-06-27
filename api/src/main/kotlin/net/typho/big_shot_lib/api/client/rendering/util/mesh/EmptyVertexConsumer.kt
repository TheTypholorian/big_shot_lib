package net.typho.big_shot_lib.api.client.rendering.util.mesh

import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.blaze3d.vertex.VertexFormat
import org.lwjgl.system.MemoryStack

object EmptyVertexConsumer : SimpleVertexConsumer {
    override fun vertex(x: Float, y: Float, z: Float): VertexConsumer {
        return this
    }

    override fun color(
        r: Int,
        g: Int,
        b: Int,
        a: Int
    ): VertexConsumer {
        return this
    }

    override fun color(color: Int): VertexConsumer {
        return this
    }

    override fun textureUV(u: Float, v: Float): VertexConsumer {
        return this
    }

    override fun overlayUV(u: Int, v: Int): VertexConsumer {
        return this
    }

    override fun lightUV(u: Int, v: Int): VertexConsumer {
        return this
    }

    override fun normal(x: Float, y: Float, z: Float): VertexConsumer {
        return this
    }

    override fun push(
        stack: MemoryStack,
        ptr: Long,
        count: Int,
        format: VertexFormat
    ) {
    }
}