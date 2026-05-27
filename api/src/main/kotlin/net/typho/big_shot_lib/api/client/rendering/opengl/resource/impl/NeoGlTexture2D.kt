package net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureFormat
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture2D
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import org.lwjgl.opengl.GL11.GL_TEXTURE_HEIGHT
import org.lwjgl.opengl.GL11.GL_TEXTURE_INTERNAL_FORMAT
import org.lwjgl.opengl.GL11.GL_TEXTURE_WIDTH
import org.lwjgl.opengl.GL11.glGetTexLevelParameteri

open class NeoGlTexture2D(
    glId: Int,
    format: GlTextureFormat?,
    width: Int,
    height: Int
) : NeoGlResource(GlResourceType.TEXTURE, glId), GlTexture2D {
    constructor() : this(GlResourceType.TEXTURE.create(), null, -1, -1)

    override var format: GlTextureFormat? = format
        protected set
    override var width: Int = width
        protected set
    override var height: Int = height
        protected set

    companion object {
        @JvmStatic
        fun ofExisting(
            glId: Int
        ): NeoGlTexture2D {
            val old = NeoGlStateManager.INSTANCE.texture
            NeoGlStateManager.INSTANCE.texture = glId
            val texture = NeoGlTexture2D(
                glId,
                GlTextureFormat.fromInternalId(glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_INTERNAL_FORMAT)),
                glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_WIDTH),
                glGetTexLevelParameteri(GL_TEXTURE_2D, 0, GL_TEXTURE_HEIGHT)
            )
            NeoGlStateManager.INSTANCE.texture = old
            return texture
        }
    }
}