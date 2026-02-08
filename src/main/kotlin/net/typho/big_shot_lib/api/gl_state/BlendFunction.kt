package net.typho.big_shot_lib.api.gl_state

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.typho.big_shot_lib.api.Bindable
import net.typho.big_shot_lib.api.OpenGL
import net.typho.big_shot_lib.api.util.NeoCodecs

sealed interface BlendFunction : Bindable {
    companion object {
        @JvmField
        val CODEC: Codec<BlendFunction> = Codec.either(
            Basic.CODEC.codec(),
            Separate.CODEC.codec()
        ).xmap(
            { either -> either.map({ l -> l }, { r -> r }) },
            { func ->
                when (func) {
                    is Basic -> Either.left(func)
                    is Separate -> Either.right(func)
                    else -> throw UnsupportedOperationException("Cannot serialize unknown sealed BlendFunction inheritor ${func.javaClass.simpleName}")
                }
            }
        )
    }

    @JvmRecord
    data class Basic(
        @JvmField
        val src: BlendFactor,
        @JvmField
        val dst: BlendFactor
    ) : BlendFunction {
        override fun bind() {
            OpenGL.INSTANCE.blendFunc(src, dst)
        }

        override fun unbind() {
        }

        companion object {
            @JvmField
            val CODEC: MapCodec<Basic> = RecordCodecBuilder.mapCodec {
                it.group(
                    NeoCodecs.enumCodec<BlendFactor>().fieldOf("src").forGetter { basic -> basic.src },
                    NeoCodecs.enumCodec<BlendFactor>().fieldOf("dst").forGetter { basic -> basic.dst }
                ).apply(it, ::Basic)
            }
        }
    }

    @JvmRecord
    data class Separate(
        @JvmField
        val src: BlendFactor,
        @JvmField
        val dst: BlendFactor,
        @JvmField
        val srcA: BlendFactor,
        @JvmField
        val dstA: BlendFactor
    ) : BlendFunction {
        override fun bind() {
            OpenGL.INSTANCE.blendFuncSeparate(src, dst, srcA, dstA)
        }

        override fun unbind() {
        }

        companion object {
            @JvmField
            val CODEC: MapCodec<Separate> = RecordCodecBuilder.mapCodec {
                it.group(
                    NeoCodecs.enumCodec<BlendFactor>().fieldOf("src").forGetter { basic -> basic.src },
                    NeoCodecs.enumCodec<BlendFactor>().fieldOf("dst").forGetter { basic -> basic.dst },
                    NeoCodecs.enumCodec<BlendFactor>().fieldOf("srcA").forGetter { basic -> basic.srcA },
                    NeoCodecs.enumCodec<BlendFactor>().fieldOf("dstA").forGetter { basic -> basic.dstA }
                ).apply(it, ::Separate)
            }
        }
    }
}