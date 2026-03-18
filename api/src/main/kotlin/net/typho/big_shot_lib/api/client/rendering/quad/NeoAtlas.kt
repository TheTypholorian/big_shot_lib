package net.typho.big_shot_lib.api.client.rendering.quad

import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture
import net.typho.big_shot_lib.api.util.resource.NamedResource
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier

interface NeoAtlas : NamedResource, GlTexture {
    val sprites: Map<NeoIdentifier, NeoAtlasSprite>
    override val width: Int
    override val height: Int
    override val depth: Int
        get() = -1
    val mipLevel: Int
}