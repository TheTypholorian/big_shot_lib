package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlCullFace
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource

sealed interface GlCullShard : GlDrawStateShard {
    data class Enabled(
        @JvmField
        val face: GlCullFace
    ) : GlCullShard {
        override fun bind(): BoundResource {
            val flag = NeoGlStateManager.CURRENT.cullFaceEnabled.push(true)
            val face = NeoGlStateManager.CURRENT.cullFace.push(face)

            return BoundResource.all(flag, face)
        }
    }

    object Disabled : GlCullShard {
        override fun bind(): BoundResource {
            return NeoGlStateManager.CURRENT.cullFaceEnabled.push(false)
        }
    }
}