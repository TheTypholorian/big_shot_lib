package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.util.BoundResource

interface GlDrawStateShard {
    fun bind(): BoundResource
}