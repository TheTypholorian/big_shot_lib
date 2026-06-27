package net.typho.big_shot_lib.api.client.rendering.common.constant

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.typho.big_shot_lib.api.util.resource.NeoCodecs

sealed interface GpuBlendFunction {
    companion object {
        @JvmField
        val CODEC: Codec<out GpuBlendFunction> = NeoCodecs.anySubclass(Basic.CODEC.codec(), Separate.CODEC.codec())
        @JvmField
        val NONE = Basic(GpuBlendFactor.one, GpuBlendFactor.zero)
        @JvmField
        val ADDITIVE = Basic(GpuBlendFactor.one, GpuBlendFactor.one)
        @JvmField
        val LIGHTNING = Basic(GpuBlendFactor.srcAlpha, GpuBlendFactor.one)
        @JvmField
        val GLINT = Separate(GpuBlendFactor.srcColor, GpuBlendFactor.one, GpuBlendFactor.zero, GpuBlendFactor.one)
        @JvmField
        val BLOCK_BREAKING = Separate(GpuBlendFactor.dstColor, GpuBlendFactor.srcColor, GpuBlendFactor.one, GpuBlendFactor.zero)
        @JvmField
        val TRANSLUCENT = Separate(GpuBlendFactor.srcAlpha, GpuBlendFactor.oneMinusSrcAlpha, GpuBlendFactor.one, GpuBlendFactor.oneMinusSrcAlpha)
        @JvmField
        val DEFAULT = Separate(GpuBlendFactor.srcAlpha, GpuBlendFactor.oneMinusSrcAlpha, GpuBlendFactor.one, GpuBlendFactor.zero)
    }

    @JvmRecord
    data class Basic(
        @JvmField
        val src: GpuBlendFactor,
        @JvmField
        val dest: GpuBlendFactor
    ) : GpuBlendFunction {
        companion object {
            @JvmField
            val CODEC: MapCodec<Basic> = RecordCodecBuilder.mapCodec {
                it.group(
                    GpuBlendFactor.codec.fieldOf("src").forGetter { basic -> basic.src },
                    GpuBlendFactor.codec.fieldOf("dest").forGetter { basic -> basic.dest }
                ).apply(it, ::Basic)
            }
        }

        override fun hashCode(): Int {
            var result = src.hashCode()
            result = 31 * result + dest.hashCode()
            return result
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true

            if (other is Basic) {
                return src == other.src && dest == other.dest
            } else if (other is Separate) {
                return src == other.src && src == other.srcA && dest == other.dest && dest == other.destA
            }

            return false
        }

        override fun toString(): String {
            return "Blend(src=$src, dest=$dest)"
        }
    }

    @JvmRecord
    data class Separate(
        @JvmField
        val src: GpuBlendFactor,
        @JvmField
        val dest: GpuBlendFactor,
        @JvmField
        val srcA: GpuBlendFactor,
        @JvmField
        val destA: GpuBlendFactor
    ) : GpuBlendFunction {
        companion object {
            @JvmField
            val CODEC: MapCodec<Separate> = RecordCodecBuilder.mapCodec {
                it.group(
                    GpuBlendFactor.codec.fieldOf("src").forGetter { basic -> basic.src },
                    GpuBlendFactor.codec.fieldOf("dest").forGetter { basic -> basic.dest },
                    GpuBlendFactor.codec.fieldOf("srcA").forGetter { basic -> basic.srcA },
                    GpuBlendFactor.codec.fieldOf("destA").forGetter { basic -> basic.destA }
                ).apply(it, ::Separate)
            }
        }

        override fun hashCode(): Int {
            var result = src.hashCode()
            result = 31 * result + dest.hashCode()
            return result
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true

            if (other is Basic) {
                return src == other.src && srcA == other.src && dest == other.dest && destA == other.dest
            } else if (other is Separate) {
                return src == other.src && dest == other.dest && srcA == other.srcA && destA == other.destA
            }

            return false
        }

        override fun toString(): String {
            return "Blend(src=$src, dest=$dest, srcA=$srcA, destA=$destA)"
        }
    }
}