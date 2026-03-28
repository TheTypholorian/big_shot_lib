package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlCullFace
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource

sealed interface GlCullShard : GlDrawStateShard {
    interface Enabled : GlCullShard {
        val face: GlCullFace

        override fun bind(): BoundResource {
            val flag = NeoGlStateManager.INSTANCE.cullFaceEnabled.push(true)
            val face = NeoGlStateManager.INSTANCE.cullFace.push(face)

            return BoundResource.all(flag, face)
        }
    }

    object Disabled : GlCullShard {
        override fun bind(): BoundResource {
            return NeoGlStateManager.INSTANCE.cullFaceEnabled.push(false)
        }
    }
}