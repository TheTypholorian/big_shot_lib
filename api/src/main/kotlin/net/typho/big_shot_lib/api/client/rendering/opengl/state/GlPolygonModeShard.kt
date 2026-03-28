package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlPolygonMode
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource

interface GlPolygonModeShard : GlDrawStateShard {
    val mode: GlPolygonMode

    override fun bind(): BoundResource {
        return NeoGlStateManager.INSTANCE.polygonMode.push(mode)
    }

    companion object {
        @JvmStatic
        fun of(mode: GlPolygonMode) = object : GlPolygonModeShard {
            override val mode: GlPolygonMode = mode
        }
    }
}