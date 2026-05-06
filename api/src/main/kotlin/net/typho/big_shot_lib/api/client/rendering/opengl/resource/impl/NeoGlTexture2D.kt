package net.typho.big_shot_lib.api.client.rendering.opengl.resource.impl

import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureFormat
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundTexture2D
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlResourceType
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.type.GlTexture2D
import net.typho.big_shot_lib.api.client.rendering.opengl.state.NeoGlStateManager
import net.typho.big_shot_lib.api.client.rendering.util.RenderingContext
import net.typho.big_shot_lib.api.util.KeyedDelegate
import org.lwjgl.opengl.GL11.GL_TEXTURE_HEIGHT
import org.lwjgl.opengl.GL11.GL_TEXTURE_INTERNAL_FORMAT
import org.lwjgl.opengl.GL11.GL_TEXTURE_WIDTH
import org.lwjgl.opengl.GL11.glGetTexLevelParameteri

open class NeoGlTexture2D(
    glId: Int,
    autoFree: Boolean,
    context: RenderingContext = RenderingContext.get()
) : NeoGlResource(GlResourceType.TEXTURE, glId, autoFree, context), GlTexture2D {
    constructor() : this(GlResourceType.TEXTURE.create(), true)

    protected val formatMap = hashMapOf<Int, GlTextureFormat>()
    protected val widthMap = hashMapOf<Int, Int>()
    protected val heightMap = hashMapOf<Int, Int>()
    override val formats: KeyedDelegate.ReadOnly<Int, GlTextureFormat?> = KeyedDelegate.ReadOnly { formatMap[it] }
    override val widths: KeyedDelegate.ReadOnly<Int, Int?> = KeyedDelegate.ReadOnly { widthMap[it] }
    override val heights: KeyedDelegate.ReadOnly<Int, Int?> = KeyedDelegate.ReadOnly { heightMap[it] }

    override fun bind(target: GlTextureTarget): GlBoundTexture2D {
        checkUsable()
        if (target.dimensions != 2) {
            throw IllegalArgumentException("Non-2D texture target $target")
        }

        return object : GlBoundTexture2D.Basic(this, target, NeoGlStateManager.CURRENT.textures[target].push(glId)) {
            override fun onResize(width: Int, height: Int, format: GlTextureFormat, level: Int) {
                formatMap[level] = format
                widthMap[level] = width
                heightMap[level] = height
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
                    false
                ).also { tex ->
                    GlTextureFormat.fromInternalId(glGetTexLevelParameteri(target.glId, 0, GL_TEXTURE_INTERNAL_FORMAT))?.let { tex.formatMap[0] = it }
                    tex.widthMap[0] = glGetTexLevelParameteri(target.glId, 0, GL_TEXTURE_WIDTH)
                    tex.heightMap[0] = glGetTexLevelParameteri(target.glId, 0, GL_TEXTURE_HEIGHT)
                }
            }
        }

        @JvmStatic
        fun ofExisting(
            glId: Int,
            format: GlTextureFormat?,
            width: Int,
            height: Int
        ): NeoGlTexture2D {
            return NeoGlTexture2D(
                glId,
                false
            ).also { tex ->
                format?.let { tex.formatMap[0] = it }
                tex.widthMap[0] = width
                tex.heightMap[0] = height
            }
        }
    }
}