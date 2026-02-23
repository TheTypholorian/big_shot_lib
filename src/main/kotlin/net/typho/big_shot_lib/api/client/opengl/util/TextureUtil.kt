package net.typho.big_shot_lib.api.client.opengl.util

import net.typho.big_shot_lib.api.BigShotApi.loadService
import net.typho.big_shot_lib.api.client.opengl.buffers.GlTexture2D
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

interface TextureUtil {
    val blockAtlasTexture: ResourceIdentifier

    //fun getRenderTypeTexture(type: RenderType): ResourceIdentifier?

    fun getMinecraftTexture(texture: ResourceIdentifier): GlTexture2D

    companion object {
        @JvmField
        val INSTANCE: TextureUtil = TextureUtil::class.loadService()
    }
}