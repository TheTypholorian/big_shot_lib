package net.typho.big_shot_lib

import net.fabricmc.api.ModInitializer
import net.typho.big_shot_lib.platform.BigShotLib

class BigShotLibFabric : ModInitializer {
    override fun onInitialize() {
        BigShotLib.init()
    }
}