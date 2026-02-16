package net.typho.big_shot_lib.api.client.rendering.state

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import net.typho.big_shot_lib.api.client.rendering.util.GlBindable
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

open class DepthMaskShard(
    @JvmField
    val mask: Boolean
) : RenderSettingShard.Basic(
    DepthMaskShard,
    listOf(GlBindable.ofStack(GlStateStack.depthMask, mask))
) {
    companion object : RenderSettingShard.Type<DepthMaskShard> {
        override fun getDefault() = DepthMaskShard(true)

        override fun codec(): MapCodec<DepthMaskShard>? = Codec.BOOL.fieldOf("mask").xmap(
            { mask -> DepthMaskShard(mask) },
            { shard -> shard.mask }
        )

        override fun location(): ResourceIdentifier = ResourceIdentifier("opengl", "depth_mask")
    }
}