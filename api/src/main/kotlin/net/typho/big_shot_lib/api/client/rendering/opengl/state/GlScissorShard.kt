package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.util.BoundResource
import net.typho.big_shot_lib.api.math.rect.AbstractRect2

sealed interface GlScissorShard : GlDrawStateShard {
    interface Enabled : GlScissorShard {
        val scissor: AbstractRect2<Int>

        override fun bind(): BoundResource {
            val flag = NeoGlStateManager.INSTANCE.scissorEnabled.push(true)
            val scissor = NeoGlStateManager.INSTANCE.scissor.push(scissor)

            return BoundResource.all(flag, scissor)
        }
    }

    object Disabled : GlScissorShard {
        override fun bind(): BoundResource {
            return NeoGlStateManager.INSTANCE.scissorEnabled.push(false)
        }
    }
}