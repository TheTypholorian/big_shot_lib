package net.typho.big_shot_lib.api.gl_state

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class ColorMask(
    @JvmField
    val red: Boolean,
    @JvmField
    val green: Boolean,
    @JvmField
    val blue: Boolean,
    @JvmField
    val alpha: Boolean
) {
    companion object {
        @JvmField
        val DEFAULT = ColorMask(true, true, true, true)
        @JvmField
        val CODEC: MapCodec<ColorMask> = RecordCodecBuilder.mapCodec {
            it.group(
                Codec.BOOL.fieldOf("red").forGetter { mask -> mask.red },
                Codec.BOOL.fieldOf("green").forGetter { mask -> mask.green },
                Codec.BOOL.fieldOf("blue").forGetter { mask -> mask.blue },
                Codec.BOOL.fieldOf("alpha").forGetter { mask -> mask.alpha }
            ).apply(it, ::ColorMask)
        }
    }
}
