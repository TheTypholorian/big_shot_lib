package net.typho.big_shot_lib.api.util

import net.typho.big_shot_lib.api.event.NeoEventBus
import java.util.function.Consumer

interface BigShotLibMod<B : NeoEventBus> {
    val modId: String

    fun addListener(listener: Consumer<B>)

    fun onInitialize(bus: B)
}