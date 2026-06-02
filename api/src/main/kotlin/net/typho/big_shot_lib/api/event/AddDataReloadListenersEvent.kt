package net.typho.big_shot_lib.api.event

import net.typho.big_shot_lib.api.util.resource.NeoReloadListener

fun interface AddDataReloadListenersEvent {
    fun registerReloadListeners(
        out: (listener: NeoReloadListener) -> Unit
    )
}