package net.typho.big_shot_lib.api.builtin

import net.minecraft.client.renderer.texture.ReloadableTexture
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.gl.resource.TextureFormat

class BuiltinReloadableTexture(inner: ReloadableTexture) : AbstractBuiltinTexture<ReloadableTexture>(inner) {
    override fun location(): ResourceLocation = inner.resourceId()

    override fun format(): TextureFormat = TextureFormat.RGBA
}