package net.typho.big_shot_lib.api.util

import net.minecraft.resources.ResourceLocation
import java.util.*

interface TextureUtil {
    fun getMinecraftTextureId(texture: ResourceLocation): Int

    companion object {
        @JvmField
        val INSTANCE: TextureUtil = ServiceLoader.load(TextureUtil::class.java).findFirst().orElseThrow()
    }
}