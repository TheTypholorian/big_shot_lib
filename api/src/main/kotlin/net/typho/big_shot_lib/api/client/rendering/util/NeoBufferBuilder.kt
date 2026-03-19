package net.typho.big_shot_lib.api.client.rendering.util

import com.mojang.blaze3d.vertex.PoseStack

class NeoBufferBuilder : NeoVertexConsumer() {
    override fun vertex(
        x: Float,
        y: Float,
        z: Float
    ): NeoVertexConsumer {
        TODO("Not yet implemented")
    }

    override fun color(
        r: Int,
        g: Int,
        b: Int,
        a: Int
    ): NeoVertexConsumer {
        TODO("Not yet implemented")
    }

    override fun textureUV(
        u: Float,
        v: Float
    ): NeoVertexConsumer {
        TODO("Not yet implemented")
    }

    override fun overlayUV(
        u: Int,
        v: Int
    ): NeoVertexConsumer {
        TODO("Not yet implemented")
    }

    override fun lightUV(
        u: Int,
        v: Int
    ): NeoVertexConsumer {
        TODO("Not yet implemented")
    }

    override fun normal(
        x: Float,
        y: Float,
        z: Float
    ): NeoVertexConsumer {
        TODO("Not yet implemented")
    }

    override fun normal(
        pose: PoseStack.Pose,
        x: Float,
        y: Float,
        z: Float
    ): NeoVertexConsumer {
        TODO("Not yet implemented")
    }
}