package net.typho.big_shot_lib.api.client.util

import net.minecraft.server.packs.resources.PreparableReloadListener
import net.typho.big_shot_lib.api.util.resources.NamedResource
import net.typho.big_shot_lib.api.util.resources.NeoIdentifier
import net.typho.big_shot_lib.api.util.resources.NeoResourceManagerReloadListener

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