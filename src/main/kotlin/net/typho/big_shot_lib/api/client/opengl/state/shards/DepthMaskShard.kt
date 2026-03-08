package net.typho.big_shot_lib.api.client.opengl.state.shards

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import net.typho.big_shot_lib.api.client.opengl.state.GlStateStack
import net.typho.big_shot_lib.api.client.opengl.util.GlBindable
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

open class DepthMaskShard(
    @JvmField
    val mask: Boolean
) : RenderSettingShard.Basic(
    DepthMaskShard,
    listOf(GlBindable.ofStack(GlStateStack.depthMask, mask))
) {
    companion object : RenderSettingShard.Type<DepthMaskShard> {
        override val default = DepthMaskShard(true)
        override val codec: MapCodec<DepthMaskShard>? = Codec.BOOL.fieldOf("mask").xmap(
            { mask -> DepthMaskShard(mask) },
            { shard -> shard.mask }
        )
        override val location = ResourceIdentifier("opengl", "depth_mask")
    }
}