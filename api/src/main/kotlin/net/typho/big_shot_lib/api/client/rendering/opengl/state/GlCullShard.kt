package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlCullFace
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource

sealed interface GlCullShard : GlDrawStateShard {
    data class Enabled @JvmOverloads constructor(
        @JvmField
        val face: GlCullFace = GlCullFace.BACK
    ) : GlCullShard {
        override fun bind(): BoundResource {
            val flag = NeoGlStateManager.MAIN.cullFaceEnabled.push(true)
            val face = NeoGlStateManager.MAIN.cullFace.push(face)

            return BoundResource.all(flag, face)
        }
    }

    object Disabled : GlCullShard {
        override fun bind(): BoundResource {
            return NeoGlStateManager.MAIN.cullFaceEnabled.push(false)
        }
    }
}