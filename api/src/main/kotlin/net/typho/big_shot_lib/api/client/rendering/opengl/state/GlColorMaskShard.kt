package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.opengl.util.ColorMask
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource

data class GlColorMaskShard(
    @JvmField
    val mask: ColorMask
) : GlDrawStateShard {
    override fun bind(): BoundResource {
        return NeoGlStateManager.CURRENT.colorMask.push(mask)
    }
}