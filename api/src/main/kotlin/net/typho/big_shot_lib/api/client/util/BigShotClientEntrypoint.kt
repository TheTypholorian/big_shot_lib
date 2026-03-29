package net.typho.big_shot_lib.api.client.util

import net.typho.big_shot_lib.api.BigShotApi.loadServices
import net.typho.big_shot_lib.api.client.util.event.ClientEventFactory

interface BigShotClientEntrypoint {
    fun registerReloadListeners(factory: ResourceListenerFactory) {
    }

    fun registerEvents(factory: ClientEventFactory) {
    }

    fun registerDebugScreenInfo(factory: DebugScreenFactory) {
    }

    companion object : BigShotClientEntrypoint {
        @JvmField
        val entrypoints = BigShotClientEntrypoint::class.loadServices()

        override fun registerReloadListeners(factory: ResourceListenerFactory) {
            entrypoints.forEach { it.registerReloadListeners(factory) }
        }

        override fun registerEvents(factory: ClientEventFactory) {
            entrypoints.forEach { it.registerEvents(factory) }
        }

        override fun registerDebugScreenInfo(factory: DebugScreenFactory) {
            entrypoints.forEach { it.registerDebugScreenInfo(factory) }
        }
    }
}