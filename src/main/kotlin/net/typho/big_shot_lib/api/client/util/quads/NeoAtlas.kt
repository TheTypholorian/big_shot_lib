package net.typho.big_shot_lib.api.client.util.quads

import net.typho.big_shot_lib.api.client.opengl.buffers.GlTexture2D
import net.typho.big_shot_lib.api.util.resources.NamedResource
import net.typho.big_shot_lib.api.util.resources.NeoIdentifier

interface NeoAtlas : NamedResource, GlTexture2D {
    val sprites: Map<NeoIdentifier, NeoAtlasSprite>
    val width: Int
    val height: Int
    val mipLevel: Int
}