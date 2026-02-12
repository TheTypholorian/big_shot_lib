package net.typho.big_shot_lib.api.util

import net.minecraft.resources.Identifier
import java.util.*

interface TextureUtil {
    fun getMinecraftTextureId(texture: Identifier): Int

    companion object {
        @JvmField
        val INSTANCE: TextureUtil = ServiceLoader.load(TextureUtil::class.java).findFirst().orElseThrow()
    }
}