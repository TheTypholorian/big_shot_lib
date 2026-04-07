package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.opengl.util.StencilFunction
import net.typho.big_shot_lib.api.client.rendering.opengl.util.StencilOp
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource

sealed interface GlStencilShard : GlDrawStateShard {
    data class Enabled(
        @JvmField
        val function: StencilFunction,
        @JvmField
        val mask: Int,
        @JvmField
        val op: StencilOp
    ) : GlStencilShard {
        override fun bind(): BoundResource {
            val flag = NeoGlStateManager.CURRENT.stencilEnabled.push(true)
            val function = NeoGlStateManager.CURRENT.stencilFunction.push(function)
            val mask = NeoGlStateManager.CURRENT.stencilMask.push(mask)
            val op = NeoGlStateManager.CURRENT.stencilOp.push(op)

            return BoundResource.all(flag, function, mask, op)
        }
    }

    object Disabled : GlStencilShard {
        override fun bind(): BoundResource {
            return NeoGlStateManager.CURRENT.stencilEnabled.push(false)
        }
    }
}