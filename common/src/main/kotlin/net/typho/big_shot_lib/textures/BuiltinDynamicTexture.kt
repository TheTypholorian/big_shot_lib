package net.typho.big_shot_lib.textures

import net.minecraft.client.renderer.texture.DynamicTexture
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.gl.resource.TextureFormat

class BuiltinDynamicTexture(inner: DynamicTexture) : AbstractBuiltinTexture<DynamicTexture>(inner) {
    override fun location(): ResourceLocation? = null

    override fun format(): TextureFormat = TextureFormat.get(inner.pixels!!.format())
}