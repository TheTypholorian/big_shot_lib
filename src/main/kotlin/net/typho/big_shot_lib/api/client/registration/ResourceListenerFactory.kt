package net.typho.big_shot_lib.api.client.registration

import net.minecraft.server.packs.resources.PreparableReloadListener
import net.typho.big_shot_lib.api.services.NeoResourceManagerReloadListener
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import net.typho.big_shot_lib.api.util.resources.ResourceRegistry

interface ResourceListenerFactory {
    fun register(id: ResourceIdentifier, listener: PreparableReloadListener)

    fun register(id: ResourceIdentifier, listener: NeoResourceManagerReloadListener)

    fun register(registry: ResourceRegistry<*>)
}