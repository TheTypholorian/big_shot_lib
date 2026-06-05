package net.typho.big_shot_lib.api.client.util

import net.typho.big_shot_lib.api.client.event.NeoClientEventBus
import net.typho.big_shot_lib.api.util.BigShotLibMod
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadServices
import net.typho.big_shot_lib.api.util.content.ContentTest
import net.typho.big_shot_lib.api.util.platform.PlatformUtil
import java.util.function.Consumer

interface NeoClientInitializer : BigShotLibMod<NeoClientEventBus> {
    override fun addListener(listener: Consumer<NeoClientEventBus>) {
        if (isInitDone) {
            throw IllegalStateException("Mod initialization is already done, cannot register mod listeners")
        }

        listeners.computeIfAbsent(this) { arrayListOf() }.add(listener)
    }

    companion object {
        @JvmStatic
        @get:JvmName("getEntrypoints")
        val entrypoints by lazy {
            NeoClientInitializer::class.loadServices()
                .also {
                    if (PlatformUtil.INSTANCE.isDevEnv()) {
                        it.add(ContentTest.Client)
                    }
                }
        }
        @get:JvmStatic
        @get:JvmName("isInitDone")
        var isInitDone = false
            internal set
        internal val listeners = hashMapOf<NeoClientInitializer, MutableList<Consumer<NeoClientEventBus>>>()
    }
}