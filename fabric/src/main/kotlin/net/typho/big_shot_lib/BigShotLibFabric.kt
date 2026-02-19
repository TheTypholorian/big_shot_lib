package net.typho.big_shot_lib

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.ResourceManager
import net.typho.big_shot_lib.BigShotLib.toMojang
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.rendering.shaders.NeoShaderRegistry
import net.typho.big_shot_lib.api.services.WrapperUtil

object BigShotLibFabric : ClientModInitializer {
    override fun onInitializeClient() {
        BigShotLib.init()
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(object : SimpleSynchronousResourceReloadListener {
            override fun getFabricId(): ResourceLocation {
                return BigShotApi.id("shaders").toMojang()
            }

            override fun onResourceManagerReload(resourceManager: ResourceManager) {
                NeoShaderRegistry.onResourceManagerReload(WrapperUtil.INSTANCE.wrap(resourceManager))
            }
        })
    }
}