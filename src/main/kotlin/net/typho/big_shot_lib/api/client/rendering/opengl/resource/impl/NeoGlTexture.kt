package net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundTexture
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.state.NeoGlStateManager
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture

class NeoGlTexture(glId: Int) : NeoGlResource(GlResourceType.TEXTURE, glId), GlTexture {
    constructor() : this(GlResourceType.TEXTURE.create())

    override fun bind(target: GlTextureTarget): GlBoundTexture {
        return GlBoundTexture.Basic(this, target, NeoGlStateManager.INSTANCE.textures[target].push(glId))
    }
}