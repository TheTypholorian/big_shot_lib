package net.typho.big_shot_lib.api.gl_state

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.api.Bindable
import net.typho.big_shot_lib.api.OpenGL

open class DepthMaskShard(
    @JvmField
    val mask: Boolean
) : RenderSettingShard.Basic(
    DepthMaskShard,
    listOf(Bindable.ofState(OpenGL::depthMask, mask, true))
) {
    companion object : RenderSettingShard.Type<DepthMaskShard> {
        override fun getDefault() = DepthMaskShard(true)

        override fun codec(): MapCodec<DepthMaskShard> = Codec.BOOL.fieldOf("mask").xmap(
            { mask -> DepthMaskShard(mask) },
            { shard -> shard.mask }
        )

        override fun location(): ResourceLocation = ResourceLocation.fromNamespaceAndPath("opengl", "depth_mask")
    }
}