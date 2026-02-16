package net.typho.big_shot_lib.api.client.rendering.state

import com.mojang.serialization.MapCodec
import net.typho.big_shot_lib.api.client.rendering.util.GlBindable
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

open class ColorMaskShard(
    @JvmField
    val mask: ColorMask
) : RenderSettingShard.Basic(
    ColorMaskShard,
    listOf(GlBindable.ofStack(GlStateStack.colorMask, mask))
) {
    companion object : RenderSettingShard.Type<ColorMaskShard> {
        override fun getDefault() = ColorMaskShard(ColorMask.DEFAULT)

        override fun codec(): MapCodec<ColorMaskShard>? = ColorMask.CODEC.xmap(
            { mask -> ColorMaskShard(mask) },
            { shard -> shard.mask }
        )

        override fun location(): ResourceIdentifier = ResourceIdentifier("opengl", "color_mask")
    }
}