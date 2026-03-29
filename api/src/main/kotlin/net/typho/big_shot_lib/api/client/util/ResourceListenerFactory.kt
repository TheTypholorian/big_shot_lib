package net.typho.big_shot_lib.api.client.util

import net.minecraft.server.packs.resources.PreparableReloadListener
import net.typho.big_shot_lib.api.client.util.resource.NeoResourceManagerReloadListener
import net.typho.big_shot_lib.api.util.resource.NamedResource
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier

interface ResourceListenerFactory {
    fun register(listener: NeoResourceManagerReloadListener)
}