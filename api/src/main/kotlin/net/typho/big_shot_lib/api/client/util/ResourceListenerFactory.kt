package net.typho.big_shot_lib.api.client.util

import net.minecraft.server.packs.resources.PreparableReloadListener
import net.typho.big_shot_lib.api.client.util.resource.NeoResourceManagerReloadListener
import net.typho.big_shot_lib.api.util.resource.NamedResource
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier

interface ResourceListenerFactory {
    fun register(location: NeoIdentifier, listener: PreparableReloadListener)

    fun <T> register(listener: T) where T : PreparableReloadListener, T : NamedResource {
        register(listener.location, listener)
    }

    fun register(location: NeoIdentifier, listener: NeoResourceManagerReloadListener)

    fun <T> register(listener: T) where T : NeoResourceManagerReloadListener, T : NamedResource {
        register(listener.location, listener)
    }
}