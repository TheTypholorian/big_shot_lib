package net.typho.big_shot_lib.api.client.util.quads

import net.typho.big_shot_lib.api.client.opengl.buffers.NeoVertexConsumer
import net.typho.big_shot_lib.api.util.NeoColor
import org.joml.Vector2fc
import org.joml.Vector2ic
import org.joml.Vector3fc
import java.util.function.UnaryOperator

interface NeoVertexData {
    val pos: Vector3fc
    val color: NeoColor?
    val textureUV: Vector2fc?
    val overlayUV: Vector2ic?
    val lightUV: Vector2ic?
    val normal: Vector3fc?

    fun withPosition(pos: UnaryOperator<Vector3fc>): NeoVertexData {
        val parent = this
        val pos = pos.apply(parent.pos)
        return object : NeoVertexData {
            override val pos: Vector3fc = pos
            override val color: NeoColor? = parent.color
            override val textureUV: Vector2fc? = parent.textureUV
            override val overlayUV: Vector2ic? = parent.overlayUV
            override val lightUV: Vector2ic? = parent.lightUV
            override val normal: Vector3fc? = parent.normal
        }
    }

    fun withColor(color: UnaryOperator<NeoColor?>): NeoVertexData {
        val parent = this
        val color = color.apply(parent.color)
        return object : NeoVertexData {
            override val pos: Vector3fc = parent.pos
            override val color: NeoColor? = color
            override val textureUV: Vector2fc? = parent.textureUV
            override val overlayUV: Vector2ic? = parent.overlayUV
            override val lightUV: Vector2ic? = parent.lightUV
            override val normal: Vector3fc? = parent.normal
        }
    }

    fun withTextureUV(textureUV: UnaryOperator<Vector2fc?>): NeoVertexData {
        val parent = this
        val textureUV = textureUV.apply(parent.textureUV)
        return object : NeoVertexData {
            override val pos: Vector3fc = parent.pos
            override val color: NeoColor? = parent.color
            override val textureUV: Vector2fc? = textureUV
            override val overlayUV: Vector2ic? = parent.overlayUV
            override val lightUV: Vector2ic? = parent.lightUV
            override val normal: Vector3fc? = parent.normal
        }
    }

    fun withOverlayUV(overlayUV: UnaryOperator<Vector2ic?>): NeoVertexData {
        val parent = this
        val overlayUV = overlayUV.apply(parent.overlayUV)
        return object : NeoVertexData {
            override val pos: Vector3fc = parent.pos
            override val color: NeoColor? = parent.color
            override val textureUV: Vector2fc? = parent.textureUV
            override val overlayUV: Vector2ic? = overlayUV
            override val lightUV: Vector2ic? = parent.lightUV
            override val normal: Vector3fc? = parent.normal
        }
    }

    fun withLightUV(lightUV: UnaryOperator<Vector2ic?>): NeoVertexData {
        val parent = this
        val lightUV = lightUV.apply(parent.lightUV)
        return object : NeoVertexData {
            override val pos: Vector3fc = parent.pos
            override val color: NeoColor? = parent.color
            override val textureUV: Vector2fc? = parent.textureUV
            override val overlayUV: Vector2ic? = parent.overlayUV
            override val lightUV: Vector2ic? = lightUV
            override val normal: Vector3fc? = parent.normal
        }
    }

    fun withNormal(normal: UnaryOperator<Vector3fc?>): NeoVertexData {
        val parent = this
        val normal = normal.apply(parent.normal)
        return object : NeoVertexData {
            override val pos: Vector3fc = parent.pos
            override val color: NeoColor? = parent.color
            override val textureUV: Vector2fc? = parent.textureUV
            override val overlayUV: Vector2ic? = parent.overlayUV
            override val lightUV: Vector2ic? = parent.lightUV
            override val normal: Vector3fc? = normal
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