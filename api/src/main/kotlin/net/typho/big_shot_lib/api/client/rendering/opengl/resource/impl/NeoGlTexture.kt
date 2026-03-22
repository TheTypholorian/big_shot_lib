package net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureFormat
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundTexture
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager

class NeoGlTexture(glId: Int, autoFree: Boolean) : NeoGlResource(GlResourceType.TEXTURE, glId, autoFree), GlTexture {
    constructor() : this(GlResourceType.TEXTURE.create(), true)

    override var format: GlTextureFormat? = null
        private set
    override var width: Int = 1
        private set
    override var height: Int = -1
        private set
    override var depth: Int = -1
        private set

    override fun bind(target: GlTextureTarget): GlBoundTexture {
        return object : GlBoundTexture.Basic(this, target, NeoGlStateManager.INSTANCE.textures[target].push(glId)) {
            override fun resize(width: Int, height: Int, depth: Int, format: GlTextureFormat) {
                this@NeoGlTexture.format = format
                this@NeoGlTexture.width = width
                this@NeoGlTexture.height = height
                this@NeoGlTexture.depth = depth
            }
        }
    }
}