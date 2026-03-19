package net.typho.big_shot_lib.api.client.rendering.quad

import net.typho.big_shot_lib.api.client.rendering.util.NeoVertexConsumer
import net.typho.big_shot_lib.api.math.vec.*
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

    open class PositionTexture(
        override val pos: AbstractVec3<Float, *>,
        override val textureUV: AbstractVec2<Float, *>
    ) : NeoVertexData {
        override val color: NeoColor?
            get() = null
        override val overlayUV: AbstractVec2<Int, *>?
            get() = null
        override val lightUV: AbstractVec2<Int, *>?
            get() = null
        override val normal: AbstractVec3<Float, *>?
            get() = null
    }

    open class LazyPacked(
        @JvmField
        val data: IntArray,
        @JvmField
        val offset: Int
    ) : NeoVertexData {
        override val pos: AbstractVec3<Float, *> by lazy { NeoVec3f(
            Float.fromBits(data[offset]),
            Float.fromBits(data[offset + 1]),
            Float.fromBits(data[offset + 2])
        ) }
        override val color: NeoColor by lazy { NeoColor.RGBA(data[offset + 3]) }
        override val textureUV: AbstractVec2<Float, *> by lazy { NeoVec2f(
            Float.fromBits(data[offset + 4]),
            Float.fromBits(data[offset + 5])
        ) }
        override val overlayUV: AbstractVec2<Int, *> by lazy {
            val packed = data[offset + 6]
            NeoVec2i(
                packed ushr 16,
                packed and 0xFFFF
            )
        }
        override val lightUV: AbstractVec2<Int, *>? = null
        override val normal: AbstractVec3<Float, *> by lazy {
            val packed = data[offset + 7]
            NeoVec3f(
                (packed ushr 24).toByte() / 127f,
                ((packed ushr 16) and 0xFF).toByte() / 127f,
                ((packed ushr 8) and 0xFF).toByte() / 127f
            )
        }

        override fun put(consumer: NeoVertexConsumer) {
            consumer.vertex(data, offset)
            consumer.color(data, offset + 3)
            consumer.textureUV(data, offset + 4)
            consumer.overlayUV(data, offset + 6)
            consumer.normal(data, offset + 7)
            consumer.endVertex()
        }
    }
}