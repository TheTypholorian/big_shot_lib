package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.opengl.util.StencilFunction
import net.typho.big_shot_lib.api.client.rendering.opengl.util.StencilOp
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource
import net.typho.big_shot_lib.api.math.rect.AbstractRect2

sealed interface GlStencilShard : GlDrawStateShard {
    interface Enabled : GlStencilShard {
        val function: StencilFunction
        val mask: Int
        val op: StencilOp

        override fun bind(): BoundResource {
            val flag = NeoGlStateManager.INSTANCE.stencilEnabled.push(true)
            val function = NeoGlStateManager.INSTANCE.stencilFunction.push(function)
            val mask = NeoGlStateManager.INSTANCE.stencilMask.push(mask)
            val op = NeoGlStateManager.INSTANCE.stencilOp.push(op)

            return BoundResource.all(flag, function, mask, op)
        }
    }

    object Disabled : GlStencilShard {
        override fun bind(): BoundResource {
            return NeoGlStateManager.INSTANCE.stencilEnabled.push(false)
        }
    }
}