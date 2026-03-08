package net.typho.big_shot_lib.api.client.opengl.state.shards

import com.mojang.serialization.MapCodec
import net.typho.big_shot_lib.api.client.opengl.state.ColorMask
import net.typho.big_shot_lib.api.client.opengl.state.GlStateStack
import net.typho.big_shot_lib.api.client.opengl.util.GlBindable
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

open class ColorMaskShard(
    @JvmField
    val mask: ColorMask
) : RenderSettingShard.Basic(
    ColorMaskShard,
    listOf(GlBindable.ofStack(GlStateStack.colorMask, mask))
) {
    companion object : RenderSettingShard.Type<ColorMaskShard> {
        override val default = ColorMaskShard(ColorMask.DEFAULT)
        override val codec: MapCodec<ColorMaskShard>? = ColorMask.CODEC.xmap(
            { mask -> ColorMaskShard(mask) },
            { shard -> shard.mask }
        )
        override val location = ResourceIdentifier("opengl", "color_mask")
    }
}