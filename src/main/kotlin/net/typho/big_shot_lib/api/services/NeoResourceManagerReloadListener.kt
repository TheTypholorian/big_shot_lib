package net.typho.big_shot_lib.api.services

import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.ResourceManagerReloadListener

interface NeoResourceManagerReloadListener : ResourceManagerReloadListener {
    override fun onResourceManagerReload(manager: ResourceManager) {
        onResourceManagerReload(WrapperUtil.INSTANCE.wrap(manager))
    }

    fun onResourceManagerReload(manager: NeoResourceManager)
}