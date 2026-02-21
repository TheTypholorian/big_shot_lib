package net.typho.big_shot_lib

import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.ResourceManagerReloadListener
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent
import net.typho.big_shot_lib.BigShotLib.toMojang
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.rendering.shaders.NeoShaderRegistry
import net.typho.big_shot_lib.api.services.WrapperUtil

@Mod(BigShotApi.MOD_ID)
@OnlyIn(Dist.CLIENT)
class BigShotLibNeoForge(eventBus: IEventBus, modContainer: ModContainer) {
    init {
        BigShotLib.init()
        eventBus.addListener { event: AddClientReloadListenersEvent ->
            event.addListener(BigShotApi.id("shaders").toMojang(), object : ResourceManagerReloadListener {
                override fun onResourceManagerReload(resourceManager: ResourceManager) {
                    NeoShaderRegistry.onResourceManagerReload(WrapperUtil.INSTANCE.wrap(resourceManager))
                }
            })
        }
    }
}