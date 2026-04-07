package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.util.BoundResource
import net.typho.big_shot_lib.api.math.rect.AbstractRect2

sealed interface GlScissorShard : GlDrawStateShard {
    data class Enabled(
        @JvmField
        val scissor: AbstractRect2<Int>
    ) : GlScissorShard {
        override fun bind(): BoundResource {
            val flag = NeoGlStateManager.CURRENT.scissorEnabled.push(true)
            val scissor = NeoGlStateManager.CURRENT.scissor.push(scissor)

            return BoundResource.all(flag, scissor)
        }
    }

    object Disabled : GlScissorShard {
        override fun bind(): BoundResource {
            return NeoGlStateManager.CURRENT.scissorEnabled.push(false)
        }
    }
}