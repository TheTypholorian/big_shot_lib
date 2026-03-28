package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlAlphaFunction
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource

sealed interface GlDepthShard : GlDrawStateShard {
    data class Enabled @JvmOverloads constructor(
        @JvmField
        val func: GlAlphaFunction,
        @JvmField
        val mask: Boolean = true
    ) : GlDepthShard {
        override fun bind(): BoundResource {
            val flag = NeoGlStateManager.INSTANCE.depthEnabled.push(true)
            val func = NeoGlStateManager.INSTANCE.depthFunc.push(func)
            val mask = NeoGlStateManager.INSTANCE.depthMask.push(mask)

            return BoundResource.all(flag, func, mask)
        }
    }

    object Disabled : GlDepthShard {
        override fun bind(): BoundResource {
            return NeoGlStateManager.INSTANCE.depthEnabled.push(false)
        }
    }
}