package net.typho.big_shot_lib.impl.util

import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.api.util.TextureUtil

class TextureUtilImpl : TextureUtil {
    override fun getMinecraftTextureId(texture: ResourceLocation): Int {
        return Minecraft.getInstance().textureManager.getTexture(texture).id
    }
}