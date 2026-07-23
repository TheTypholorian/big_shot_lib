package net.typho.big_shot_lib.api.util

import com.mojang.serialization.Codec
import net.typho.big_shot_lib.api.math.IVec3
import net.typho.big_shot_lib.api.math.IVec4
import net.typho.big_shot_lib.api.util.resource.CodecUtil
import org.joml.Vector3f
import org.joml.Vector3i
import org.joml.Vector4f
import org.joml.Vector4i
import java.awt.Color

@Suppress("UNUSED")
sealed interface NeoColor {
    companion object {
        @JvmField
        val FULL_ON = Expanded(1f, 1f, 1f, 1f)
        @JvmField
        val FULL_OFF = Expanded(0f, 0f, 0f, 0f)

        @JvmField
        val WHITE = Expanded(Color.WHITE)
        @JvmField
        val LIGHT_GRAY = Expanded(Color.LIGHT_GRAY)
        @JvmField
        val GRAY = Expanded(Color.GRAY)
        @JvmField
        val DARK_GRAY = Expanded(Color.DARK_GRAY)
        @JvmField
        val BLACK = Expanded(Color.BLACK)
        @JvmField
        val RED = Expanded(Color.RED)
        @JvmField
        val PINK = Expanded(Color.PINK)
        @JvmField
        val ORANGE = Expanded(Color.ORANGE)
        @JvmField
        val YELLOW = Expanded(Color.YELLOW)
        @JvmField
        val GREEN = Expanded(Color.GREEN)
        @JvmField
        val MAGENTA = Expanded(Color.MAGENTA)
        @JvmField
        val CYAN = Expanded(Color.CYAN)
        @JvmField
        val BLUE = Expanded(Color.BLUE)

        @JvmField
        val CODEC_PACKED: Codec<NeoColor> = Codec.INT.xmap(
            { vec -> RGBA(vec) },
            { color -> color.toPackedARGB() }
        )
        @JvmField
        val CODEC_3I: Codec<NeoColor> = IVec3.INT_CODEC.xmap(
            { vec -> RGB(vec) },
            { color -> color.toVec3i() }
        )
        @JvmField
        val CODEC_4I: Codec<NeoColor> = IVec4.INT_CODEC.xmap(
            { vec -> RGBA(vec) },
            { color -> color.toVec4i() }
        )
        @JvmField
        val CODEC_3F: Codec<NeoColor> = IVec3.FLOAT_CODEC.xmap(
            { vec -> RGBF(vec) },
            { color -> color.toVec3F() }
        )
        @JvmField
        val CODEC_4F: Codec<NeoColor> = IVec4.FLOAT_CODEC.xmap(
            { vec -> RGBAF(vec) },
            { color -> color.toVec4F() }
        )
        @JvmField
        val CODEC_ANY: Codec<out NeoColor> = CodecUtil.any(
            CODEC_4I,
            CODEC_3I,
            CODEC_PACKED
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

    val red: Int
        get() = (redF * 255).toInt()
    val green: Int
        get() = (greenF * 255).toInt()
    val blue: Int
        get() = (blueF * 255).toInt()
    val alpha: Int?
        get() = alphaF?.times(255)?.toInt()

    fun toVec3F(): IVec3<Float> = IVec3(redF, greenF, blueF)

    fun toVec4F(): IVec4<Float> = IVec4(redF, greenF, blueF, alphaF ?: 1f)

    fun toVec3i(): IVec3<Int> = IVec3(red, green, blue)

    fun toVec4i(): IVec4<Int> = IVec4(red, green, blue, alpha ?: 255)

    fun toPackedARGB() = ((alpha ?: 255) shl 24) or (red shl 16) or (green shl 8) or blue

    fun toPackedABGR() = ((alpha ?: 255) shl 24) or (blue shl 16) or (green shl 8) or red

    fun toPackedRGBA() = (red shl 24) or (green shl 16) or (blue shl 8) or (alpha ?: 255)

    fun toPackedBGRA() = (blue shl 24) or (green shl 16) or (red shl 8) or (alpha ?: 255)

    fun toPackedRGB() = (red shl 16) or (green shl 8) or blue

    fun toPackedBGR() = (blue shl 16) or (green shl 8) or red

    fun toBytesARGB() = byteArrayOf((alpha ?: 255).toByte(), red.toByte(), green.toByte(), blue.toByte())

    fun toBytesABGR() = byteArrayOf((alpha ?: 255).toByte(), blue.toByte(), green.toByte(), red.toByte())

    fun toBytesRGBA() = byteArrayOf(red.toByte(), green.toByte(), blue.toByte(), (alpha ?: 255).toByte())

    fun toBytesBGRA() = byteArrayOf(blue.toByte(), green.toByte(), red.toByte(), (alpha ?: 255).toByte())

    fun toBytesRGB() = byteArrayOf(red.toByte(), green.toByte(), blue.toByte())

    fun toBytesBGR() = byteArrayOf(blue.toByte(), green.toByte(), red.toByte())

    fun toFloatsARGB() = floatArrayOf(alphaF ?: 1f, redF, greenF, blueF)

    fun toFloatsABGR() = floatArrayOf(alphaF ?: 1f, blueF, greenF, redF)

    fun toFloatsRGBA() = floatArrayOf(redF, greenF, blueF, alphaF ?: 1f)

    fun toFloatsBGRA() = floatArrayOf(blueF, greenF, redF, alphaF ?: 1f)

    fun toFloatsRGB() = floatArrayOf(redF, greenF, blueF)

    fun toFloatsBGR() = floatArrayOf(blueF, greenF, redF)

    fun toJava() = alpha?.let { Color(red, green, blue, it) } ?: Color(red, green, blue)

    @JvmRecord
    data class Expanded @JvmOverloads constructor(
        override val redF: Float,
        override val greenF: Float,
        override val blueF: Float,
        override val alphaF: Float?,
        override val red: Int,
        override val green: Int,
        override val blue: Int,
        override val alpha: Int?,
        private val java: Color = alpha?.let { Color(red, green, blue, it) } ?: Color(red, green, blue)
    ) : NeoColor {
        @JvmOverloads constructor(red: Int, green: Int, blue: Int, alpha: Int? = null) : this(red / 255f, green / 255f, blue / 255f, alpha?.div(255f), red, green, blue, alpha)

        @JvmOverloads constructor(red: Float, green: Float, blue: Float, alpha: Float? = null) : this(red, green, blue, alpha, (red * 255).toInt(), (green * 255).toInt(), (blue * 255).toInt(), (alpha?.times(255)?.toInt() ?: 255))

        constructor(color: Color) : this(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f, color.red, color.green, color.blue, color.alpha, color)

        constructor(color: Vector3i) : this(color.x, color.y, color.z, null)

        constructor(color: Vector3f) : this(color.x, color.y, color.z, null)

        constructor(color: IVec3<Float>) : this(color.r, color.g, color.b, null)

        constructor(color: Vector4i) : this(color.x, color.y, color.z, color.w)

        constructor(color: Vector4f) : this(color.x, color.y, color.z, color.w)

        constructor(color: IVec4<Float>) : this(color.r, color.g, color.b, color.a)

        constructor(color: IntArray) : this(color[0], color[1], color[2], color.getOrNull(3))

        constructor(color: FloatArray) : this(color[0], color[1], color[2], color.getOrNull(3))

        override fun toJava() = java

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is NeoColor) return false

            if (red != other.red) return false
            if (green != other.green) return false
            if (blue != other.blue) return false
            if (alpha != other.alpha) return false

            return true
        }

        override fun hashCode(): Int {
            var result = red.hashCode()
            result = 31 * result + green.hashCode()
            result = 31 * result + blue.hashCode()
            result = 31 * result + (alpha?.hashCode() ?: 0)
            return result
        }
    }

    @JvmRecord
    data class RGB(
        override val red: Int,
        override val green: Int,
        override val blue: Int
    ) : NeoColor {
        constructor(red: Float, green: Float, blue: Float) : this((red * 255).toInt(), (green * 255).toInt(), (blue * 255).toInt())

        constructor(rgb: Int) : this(
            (rgb shl 16) and 0xFF,
            (rgb shl 8) and 0xFF,
            rgb and 0xFF
        )

        constructor(color: Color) : this(color.red, color.green, color.blue)

        constructor(color: Vector3i) : this(color.x, color.y, color.z)

        constructor(color: Vector3f) : this(color.x, color.y, color.z)

        constructor(color: IVec3<Int>) : this(color.r, color.g, color.b)

        constructor(color: IntArray) : this(color[0], color[1], color[2])

        constructor(color: FloatArray) : this(color[0], color[1], color[2])

        override val alpha: Int?
            get() = null

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is NeoColor) return false

            if (red != other.red) return false
            if (green != other.green) return false
            if (blue != other.blue) return false
            if (alpha != other.alpha) return false

            return true
        }

        override fun hashCode(): Int {
            var result = red.hashCode()
            result = 31 * result + green.hashCode()
            result = 31 * result + blue.hashCode()
            result = 31 * result + (alpha?.hashCode() ?: 0)
            return result
        }
    }

    @JvmRecord
    data class RGBA(
        override val red: Int,
        override val green: Int,
        override val blue: Int,
        override val alpha: Int
    ) : NeoColor {
        constructor(red: Float, green: Float, blue: Float, alpha: Float) : this((red * 255).toInt(), (green * 255).toInt(), (blue * 255).toInt(), (alpha * 255).toInt())

        constructor(rgba: Int) : this(
            (rgba shl 24) and 0xFF,
            (rgba shl 16) and 0xFF,
            (rgba shl 8) and 0xFF,
            rgba and 0xFF
        )

        constructor(color: Color) : this(color.red, color.green, color.blue, color.alpha)

        constructor(color: Vector4i) : this(color.x, color.y, color.z, color.w)

        constructor(color: Vector4f) : this(color.x, color.y, color.z, color.w)

        constructor(color: IVec4<Int>) : this(color.r, color.g, color.b, color.a)

        constructor(color: IntArray) : this(color[0], color[1], color[2], color[3])

        constructor(color: FloatArray) : this(color[0], color[1], color[2], color[3])

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is NeoColor) return false

            if (red != other.red) return false
            if (green != other.green) return false
            if (blue != other.blue) return false
            if (alpha != other.alpha) return false

            return true
        }

        override fun hashCode(): Int {
            var result = red.hashCode()
            result = 31 * result + green.hashCode()
            result = 31 * result + blue.hashCode()
            result = 31 * result + alpha.hashCode()
            return result
        }
    }

    @JvmRecord
    data class RGBF(
        override val redF: Float,
        override val greenF: Float,
        override val blueF: Float
    ) : NeoColor {
        constructor(red: Int, green: Int, blue: Int) : this(red / 255f, green / 255f, blue / 255f)

        constructor(red: UByte, green: UByte, blue: UByte) : this(red.toInt(), green.toInt(), blue.toInt())

        constructor(rgb: Int) : this(
            (rgb shl 16) and 0xFF,
            (rgb shl 8) and 0xFF,
            rgb and 0xFF
        )

        constructor(color: Color) : this(color.red, color.green, color.blue)

        constructor(color: Vector3i) : this(color.x, color.y, color.z)

        constructor(color: Vector3f) : this(color.x, color.y, color.z)

        constructor(color: IVec3<Float>) : this(color.r, color.g, color.b)

        constructor(color: IntArray) : this(color[0], color[1], color[2])

        constructor(color: FloatArray) : this(color[0], color[1], color[2])

        override val alphaF: Float?
            get() = null

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is NeoColor) return false

            if (red != other.red) return false
            if (green != other.green) return false
            if (blue != other.blue) return false
            if (alpha != other.alpha) return false

            return true
        }

        override fun hashCode(): Int {
            var result = red.hashCode()
            result = 31 * result + green.hashCode()
            result = 31 * result + blue.hashCode()
            result = 31 * result + (alpha?.hashCode() ?: 0)
            return result
        }
    }

    @JvmRecord
    data class RGBAF(
        override val redF: Float,
        override val greenF: Float,
        override val blueF: Float,
        override val alphaF: Float
    ) : NeoColor {
        override val alpha: Int
            get() = super.alpha!!

        constructor(red: Int, green: Int, blue: Int, alpha: Int) : this(red / 255f, green / 255f, blue / 255f, alpha / 255f)

        constructor(red: UByte, green: UByte, blue: UByte, alpha: UByte) : this(red.toInt(), green.toInt(), blue.toInt(), alpha.toInt())

        constructor(rgba: Int) : this(
            (rgba shl 24) and 0xFF,
            (rgba shl 16) and 0xFF,
            (rgba shl 8) and 0xFF,
            rgba and 0xFF
        )

        constructor(color: Color) : this(color.red, color.green, color.blue, color.alpha)

        constructor(color: Vector4i) : this(color.x, color.y, color.z, color.w)

        constructor(color: Vector4f) : this(color.x, color.y, color.z, color.w)

        constructor(color: IVec4<Float>) : this(color.r, color.g, color.b, color.a)

        constructor(color: IntArray) : this(color[0], color[1], color[2], color[3])

        constructor(color: FloatArray) : this(color[0], color[1], color[2], color[3])

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is NeoColor) return false

            if (red != other.red) return false
            if (green != other.green) return false
            if (blue != other.blue) return false
            if (alpha != other.alpha) return false

            return true
        }

        override fun hashCode(): Int {
            var result = red.hashCode()
            result = 31 * result + green.hashCode()
            result = 31 * result + blue.hashCode()
            result = 31 * result + alpha.hashCode()
            return result
        }
    }
}