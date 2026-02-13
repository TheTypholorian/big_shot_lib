package net.typho.big_shot_lib.api.client.rendering.services

import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import java.util.*

interface TextureUtil {
    fun getMinecraftTextureId(texture: ResourceIdentifier): Int

    companion object {
        @JvmField
        val INSTANCE: TextureUtil = ServiceLoader.load(TextureUtil::class.java).findFirst().orElseThrow()
    }
}