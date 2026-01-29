package net.typho.big_shot_lib.textures

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import org.joml.Vector2i
import org.joml.Vector2ic

interface ITextureSettings {
    fun getSize(): Vector2ic = Vector2i(getWidth(), getHeight())

    fun getWidth(): Int

    fun getHeight(): Int

    fun getFormat(): TextureFormat

    fun getMinInterpolation(): InterpolationType

    fun getMagInterpolation(): InterpolationType

    fun getSWrapping(): WrappingType

    fun getTWrapping(): WrappingType

    interface Mutable : ITextureSettings {
        fun setSize(width: Int, height: Int): Mutable

        fun setSize(size: Vector2ic) = setSize(size.x(), size.y())

        fun setFormat(format: TextureFormat): Mutable

        fun setInterpolation(min: InterpolationType, mag: InterpolationType): Mutable

        fun setWrapping(s: WrappingType, t: WrappingType): Mutable

        fun copy(other: ITextureSettings): Mutable {
            setSize(other.getSize())
            setFormat(other.getFormat())
            setInterpolation(other.getMinInterpolation(), other.getMagInterpolation())
            setWrapping(other.getSWrapping(), other.getTWrapping())
            return this
        }
    }

    open class Storage : Mutable {
        companion object {
            @JvmField
            val CODEC: MapCodec<Storage> = RecordCodecBuilder.mapCodec {
                it.group(
                    Codec.INT.fieldOf("width")
                        .forGetter { s: Storage -> s.getWidth() },
                    Codec.INT.fieldOf("height")
                        .forGetter { s: Storage -> s.getHeight() },
                    TextureFormat.CODEC.optionalFieldOf("format", TextureFormat.RGBA)
                        .forGetter { s: Storage -> s.getFormat() },
                    InterpolationType.CODEC.optionalFieldOf("minInterpolation", InterpolationType.NEAREST)
                        .forGetter { s: Storage -> s.getMinInterpolation() },
                    InterpolationType.CODEC.optionalFieldOf("magInterpolation", InterpolationType.NEAREST)
                        .forGetter { s: Storage -> s.getMagInterpolation() },
                    WrappingType.CODEC.optionalFieldOf("sWrapping", WrappingType.CLAMP_TO_EDGE)
                        .forGetter { s: Storage -> s.getSWrapping() },
                    WrappingType.CODEC.optionalFieldOf("tWrapping", WrappingType.CLAMP_TO_EDGE)
                        .forGetter { s: Storage -> s.getTWrapping() },
                ).apply(it) { width, height, format, min, mag, s, t ->
                    Storage()
                        .setSize(width, height)
                        .setFormat(format)
                        .setInterpolation(min, mag)
                        .setWrapping(s, t)
                }
            }
        }

        protected var width: Int? = null
        protected var height: Int? = null
        protected var format = TextureFormat.RGBA
        protected var minInterpolation = InterpolationType.NEAREST
        protected var magInterpolation = InterpolationType.NEAREST
        protected var sWrapping = WrappingType.CLAMP_TO_EDGE
        protected var tWrapping = WrappingType.CLAMP_TO_EDGE

        override fun getWidth() = width!!

        override fun getHeight() = height!!

        override fun setSize(width: Int, height: Int): Storage {
            this.width = width
            this.height = height
            return this
        }

        override fun getFormat() = format

        override fun setFormat(format: TextureFormat): Storage {
            this.format = format
            return this
        }

        override fun getMinInterpolation() = minInterpolation

        override fun getMagInterpolation() = magInterpolation

        override fun setInterpolation(
            min: InterpolationType,
            mag: InterpolationType
        ): Storage {
            minInterpolation = min
            magInterpolation = mag
            return this
        }

        override fun getSWrapping() = sWrapping

        override fun getTWrapping() = tWrapping

        override fun setWrapping(
            s: WrappingType,
            t: WrappingType
        ): Storage {
            sWrapping = s
            tWrapping = t
            return this
        }

        override fun copy(other: ITextureSettings): Storage {
            super.copy(other)
            return this
        }
    }
}