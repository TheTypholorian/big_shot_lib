package net.typho.big_shot_lib.api.client.rendering.opengl.util

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlAlphaFunction
import net.typho.big_shot_lib.api.util.resource.NeoCodecs

@JvmRecord
data class StencilFunction(
    @JvmField
    val func: GlAlphaFunction,
    @JvmField
    val ref: Int,
    @JvmField
    val mask: Int
) {
    companion object {
        @JvmField
        val CODEC: MapCodec<StencilFunction> = RecordCodecBuilder.mapCodec {
            it.group(
                NeoCodecs.enumCodec<GlAlphaFunction>().fieldOf("func").forGetter { color -> color.func },
                Codec.INT.fieldOf("ref").forGetter { color -> color.ref },
                Codec.INT.fieldOf("mask").forGetter { color -> color.mask }
            ).apply(it, ::StencilFunction)
        }
    }
}