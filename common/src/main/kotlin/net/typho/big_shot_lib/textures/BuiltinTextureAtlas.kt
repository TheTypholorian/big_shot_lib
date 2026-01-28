package net.typho.big_shot_lib.textures

import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.gl.resource.TextureFormat

class BuiltinTextureAtlas(inner: TextureAtlas) : AbstractBuiltinTexture<TextureAtlas>(inner) {
    override fun location(): ResourceLocation = inner.location()

    override fun getFormat(): TextureFormat = TextureFormat.RGBA
}