package net.typho.big_shot_lib.impl.util

import net.minecraft.client.Minecraft
import net.typho.big_shot_lib.BigShotLib.toMojang
import net.typho.big_shot_lib.api.client.rendering.services.TextureUtil
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

class TextureUtilImpl : TextureUtil {
    override fun getMinecraftTextureId(texture: ResourceIdentifier): Int {
        return Minecraft.getInstance().textureManager.getTexture(texture.toMojang()).id
    }
}