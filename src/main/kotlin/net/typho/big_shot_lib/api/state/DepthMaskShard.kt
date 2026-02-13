package net.typho.big_shot_lib.api.state

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import net.typho.big_shot_lib.api.util.Bindable
import net.typho.big_shot_lib.api.util.ResourceIdentifier

open class DepthMaskShard(
    @JvmField
    val mask: Boolean
) : RenderSettingShard.Basic(
    DepthMaskShard,
    listOf(Bindable.ofState(OpenGL.INSTANCE::depthMask, mask, true))
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