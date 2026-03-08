package net.typho.big_shot_lib.api.util

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import net.typho.big_shot_lib.api.util.resources.NeoCodecs
import org.joml.*
import java.awt.Color

interface NeoColor {
    companion object {
        @JvmField
        val FULL_ON = RGBAF(1f, 1f, 1f, 1f)
        @JvmField
        val FULL_OFF = RGBAF(0f, 0f, 0f, 0f)

        @JvmField
        val WHITE = RGBA(Color.WHITE)
        @JvmField
        val LIGHT_GRAY = RGBA(Color.LIGHT_GRAY)
        @JvmField
        val GRAY = RGBA(Color.GRAY)
        @JvmField
        val DARK_GRAY = RGBA(Color.DARK_GRAY)
        @JvmField
        val BLACK = RGBA(Color.BLACK)
        @JvmField
        val RED = RGBA(Color.RED)
        @JvmField
        val PINK = RGBA(Color.PINK)
        @JvmField
        val ORANGE = RGBA(Color.ORANGE)
        @JvmField
        val YELLOW = RGBA(Color.YELLOW)
        @JvmField
        val GREEN = RGBA(Color.GREEN)
        @JvmField
        val MAGENTA = RGBA(Color.MAGENTA)
        @JvmField
        val CYAN = RGBA(Color.CYAN)
        @JvmField
        val BLUE = RGBA(Color.BLUE)

        @JvmField
        val CODEC_PACKED: Codec<NeoColor> = Codec.INT.xmap(
            { vec -> RGBA(vec) },
            { color -> color.toRGBA() }
        )
        @JvmField
        val CODEC_3I: Codec<NeoColor> = NeoCodecs.VEC3I.xmap(
            { vec -> RGB(vec) },
            { color -> color.toVec3i() }
        )
        @JvmField
        val CODEC_4I: Codec<NeoColor> = NeoCodecs.VEC4I.xmap(
            { vec -> RGBA(vec) },
            { color -> color.toVec4i() }
        )
        @JvmField
        val CODEC_3F: Codec<NeoColor> = NeoCodecs.VEC3F.xmap(
            { vec -> RGBF(vec) },
            { color -> color.toVec3F() }
        )
        @JvmField
        val CODEC_4F: Codec<NeoColor> = NeoCodecs.VEC4F.xmap(
            { vec -> RGBAF(vec) },
            { color -> color.toVec4F() }
        )
        @JvmField
        val CODEC_ANY: Codec<NeoColor> = Codec.either(
            CODEC_PACKED,
            Codec.either(
                CODEC_3I,
                Codec.either(
                    CODEC_4I,
                    Codec.either(
                        CODEC_3F,
                        CODEC_4F
                    )
                )
            )
        ).xmap(
            { either -> either.map({ l -> l }, { r -> r.map({ l -> l }, { r1 -> r1.map({ l -> l }, { r2 -> r2.map({ l -> l }, { r3 -> r3 }) }) }) }) },
            { color -> Either.left(color) }
        )
    }

    fun redF(): Float = red() / 255f

    fun greenF(): Float = green() / 255f

    fun blueF(): Float = blue() / 255f

    fun alphaF(): Float? = alpha()?.div(255f)

    fun toVec3F() = Vector3f(redF(), greenF(), blueF())

    fun toVec4F() = Vector4f(redF(), greenF(), blueF(), alphaF() ?: 1f)

    fun red(): Int = (redF() * 255).toInt()

    fun green(): Int = (greenF() * 255).toInt()

    fun blue(): Int = (blueF() * 255).toInt()

    fun alpha(): Int? = alphaF()?.times(255)?.toInt()

    fun toVec3i() = Vector3i(red(), green(), blue())

    fun toVec4i() = Vector4i(red(), green(), blue(), alpha() ?: 255)

    fun toARGB() = ((alpha() ?: 255) shl 24) or (red() shl 16) or (green() shl 8) or blue()

    fun toRGBA() = (red() shl 24) or (green() shl 16) or (blue() shl 8) or (alpha() ?: 255)

    fun toIntArray() = intArrayOf(red(), green(), blue(), alpha() ?: 255)

    fun toFloatArray() = floatArrayOf(redF(), greenF(), blueF(), alphaF() ?: 1f)

    @JvmRecord
    data class RGB(
        @JvmField
        val red: Int,
        @JvmField
        val green: Int,
        @JvmField
        val blue: Int
    ) : NeoColor {
        constructor(rgb: Int) : this(rgb ushr 16 and 0xFF, rgb ushr 8 and 0xFF, rgb and 0xFF)

        constructor(color: Color) : this(color.red, color.green, color.blue)

        constructor(color: Vector3ic) : this(color.x(), color.y(), color.z())

        constructor(color: Vector3fc) : this((color.x() * 255).toInt(), (color.y() * 255).toInt(), (color.z() * 255).toInt())

        constructor(color: IntArray) : this(color[0], color[1], color[2])

        constructor(color: FloatArray) : this((color[0] * 255).toInt(), (color[1] * 255).toInt(), (color[2] * 255).toInt())

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
    ) : NeoColor {
        constructor(argb: Int) : this(argb ushr 16 and 0xFF, argb ushr 8 and 0xFF, argb and 0xFF, argb ushr 24)

        constructor(color: Color) : this(color.red, color.green, color.blue, color.alpha)

        constructor(color: Vector4ic) : this(color.x(), color.y(), color.z(), color.w())

        constructor(color: Vector4fc) : this((color.x() * 255).toInt(), (color.y() * 255).toInt(), (color.z() * 255).toInt(), (color.w() * 255).toInt())

        constructor(color: IntArray) : this(color[0], color[1], color[2], color[3])

        constructor(color: FloatArray) : this((color[0] * 255).toInt(), (color[1] * 255).toInt(), (color[2] * 255).toInt(), (color[3] * 255).toInt())

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
    ) : NeoColor {
        constructor(rgb: Int) : this((rgb ushr 16 and 0xFF) / 255f, (rgb ushr 8 and 0xFF) / 255f, (rgb and 0xFF) / 255f)

        constructor(color: Color) : this(color.red / 255f, color.green / 255f, color.blue / 255f)

        constructor(color: Vector3ic) : this(color.x() / 255f, color.y() / 255f, color.z() / 255f)

        constructor(color: Vector3fc) : this(color.x(), color.y(), color.z())

        constructor(color: IntArray) : this(color[0] / 255f, color[1] / 255f, color[2] / 255f)

        constructor(color: FloatArray) : this(color[0], color[1], color[2])

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
    ) : NeoColor {
        constructor(argb: Int) : this((argb ushr 16 and 0xFF) / 255f, (argb ushr 8 and 0xFF) / 255f, (argb and 0xFF) / 255f, (argb ushr 24) / 255f)

        constructor(color: Color) : this(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)

        constructor(color: Vector4ic) : this(color.x() / 255f, color.y() / 255f, color.z() / 255f, color.w() / 255f)

        constructor(color: Vector4fc) : this(color.x(), color.y(), color.z(), color.w())

        constructor(color: IntArray) : this(color[0] / 255f, color[1] / 255f, color[2] / 255f, color[3] / 255f)

        constructor(color: FloatArray) : this(color[0], color[1], color[2], color[3])

        override fun redF() = red

        override fun greenF() = green

        override fun blueF() = blue

        override fun alphaF() = alpha
    }
}