package net.typho.big_shot_lib.api.client.util.resource

import net.typho.big_shot_lib.api.util.resource.NamedResource

interface NeoResourceManagerReloadListener : NamedResource {
    fun onResourceManagerReload(manager: NeoResourceManager)
}