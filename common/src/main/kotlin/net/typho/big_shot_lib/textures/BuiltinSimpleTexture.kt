package net.typho.big_shot_lib.textures

import net.minecraft.client.renderer.texture.SimpleTexture
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.gl.resource.TextureFormat
import net.typho.big_shot_lib.mixin.textures.SimpleTextureAccessor

class BuiltinSimpleTexture(inner: SimpleTexture) : AbstractBuiltinTexture<SimpleTexture>(inner) {
    override fun location(): ResourceLocation = (inner as SimpleTextureAccessor).location

    override fun getFormat(): TextureFormat = TextureFormat.RGBA
}