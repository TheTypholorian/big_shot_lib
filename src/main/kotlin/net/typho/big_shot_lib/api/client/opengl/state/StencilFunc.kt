package net.typho.big_shot_lib.api.client.opengl.state

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.typho.big_shot_lib.api.util.resources.NeoCodecs

@JvmRecord
data class StencilFunc(
    @JvmField
    val func: ComparisonFunc,
    @JvmField
    val ref: Int,
    @JvmField
    val mask: Int
) {
    companion object {
        @JvmField
        val CODEC: MapCodec<StencilFunc> = RecordCodecBuilder.mapCodec {
            it.group(
                NeoCodecs.enumCodec<ComparisonFunc>().fieldOf("func").forGetter { color -> color.func },
                Codec.INT.fieldOf("ref").forGetter { color -> color.ref },
                Codec.INT.fieldOf("mask").forGetter { color -> color.mask }
            ).apply(it, ::StencilFunc)
        }
    }
}