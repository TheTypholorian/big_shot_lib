package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.opengl.util.ColorMask
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource

interface GlColorMaskShard : GlDrawStateShard {
    val mask: ColorMask

    override fun bind(): BoundResource {
        return NeoGlStateManager.INSTANCE.colorMask.push(mask)
    }

    companion object {
        @JvmStatic
        fun of(mask: ColorMask) = object : GlColorMaskShard {
            override val mask: ColorMask = mask
        }
    }
}