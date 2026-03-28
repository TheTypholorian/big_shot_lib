package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlPolygonMode
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource

data class GlPolygonModeShard(
    @JvmField
    val mode: GlPolygonMode
) : GlDrawStateShard {
    override fun bind(): BoundResource {
        return NeoGlStateManager.INSTANCE.polygonMode.push(mode)
    }
}