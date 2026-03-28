package net.typho.big_shot_lib.api.client.rendering.opengl.state

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture2D
import net.typho.big_shot_lib.api.util.resource.MaybeNamedResource

interface GlBlendShard : MaybeNamedResource {
    val texture: GlTexture2D
}