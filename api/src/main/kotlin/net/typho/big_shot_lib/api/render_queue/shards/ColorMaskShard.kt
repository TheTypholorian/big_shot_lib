package net.typho.big_shot_lib.api.render_queue.shards

import com.mojang.serialization.MapCodec
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.api.Bindable
import net.typho.big_shot_lib.api.GlManager
import net.typho.big_shot_lib.api.render_queue.RenderSettingShard

open class ColorMaskShard(
    @JvmField
    val mask: ColorMask
) : RenderSettingShard.Basic(
    ColorMaskShard,
    listOf(Bindable.ofState(GlManager::colorMask, mask, ColorMask.DEFAULT))
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