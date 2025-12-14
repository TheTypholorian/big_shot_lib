package net.typho.big_shot_lib

import net.fabricmc.api.ClientModInitializer

object BigShotLibFabric : ClientModInitializer {
    override fun onInitializeClient() {
        BigShotLib.init()
    }
}