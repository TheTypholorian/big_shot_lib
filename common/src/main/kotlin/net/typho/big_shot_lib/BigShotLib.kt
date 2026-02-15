package net.typho.big_shot_lib

import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.api.client.rendering.shaders.mixins.ShaderMixinManager
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import net.typho.big_shot_lib.impl.shaders.mixins.BreezeWindShaderMixin

object BigShotLib {
    @JvmStatic
    fun ResourceLocation.toNeo(): ResourceIdentifier = ResourceIdentifier(namespace, path)

    @JvmStatic
    fun ResourceIdentifier.toMojang(): ResourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, path)

    @JvmStatic
    fun init() {
        ShaderMixinManager.register(BreezeWindShaderMixin)
    }
}