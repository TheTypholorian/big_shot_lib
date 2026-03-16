package net.typho.big_shot_lib.api.util

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import net.typho.big_shot_lib.api.math.vec.*
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
        val CODEC_3I: Codec<NeoColor> = AbstractVec3.INT_CODEC.xmap(
            { vec -> RGB(vec) },
            { color -> color.toVec3i() }
        )
        @JvmField
        val CODEC_4I: Codec<NeoColor> = AbstractVec4.INT_CODEC.xmap(
            { vec -> RGBA(vec) },
            { color -> color.toVec4i() }
        )
        @JvmField
        val CODEC_3F: Codec<NeoColor> = AbstractVec3.FLOAT_CODEC.xmap(
            { vec -> RGBF(vec) },
            { color -> color.toVec3F() }
        )
        @JvmField
        val CODEC_4F: Codec<NeoColor> = AbstractVec4.FLOAT_CODEC.xmap(
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

    val redF: Float
        get() = red / 255f

    val greenF: Float
        get() = green / 255f

    val blueF: Float
        get() = blue / 255f

    val alphaF: Float?
        get() = alpha?.div(255f)

    fun toVec3F(): AbstractVec3<Float, *> = NeoVec3f(redF, greenF, blueF)

    fun toVec4F(): AbstractVec4<Float, *> = NeoVec4f(redF, greenF, blueF, alphaF ?: 1f)

    val red: Int
        get() = (redF * 255).toInt()

    val green: Int
        get() = (greenF * 255).toInt()

    val blue: Int
        get() = (blueF * 255).toInt()

    val alpha: Int?
        get() = alphaF?.times(255)?.toInt()

    fun toVec3i() = NeoVec3i(red, green, blue)

    fun toVec4i() = NeoVec4i(red, green, blue, alpha ?: 255)

    fun toARGB() = ((alpha ?: 255) shl 24) or (red shl 16) or (green shl 8) or blue

    fun toRGBA() = (red shl 24) or (green shl 16) or (blue shl 8) or (alpha ?: 255)

    fun toIntArray() = intArrayOf(red, green, blue, alpha ?: 255)

    fun toFloatArray() = floatArrayOf(redF, greenF, blueF, alphaF ?: 1f)

    @JvmRecord
    data class RGB(
        override val red: Int,
        override val green: Int,
        override val blue: Int
    ) : NeoColor {
        constructor(rgb: Int) : this(rgb ushr 16 and 0xFF, rgb ushr 8 and 0xFF, rgb and 0xFF)

        constructor(color: Color) : this(color.red, color.green, color.blue)

        constructor(color: AbstractVec3<Int, *>) : this(color.r, color.g, color.b)

        constructor(color: IntArray) : this(color[0], color[1], color[2])

        constructor(color: FloatArray) : this((color[0] * 255).toInt(), (color[1] * 255).toInt(), (color[2] * 255).toInt())

        override val alpha: Int?
            get() = null
    }

    @JvmRecord
    data class RGBA(
        override val red: Int,
        override val green: Int,
        override val blue: Int,
        override val alpha: Int
    ) : NeoColor {
        constructor(rgba: Int) : this(rgba ushr 24 and 0xFF, rgba ushr 16 and 0xFF, rgba ushr 8 and 0xFF, rgba and 0xFF)

        constructor(color: Color) : this(color.red, color.green, color.blue, color.alpha)

        constructor(color: AbstractVec4<Int, *>) : this(color.r, color.g, color.b, color.a)

        constructor(color: IntArray) : this(color[0], color[1], color[2], color[3])

        constructor(color: FloatArray) : this((color[0] * 255).toInt(), (color[1] * 255).toInt(), (color[2] * 255).toInt(), (color[3] * 255).toInt())
    }

    @JvmRecord
    data class RGBF(
        override val redF: Float,
        override val greenF: Float,
        override val blueF: Float
    ) : NeoColor {
        constructor(rgb: Int) : this((rgb ushr 16 and 0xFF) / 255f, (rgb ushr 8 and 0xFF) / 255f, (rgb and 0xFF) / 255f)

        constructor(color: Color) : this(color.red / 255f, color.green / 255f, color.blue / 255f)

        constructor(color: AbstractVec3<Float, *>) : this(color.r, color.g, color.b)

        constructor(color: IntArray) : this(color[0] / 255f, color[1] / 255f, color[2] / 255f)

        constructor(color: FloatArray) : this(color[0], color[1], color[2])

        override val alphaF: Float?
            get() = null
    }

    @JvmRecord
    data class RGBAF(
        override val redF: Float,
        override val greenF: Float,
        override val blueF: Float,
        override val alphaF: Float
    ) : NeoColor {
        constructor(rgba: Int) : this((rgba ushr 24 and 0xFF) / 255f, (rgba ushr 16 and 0xFF) / 255f, (rgba ushr 8 and 0xFF) / 255f, (rgba and 0xFF) / 255f)

        constructor(color: Color) : this(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)

        constructor(color: AbstractVec4<Float, *>) : this(color.r, color.g, color.b, color.a)

        constructor(color: IntArray) : this(color[0] / 255f, color[1] / 255f, color[2] / 255f, color[3] / 255f)

        constructor(color: FloatArray) : this(color[0], color[1], color[2], color[3])
    }
}