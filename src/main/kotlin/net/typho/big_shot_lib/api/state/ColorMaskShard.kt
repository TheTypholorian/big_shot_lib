package net.typho.big_shot_lib.api.state

import com.mojang.serialization.MapCodec
import net.minecraft.resources.ResourceIdentifier
import net.typho.big_shot_lib.api.util.Bindable

open class ColorMaskShard(
    @JvmField
    val mask: ColorMask
) : RenderSettingShard.Basic(
    ColorMaskShard,
    listOf(Bindable.ofState(OpenGL.INSTANCE::colorMask, mask, ColorMask.DEFAULT))
) {
    companion object : RenderSettingShard.Type<ColorMaskShard> {
        override fun getDefault() = ColorMaskShard(ColorMask.DEFAULT)

        override fun codec(): MapCodec<ColorMaskShard>? = ColorMask.CODEC.xmap(
            { mask -> ColorMaskShard(mask) },
            { shard -> shard.mask }
        )

        override fun location(): ResourceIdentifier = ResourceIdentifier.fromNamespaceAndPath("opengl", "color_mask")
    }
}