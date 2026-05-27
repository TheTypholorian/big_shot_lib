package net.typho.big_shot_lib.api.client.rendering.opengl.resource.type

import net.minecraft.resources.Identifier
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.client.rendering.opengl.constant.GlTextureFormat
import net.typho.big_shot_lib.api.plugin.Namespace
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import org.lwjgl.opengl.GL30.glFramebufferTexture2D

@Namespace(BigShotApi.MOD_ID)
interface GlTexture2D : GlResource {
    val format: GlTextureFormat?
    val width: Int
    val height: Int
    var blur: Boolean
    var mipmap: Boolean

    companion object {
        @JvmStatic
        operator fun get(location: Identifier) = InternalUtil.INSTANCE.getTexture(location)
    }
}