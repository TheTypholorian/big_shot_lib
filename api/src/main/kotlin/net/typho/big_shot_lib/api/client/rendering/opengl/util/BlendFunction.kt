package net.typho.big_shot_lib.api.client.rendering.opengl.util

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlBlendingFactor
import net.typho.big_shot_lib.api.util.resource.NeoCodecs
import org.lwjgl.opengl.GL11.glBlendFunc
import org.lwjgl.opengl.GL14.glBlendFuncSeparate

sealed interface BlendFunction {
    companion object {
        @JvmField
        val CODEC: Codec<out BlendFunction> = NeoCodecs.anySubclass(Basic.CODEC.codec(), Separate.CODEC.codec())
        @JvmField
        val NONE = Basic(GlBlendingFactor.ONE, GlBlendingFactor.ZERO)
        @JvmField
        val ADDITIVE = Basic(GlBlendingFactor.ONE, GlBlendingFactor.ONE)
        @JvmField
        val LIGHTNING = Basic(GlBlendingFactor.SRC_ALPHA, GlBlendingFactor.ONE)
        @JvmField
        val GLINT = Separate(GlBlendingFactor.SRC_COLOR, GlBlendingFactor.ONE, GlBlendingFactor.ZERO, GlBlendingFactor.ONE)
        @JvmField
        val BLOCK_BREAKING = Separate(GlBlendingFactor.DST_COLOR, GlBlendingFactor.SRC_COLOR, GlBlendingFactor.ONE, GlBlendingFactor.ZERO)
        @JvmField
        val TRANSLUCENT = Separate(GlBlendingFactor.SRC_ALPHA, GlBlendingFactor.ONE_MINUS_SRC_ALPHA, GlBlendingFactor.ONE, GlBlendingFactor.ONE_MINUS_SRC_ALPHA)
        @JvmField
        val DEFAULT = Separate(GlBlendingFactor.SRC_ALPHA, GlBlendingFactor.ONE_MINUS_SRC_ALPHA, GlBlendingFactor.ONE, GlBlendingFactor.ZERO)
    }

    fun rawBind()

    @JvmRecord
    data class Basic(
        @JvmField
        val src: GlBlendingFactor,
        @JvmField
        val dest: GlBlendingFactor
    ) : BlendFunction {
        companion object {
            @JvmField
            val CODEC: MapCodec<Basic> = RecordCodecBuilder.mapCodec {
                it.group(
                    NeoCodecs.enumCodec<GlBlendingFactor>().fieldOf("src").forGetter { basic -> basic.src },
                    NeoCodecs.enumCodec<GlBlendingFactor>().fieldOf("dest").forGetter { basic -> basic.dest }
                ).apply(it, ::Basic)
            }
        }

        override fun rawBind() {
            glBlendFunc(src.glId, dest.glId)
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
        val src: GlBlendingFactor,
        @JvmField
        val dest: GlBlendingFactor,
        @JvmField
        val srcA: GlBlendingFactor,
        @JvmField
        val destA: GlBlendingFactor
    ) : BlendFunction {
        companion object {
            @JvmField
            val CODEC: MapCodec<Separate> = RecordCodecBuilder.mapCodec {
                it.group(
                    NeoCodecs.enumCodec<GlBlendingFactor>().fieldOf("src").forGetter { basic -> basic.src },
                    NeoCodecs.enumCodec<GlBlendingFactor>().fieldOf("dest").forGetter { basic -> basic.dest },
                    NeoCodecs.enumCodec<GlBlendingFactor>().fieldOf("srcA").forGetter { basic -> basic.srcA },
                    NeoCodecs.enumCodec<GlBlendingFactor>().fieldOf("destA").forGetter { basic -> basic.destA }
                ).apply(it, ::Separate)
            }
        }

        override fun rawBind() {
            glBlendFuncSeparate(src.glId, dest.glId, srcA.glId, destA.glId)
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