package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.opengl.util.PolygonOffset
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource

sealed interface GlPolygonOffsetShard : GlDrawStateShard {
    data class Enabled(
        @JvmField
        val offset: PolygonOffset
    ) : GlPolygonOffsetShard {
        override fun bind(): BoundResource {
            val flag = NeoGlStateManager.CURRENT.polygonOffsetEnabled.push(true)
            val offset = NeoGlStateManager.CURRENT.polygonOffset.push(offset)

            return BoundResource.all(flag, offset)
        }
    }

    object Disabled : GlPolygonOffsetShard {
        override fun bind(): BoundResource {
            return NeoGlStateManager.CURRENT.polygonOffsetEnabled.push(false)
        }
    }
}