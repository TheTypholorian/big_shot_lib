package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlAlphaFunction
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource

sealed interface GlDepthShard : GlDrawStateShard {
    interface Enabled : GlDepthShard {
        val mask: Boolean
        val func: GlAlphaFunction

        override fun bind(): BoundResource {
            val flag = NeoGlStateManager.INSTANCE.depthEnabled.push(true)
            val mask = NeoGlStateManager.INSTANCE.depthMask.push(mask)
            val func = NeoGlStateManager.INSTANCE.depthFunc.push(func)
            
            return BoundResource.all(flag, mask, func)
        }
    }

    object Disabled : GlDepthShard {
        override fun bind(): BoundResource {
            return NeoGlStateManager.INSTANCE.depthEnabled.push(false)
        }
    }
}