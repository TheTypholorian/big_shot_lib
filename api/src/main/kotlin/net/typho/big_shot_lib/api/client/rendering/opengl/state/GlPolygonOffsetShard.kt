package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.opengl.util.PolygonOffset
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource

sealed interface GlPolygonOffsetShard : GlDrawStateShard {
    interface Enabled : GlPolygonOffsetShard {
        val offset: PolygonOffset

        override fun bind(): BoundResource {
            val flag = NeoGlStateManager.INSTANCE.polygonOffsetEnabled.push(true)
            val offset = NeoGlStateManager.INSTANCE.polygonOffset.push(offset)

            return BoundResource.all(flag, offset)
        }
    }

    object Disabled : GlPolygonOffsetShard {
        override fun bind(): BoundResource {
            return NeoGlStateManager.INSTANCE.polygonOffsetEnabled.push(false)
        }
    }
}