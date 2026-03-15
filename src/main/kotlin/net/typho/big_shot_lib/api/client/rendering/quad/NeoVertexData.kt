package net.typho.big_shot_lib.api.client.rendering.quad

import net.typho.big_shot_lib.api.client.rendering.NeoVertexConsumer
import net.typho.big_shot_lib.api.math.vec.AbstractVec2
import net.typho.big_shot_lib.api.math.vec.AbstractVec3
import net.typho.big_shot_lib.api.util.NeoColor
import java.util.function.UnaryOperator

interface NeoVertexData {
    val pos: AbstractVec3<Float, *>
    val color: NeoColor?
    val textureUV: AbstractVec2<Float, *>?
    val overlayUV: AbstractVec2<Int, *>?
    val lightUV: AbstractVec2<Int, *>?
    val normal: AbstractVec3<Float, *>?

    fun withPosition(pos: UnaryOperator<AbstractVec3<Float, *>>): NeoVertexData {
        val parent = this
        val pos = pos.apply(parent.pos)
        return object : NeoVertexData {
            override val pos: AbstractVec3<Float, *> = pos
            override val color: NeoColor? = parent.color
            override val textureUV: AbstractVec2<Float, *>? = parent.textureUV
            override val overlayUV: AbstractVec2<Int, *>? = parent.overlayUV
            override val lightUV: AbstractVec2<Int, *>? = parent.lightUV
            override val normal: AbstractVec3<Float, *>? = parent.normal
        }
    }

    fun withColor(color: UnaryOperator<NeoColor?>): NeoVertexData {
        val parent = this
        val color = color.apply(parent.color)
        return object : NeoVertexData {
            override val pos: AbstractVec3<Float, *> = parent.pos
            override val color: NeoColor? = color
            override val textureUV: AbstractVec2<Float, *>? = parent.textureUV
            override val overlayUV: AbstractVec2<Int, *>? = parent.overlayUV
            override val lightUV: AbstractVec2<Int, *>? = parent.lightUV
            override val normal: AbstractVec3<Float, *>? = parent.normal
        }
    }

    fun withTextureUV(textureUV: UnaryOperator<AbstractVec2<Float, *>?>): NeoVertexData {
        val parent = this
        val textureUV = textureUV.apply(parent.textureUV)
        return object : NeoVertexData {
            override val pos: AbstractVec3<Float, *> = parent.pos
            override val color: NeoColor? = parent.color
            override val textureUV: AbstractVec2<Float, *>? = textureUV
            override val overlayUV: AbstractVec2<Int, *>? = parent.overlayUV
            override val lightUV: AbstractVec2<Int, *>? = parent.lightUV
            override val normal: AbstractVec3<Float, *>? = parent.normal
        }
    }

    fun withOverlayUV(overlayUV: UnaryOperator<AbstractVec2<Int, *>?>): NeoVertexData {
        val parent = this
        val overlayUV = overlayUV.apply(parent.overlayUV)
        return object : NeoVertexData {
            override val pos: AbstractVec3<Float, *> = parent.pos
            override val color: NeoColor? = parent.color
            override val textureUV: AbstractVec2<Float, *>? = parent.textureUV
            override val overlayUV: AbstractVec2<Int, *>? = overlayUV
            override val lightUV: AbstractVec2<Int, *>? = parent.lightUV
            override val normal: AbstractVec3<Float, *>? = parent.normal
        }
    }

    fun withLightUV(lightUV: UnaryOperator<AbstractVec2<Int, *>?>): NeoVertexData {
        val parent = this
        val lightUV = lightUV.apply(parent.lightUV)
        return object : NeoVertexData {
            override val pos: AbstractVec3<Float, *> = parent.pos
            override val color: NeoColor? = parent.color
            override val textureUV: AbstractVec2<Float, *>? = parent.textureUV
            override val overlayUV: AbstractVec2<Int, *>? = parent.overlayUV
            override val lightUV: AbstractVec2<Int, *>? = lightUV
            override val normal: AbstractVec3<Float, *>? = parent.normal
        }
    }

    fun withNormal(normal: UnaryOperator<AbstractVec3<Float, *>?>): NeoVertexData {
        val parent = this
        val normal = normal.apply(parent.normal)
        return object : NeoVertexData {
            override val pos: AbstractVec3<Float, *> = parent.pos
            override val color: NeoColor? = parent.color
            override val textureUV: AbstractVec2<Float, *>? = parent.textureUV
            override val overlayUV: AbstractVec2<Int, *>? = parent.overlayUV
            override val lightUV: AbstractVec2<Int, *>? = parent.lightUV
            override val normal: AbstractVec3<Float, *>? = normal
        }
    }

    fun put(consumer: NeoVertexConsumer) {
        consumer.vertex(pos)
        color?.let(consumer::color)
        textureUV?.let(consumer::textureUV)
        overlayUV?.let(consumer::overlayUV)
        lightUV?.let(consumer::lightUV)
        normal?.let(consumer::normal)
    }
}