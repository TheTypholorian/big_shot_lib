package net.typho.big_shot_lib.api.util

import net.typho.big_shot_lib.api.event.NeoEventBus
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadServices

interface BigShotCommonEntrypoint {
    val modId: String

    fun onInitialize(bus: NeoEventBus)

    companion object {
        @JvmStatic
        @get:JvmName("getEntrypoints")
        val entrypoints by lazy { BigShotCommonEntrypoint::class.loadServices() }
    }
}