package net.typho.big_shot_lib.api.util

import com.mojang.serialization.Codec
import org.joml.*
import java.awt.Color

interface IColor {
    companion object {
        @JvmField
        val BLACK = from(Color.BLACK)

        @JvmField
        val CODEC_PACKED: Codec<IColor> = Codec.INT.xmap(
            { vec -> from(vec) },
            { color -> color.toRGBA() }
        )
        @JvmField
        val CODEC_3I: Codec<IColor> = VectorCodecs.VEC3I.xmap(
            { vec -> from(vec) },
            { color -> color.toVec3() }
        )
        @JvmField
        val CODEC_4I: Codec<IColor> = VectorCodecs.VEC4I.xmap(
            { vec -> from(vec) },
            { color -> color.toVec4() }
        )
        @JvmField
        val CODEC_3F: Codec<IColor> = VectorCodecs.VEC3F.xmap(
            { vec -> from(vec) },
            { color -> color.toVec3F() }
        )
        @JvmField
        val CODEC_4F: Codec<IColor> = VectorCodecs.VEC4F.xmap(
            { vec -> from(vec) },
            { color -> color.toVec4F() }
        )

        fun from(argb: Int) = RGBA(argb shl 16 and 0xFF, argb shl 8 and 0xFF, argb and 0xFF, argb ushr 24)

        fun from(color: Color) = RGBA(color.red, color.green, color.blue, color.alpha)

        fun from(color: Vector3ic) = RGB(color.x(), color.y(), color.z())

        fun from(color: Vector4ic) = RGBA(color.x(), color.y(), color.z(), color.w())

        fun from(color: Vector3fc) = RGBF(color.x(), color.y(), color.z())

        fun from(color: Vector4fc) = RGBAF(color.x(), color.y(), color.z(), color.w())
    }

    fun redF(): Float = red() / 255f

    fun greenF(): Float = green() / 255f

    fun blueF(): Float = blue() / 255f

    fun alphaF(): Float? = alphaF()?.div(255f)

    fun toVec3F() = Vector3f(redF(), greenF(), blueF())

    fun toVec4F() = Vector4f(redF(), greenF(), blueF(), alphaF() ?: 1f)

    fun red(): Int = (redF() * 255).toInt()

    fun green(): Int = (greenF() * 255).toInt()

    fun blue(): Int = (blueF() * 255).toInt()

    fun alpha(): Int? = alphaF()?.times(255)?.toInt()

    fun toVec3() = Vector3i(red(), green(), blue())

    fun toVec4() = Vector4i(red(), green(), blue(), alpha() ?: 255)

    fun toRGBA() = ((alpha() ?: 0xFF) shl 24) or (red() shl 16) or (green() shl 8) or blue()

    @JvmRecord
    data class RGB(
        @JvmField
        val red: Int,
        @JvmField
        val green: Int,
        @JvmField
        val blue: Int
    ) : IColor {
        override fun red() = red

        override fun green() = green

        override fun blue() = blue

        override fun alpha() = null
    }

    @JvmRecord
    data class RGBA(
        @JvmField
        val red: Int,
        @JvmField
        val green: Int,
        @JvmField
        val blue: Int,
        @JvmField
        val alpha: Int
    ) : IColor {
        override fun red() = red

        override fun green() = green

        override fun blue() = blue

        override fun alpha() = alpha
    }

    @JvmRecord
    data class RGBF(
        @JvmField
        val red: Float,
        @JvmField
        val green: Float,
        @JvmField
        val blue: Float
    ) : IColor {
        override fun redF() = red

        override fun greenF() = green

        override fun blueF() = blue

        override fun alphaF() = null
    }

    @JvmRecord
    data class RGBAF(
        @JvmField
        val red: Float,
        @JvmField
        val green: Float,
        @JvmField
        val blue: Float,
        @JvmField
        val alpha: Float
    ) : IColor {
        override fun redF() = red

        override fun greenF() = green

        override fun blueF() = blue

        override fun alphaF() = alpha
    }
}