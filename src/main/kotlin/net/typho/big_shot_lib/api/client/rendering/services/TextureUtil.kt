package net.typho.big_shot_lib.api.client.rendering.services

import net.typho.big_shot_lib.api.BigShotApi.loadService
import net.typho.big_shot_lib.api.client.rendering.textures.GlTexture2D
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

interface TextureUtil {
    fun getMinecraftTexture(texture: ResourceIdentifier): GlTexture2D

    companion object {
        @JvmField
        val INSTANCE: TextureUtil = TextureUtil::class.loadService()
    }
}