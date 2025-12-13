package net.typho.big_shot_lib

import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod

@Mod(BigShotLib.MOD_ID)
class BigShotLibNeoForge(eventBus: IEventBus, modContainer: ModContainer) {
    init {
        BigShotLib.init()
    }
}