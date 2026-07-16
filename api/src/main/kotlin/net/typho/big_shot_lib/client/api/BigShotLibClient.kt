package net.typho.big_shot_lib.client.api

import net.typho.big_shot_lib.api.BigShotLib
import net.typho.big_shot_lib.api.event.NeoClientEventBus

object BigShotLibClient : NeoClientInitializer {
    override val modId: String = BigShotLib.modId

    override fun onInitializeClient(bus: NeoClientEventBus) {
    }
}