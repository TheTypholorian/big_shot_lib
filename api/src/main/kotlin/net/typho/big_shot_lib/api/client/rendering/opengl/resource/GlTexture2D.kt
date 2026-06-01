package net.typho.big_shot_lib.api.client.rendering.opengl.resource

import net.minecraft.client.renderer.texture.AbstractTexture
import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureFormat

interface GlTexture2D : GlResource {
    val width: Int
    val height: Int
    val format: GlTextureFormat
    var blur: Boolean
    var mipmap: Boolean

    companion object {
        @JvmStatic
        operator fun get(location: Identifier): AbstractTexture? = InternalUtil.INSTANCE.getTexture(location)

        @JvmStatic
        @JvmOverloads
        @JvmName("create")
        operator fun invoke(width: Int, height: Int, format: GlTextureFormat = GlTextureFormat.RGBA8, blur: Boolean = false, mipmap: Boolean = false): GlTexture2D = InternalUtil.INSTANCE.createTexture(width, height, format, blur, mipmap)
    }
}