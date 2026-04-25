package net.typho.big_shot_lib.api.client.util

import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadServices
import net.typho.big_shot_lib.api.client.util.event.ClientEventFactory

interface BigShotClientEntrypoint {
    fun registerReloadListeners(factory: ResourceListenerFactory) {
    }

    fun registerEvents(factory: ClientEventFactory) {
    }

    fun registerDebugScreenInfo(factory: DebugScreenFactory) {
    }

    fun displayInitialScreens(factory: InitialScreenFactory) {
    }

    companion object : BigShotClientEntrypoint {
        val entrypoints by lazy {
            val services = BigShotClientEntrypoint::class.loadServices()

            for (entrypoint in services) {
                BigShotApi.LOGGER.info("Loading client entrypoint $entrypoint")
            }

            services
        }

        override fun registerReloadListeners(factory: ResourceListenerFactory) {
            entrypoints.forEach { it.registerReloadListeners(factory) }
        }

        override fun registerEvents(factory: ClientEventFactory) {
            entrypoints.forEach { it.registerEvents(factory) }
        }

        override fun registerDebugScreenInfo(factory: DebugScreenFactory) {
            entrypoints.forEach { it.registerDebugScreenInfo(factory) }
        }

        override fun displayInitialScreens(factory: InitialScreenFactory) {
            entrypoints.forEach { it.displayInitialScreens(factory) }
        }
    }
}