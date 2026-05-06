package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureFormat
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureTarget
import net.typho.big_shot_lib.api.client.rendering.opengl.resource.bound.GlBoundTexture2D
import net.typho.big_shot_lib.api.util.KeyedDelegate
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import org.lwjgl.opengl.GL30.glFramebufferTexture2D

interface GlTexture2D : GlResource, TextureMipmapLevel {
    val formats: KeyedDelegate.ReadOnly<Int, GlTextureFormat?>
    val widths: KeyedDelegate.ReadOnly<Int, Int?>
    val heights: KeyedDelegate.ReadOnly<Int, Int?>
    override val width: Int?
        get() = widths[mipmapLevel]
    override val height: Int?
        get() = heights[mipmapLevel]
    override val mipmapLevel: Int
        get() = 0
    override val texture: GlTexture2D
        get() = this

    fun bind(target: GlTextureTarget): GlBoundTexture2D

    fun level(level: Int) = object : TextureMipmapLevel {
        override val mipmapLevel = level
        override val texture = this@GlTexture2D
    }

    companion object {
        @JvmStatic
        operator fun get(location: NeoIdentifier) = InternalUtil.INSTANCE.getTexture(location)
    }
}