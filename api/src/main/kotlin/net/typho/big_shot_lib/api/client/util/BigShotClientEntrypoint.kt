package net.typho.big_shot_lib.api.client.util

import net.typho.big_shot_lib.api.client.event.NeoClientEventBus
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadServices
import net.typho.big_shot_lib.api.util.content.ContentTest
import net.typho.big_shot_lib.api.util.platform.PlatformUtil

interface BigShotClientEntrypoint {
    val modId: String

    fun onInitializeClient(bus: NeoClientEventBus)

    companion object {
        @JvmStatic
        @get:JvmName("getEntrypoints")
        val entrypoints by lazy {
            BigShotClientEntrypoint::class.loadServices()
                .also {
                    if (PlatformUtil.INSTANCE.isDevEnv()) {
                        it.add(ContentTest.Client)
                    }
                }
        }
    }
}