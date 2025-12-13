package net.typho.big_shot_lib

import net.fabricmc.api.ModInitializer

class BigShotLibFabric : ModInitializer {
    override fun onInitialize() {
        BigShotLib.init()
    }
}