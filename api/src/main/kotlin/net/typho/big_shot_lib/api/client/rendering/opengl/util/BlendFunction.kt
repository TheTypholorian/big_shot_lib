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
        val DEFAULT = Basic(GlBlendingFactor.ONE, GlBlendingFactor.ZERO)
    }

    fun bind()

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

        override fun bind() {
            glBlendFunc(src.glId, dest.glId)
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

        override fun bind() {
            glBlendFuncSeparate(src.glId, dest.glId, srcA.glId, destA.glId)
        }
    }
}