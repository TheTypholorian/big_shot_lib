package net.typho.big_shot_lib.api.client.util

import net.minecraft.server.packs.resources.PreparableReloadListener
import net.typho.big_shot_lib.api.util.resources.NeoResourceManagerReloadListener
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import net.typho.big_shot_lib.api.util.resources.ResourceRegistry

interface ResourceListenerFactory {
    fun register(location: ResourceIdentifier, listener: PreparableReloadListener)

    fun register(location: ResourceIdentifier, listener: NeoResourceManagerReloadListener)

    fun register(registry: ResourceRegistry<*>)
}