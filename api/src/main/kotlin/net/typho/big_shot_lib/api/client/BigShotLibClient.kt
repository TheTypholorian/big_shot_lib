package net.typho.big_shot_lib.api.client

import net.typho.big_shot_lib.api.BigShotLib
import net.typho.big_shot_lib.api.client.rendering.NeoShaderLoader
import net.typho.big_shot_lib.api.event.NeoClientEventBus

object BigShotLibClient : NeoClientInitializer {
    override val modId: String = BigShotLib.modId

    override fun onInitializeClient(bus: NeoClientEventBus) {
        NeoShaderLoader.onInitializeClient(bus)
    }
}