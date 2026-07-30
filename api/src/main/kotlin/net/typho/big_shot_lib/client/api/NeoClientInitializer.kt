package net.typho.big_shot_lib.client.api

import net.typho.big_shot_lib.api.event.NeoClientEventBus
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadServices
import net.typho.big_shot_lib.common.annotation.Environment
import net.typho.big_shot_lib.common.annotation.OnlyIn
import java.util.function.Consumer

interface NeoClientInitializer {
    val modId: String

    fun addClientListener(listener: Consumer<NeoClientEventBus>) {
        if (isClientInitDone) {
            throw IllegalStateException("Cannot register a client mod listener for $modId after init")
        }

        clientListeners.computeIfAbsent(modId) { arrayListOf() }.add(listener)
    }

    fun onInitializeClient(bus: NeoClientEventBus)

    companion object {
        @JvmStatic
        @get:JvmName("getMods")
        val mods by lazy { NeoClientInitializer::class.loadServices() }

        // TODO unfuck this
        @get:JvmStatic
        @get:JvmName("isClientInitDone")
        //@OnlyIn(Environment.CLIENT)
        var isClientInitDone = false
            internal set
        //@OnlyIn(Environment.CLIENT)
        internal val clientListeners = hashMapOf<String, MutableList<Consumer<NeoClientEventBus>>>()
    }
}