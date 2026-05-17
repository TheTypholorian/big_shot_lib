package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.plugin.Namespace
import net.typho.big_shot_lib.api.client.rendering.util.BoundResource

@Namespace("big_shot_lib")
interface GlDrawStateShard {
    fun bind(): BoundResource
}