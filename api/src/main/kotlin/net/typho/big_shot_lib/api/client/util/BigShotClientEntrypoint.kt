package net.typho.big_shot_lib.api.client.util

import net.typho.big_shot_lib.api.client.event.NeoClientEventBus
import net.typho.big_shot_lib.api.client.rendering.util.MainMenuMode
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadServices

interface BigShotClientEntrypoint {
    fun onInitializeClient(bus: NeoClientEventBus) {
    }

    companion object {
        @JvmStatic
        @get:JvmName("getEntrypoints")
        val entrypoints by lazy { BigShotClientEntrypoint::class.loadServices() }
        @JvmStatic
        @get:JvmName("getMainMenuModes")
        val mainMenuModes by lazy {
            mutableListOf(MainMenuMode.MINECRAFT) // TODO
            /*
            entrypoints.flatMapTo(
                mutableListOf(MainMenuMode.MINECRAFT),
                { it.loadMainMenuModes() }
            )
                .also {
                    it.sortWith(Comparator.comparingInt { -it.priority })
                }
             */
        }
    }
}