package net.typho.big_shot_lib.api

import net.typho.big_shot_lib.api.event.NeoClientEventBus
import net.typho.big_shot_lib.api.event.NeoEventBus
import net.typho.big_shot_lib.api.plugin.Environment
import net.typho.big_shot_lib.api.plugin.OnlyIn
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadServices
import net.typho.big_shot_lib.api.util.content.ContentTest
import net.typho.big_shot_lib.api.util.platform.PlatformUtil
import java.util.function.Consumer

interface BigShotLibMod {
    val modId: String

    fun addListener(listener: Consumer<NeoEventBus>) {
        if (isInitDone) {
            throw IllegalStateException("Cannot register a mod listener for $modId after init")
        }

        listeners.computeIfAbsent(this) { arrayListOf() }.add(listener)
    }

    fun addClientListener(listener: Consumer<NeoClientEventBus>) {
        if (isClientInitDone) {
            throw IllegalStateException("Cannot register a client mod listener for $modId after init")
        }

        clientListeners.computeIfAbsent(this) { arrayListOf() }.add(listener)
    }

    fun onInitialize(bus: NeoEventBus)

    fun onInitializeClient(bus: NeoClientEventBus)

    companion object {
        @JvmStatic
        @get:JvmName("getMods")
        val mods by lazy {
            BigShotLibMod::class.loadServices()
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
        internal val listeners = hashMapOf<BigShotLibMod, MutableList<Consumer<NeoEventBus>>>()

        @get:JvmStatic
        @get:JvmName("isClientInitDone")
        @OnlyIn(Environment.CLIENT)
        var isClientInitDone = false
            internal set
        @OnlyIn(Environment.CLIENT)
        internal val clientListeners = hashMapOf<BigShotLibMod, MutableList<Consumer<NeoClientEventBus>>>()
    }
}