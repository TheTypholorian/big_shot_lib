package net.typho.big_shot_lib.client.api.event

import net.typho.big_shot_lib.api.util.resource.NeoReloadListener

fun interface AddAssetReloadListenersEvent {
    fun registerReloadListeners(
        out: (listener: NeoReloadListener) -> Unit
    )
}