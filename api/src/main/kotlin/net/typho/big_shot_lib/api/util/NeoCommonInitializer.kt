package net.typho.big_shot_lib.api.util

import net.typho.big_shot_lib.api.event.NeoEventBus
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadServices
import net.typho.big_shot_lib.api.util.content.ContentTest
import net.typho.big_shot_lib.api.util.platform.PlatformUtil
import java.util.function.Consumer

interface NeoCommonInitializer : BigShotLibMod<NeoEventBus> {
    override fun addListener(listener: Consumer<NeoEventBus>) {
        if (isInitDone) {
            throw IllegalStateException("Mod initialization is already done, cannot register mod listeners")
        }

        listeners.computeIfAbsent(this) { arrayListOf() }.add(listener)
    }

    companion object {
        @JvmStatic
        @get:JvmName("getEntrypoints")
        val entrypoints by lazy {
            NeoCommonInitializer::class.loadServices()
                .also {
                    if (PlatformUtil.INSTANCE.isDevEnv()) {
                        it.add(ContentTest)
                    }
                }
        }
        @get:JvmStatic
        @get:JvmName("isInitDone")
        var isInitDone = false
            internal set
        internal val listeners = hashMapOf<NeoCommonInitializer, MutableList<Consumer<NeoEventBus>>>()
    }
}