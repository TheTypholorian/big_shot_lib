package net.typho.big_shot_lib.state.gl

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.ResourceLocation

class ColorMask(
    var red: Boolean,
    var green: Boolean,
    var blue: Boolean,
    var alpha: Boolean,
) : GlState<ColorMask> {
    companion object {
        @JvmField
        val DEFAULT = ColorMask(true, true, true, true)
        @JvmField
        val LOCATION: ResourceLocation = ResourceLocation.fromNamespaceAndPath("opengl", "color_mask")
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

    override fun location() = LOCATION

    override fun default() = DEFAULT

    override fun queryValue(): ColorMask? = null

    override fun set(value: ColorMask) {
        GlStateManager._colorMask(value.red, value.green, value.blue, value.alpha)
    }
}
