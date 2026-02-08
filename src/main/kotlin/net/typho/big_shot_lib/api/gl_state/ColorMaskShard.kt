package net.typho.big_shot_lib.api.gl_state

import com.mojang.serialization.MapCodec
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.api.Bindable
import net.typho.big_shot_lib.api.OpenGL

open class ColorMaskShard(
    @JvmField
    val mask: ColorMask
) : RenderSettingShard.Basic(
    ColorMaskShard,
    listOf(Bindable.ofState(OpenGL::colorMask, mask, ColorMask.DEFAULT))
) {
    companion object : RenderSettingShard.Type<ColorMaskShard> {
        override fun getDefault() = ColorMaskShard(ColorMask.DEFAULT)

        override fun codec(): MapCodec<ColorMaskShard> = ColorMask.CODEC.xmap(
            { mask -> ColorMaskShard(mask) },
            { shard -> shard.mask }
        )

        override fun location(): ResourceLocation = ResourceLocation.fromNamespaceAndPath("opengl", "color_mask")
    }
}