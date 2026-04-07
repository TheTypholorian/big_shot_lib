package net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureFormat
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundTexture2D
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture2D
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager
import org.lwjgl.opengl.GL11.GL_TEXTURE_HEIGHT
import org.lwjgl.opengl.GL11.GL_TEXTURE_INTERNAL_FORMAT
import org.lwjgl.opengl.GL11.GL_TEXTURE_WIDTH
import org.lwjgl.opengl.GL11.glGetTexLevelParameteri

open class NeoGlTexture2D(
    glId: Int,
    autoFree: Boolean,
    format: GlTextureFormat?,
    width: Int,
    height: Int
) : NeoGlResource(GlResourceType.TEXTURE, glId, autoFree), GlTexture2D {
    constructor() : this(GlResourceType.TEXTURE.create(), true, null, -1, -1)

    override var format: GlTextureFormat? = format
        protected set
    override var width: Int = width
        protected set
    override var height: Int = height
        protected set

    override fun bind(target: GlTextureTarget): GlBoundTexture2D {
        if (target.dimensions != 2) {
            throw IllegalArgumentException("Non-2D texture target $target")
        }

        return object : GlBoundTexture2D.Basic(this, target, NeoGlStateManager.CURRENT.textures[target].push(glId)) {
            override fun resize(width: Int, height: Int, format: GlTextureFormat) {
                this@NeoGlTexture2D.format = format
                this@NeoGlTexture2D.width = width
                this@NeoGlTexture2D.height = height
            }
        }
    }

    companion object {
        @JvmStatic
        fun ofExisting(
            glId: Int,
            target: GlTextureTarget
        ): NeoGlTexture2D {
            NeoGlStateManager.CURRENT.textures[target].push(glId).use {
                return NeoGlTexture2D(
                    glId,
                    false,
                    GlTextureFormat.fromInternalId(glGetTexLevelParameteri(target.glId, 0, GL_TEXTURE_INTERNAL_FORMAT)),
                    glGetTexLevelParameteri(target.glId, 0, GL_TEXTURE_WIDTH),
                    glGetTexLevelParameteri(target.glId, 0, GL_TEXTURE_HEIGHT)
                )
            }
        }
    }
}