package net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureFormat
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundTexture2D
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture2D
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager

class NeoGlTexture2D(glId: Int, autoFree: Boolean) : NeoGlResource(GlResourceType.TEXTURE, glId, autoFree), GlTexture2D {
    constructor() : this(GlResourceType.TEXTURE.create(), true)

    override var format: GlTextureFormat? = null
        private set
    override var width: Int = -1
        private set
    override var height: Int = -1
        private set

    override fun bind(target: GlTextureTarget): GlBoundTexture2D {
        if (target.dimensions != 2) {
            throw IllegalArgumentException("Non-2D texture target $target")
        }

        return object : GlBoundTexture2D.Basic(this, target, NeoGlStateManager.INSTANCE.textures[target].push(glId)) {
            override fun resize(width: Int, height: Int, format: GlTextureFormat) {
                this@NeoGlTexture2D.format = format
                this@NeoGlTexture2D.width = width
                this@NeoGlTexture2D.height = height
            }
        }
    }
}