package net.typho.big_shot_lib.api

import net.typho.big_shot_lib.api.client.NeoClientInitializer
import net.typho.big_shot_lib.api.event.NeoClientEventBus
import net.typho.big_shot_lib.api.event.NeoEventBus
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadServices
import net.typho.big_shot_lib.api.util.content.ContentTest
import net.typho.big_shot_lib.api.util.platform.PlatformUtil
import java.util.function.Consumer

interface NeoCommonInitializer {
    val modId: String

    fun addListener(listener: Consumer<NeoEventBus>) {
        if (isInitDone) {
            throw IllegalStateException("Cannot register a mod listener for $modId after init")
        }

        listeners.computeIfAbsent(modId) { arrayListOf() }.add(listener)
    }

    fun addClientListener(listener: Consumer<NeoClientEventBus>) {
        if (PlatformUtil.INSTANCE.isClient()) {
            if (NeoClientInitializer.isClientInitDone) {
                throw IllegalStateException("Cannot register a client mod listener for $modId after init")
            }

            NeoClientInitializer.clientListeners.computeIfAbsent(modId) { arrayListOf() }.add(listener)
        }
    }

    fun onInitialize(bus: NeoEventBus)

    companion object {
        @JvmStatic
        @get:JvmName("getMods")
        val mods by lazy {
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
        internal val listeners = hashMapOf<String, MutableList<Consumer<NeoEventBus>>>()
    }
}