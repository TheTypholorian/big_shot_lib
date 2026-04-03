package net.typho.big_shot_lib.impl

import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.typho.big_shot_lib.impl.util.BigShotCommonEvents

//? fabric {
/*import net.fabricmc.api.ModInitializer

class BigShotCommonInit : ModInitializer {
    override fun onInitialize() {
        BigShotCommonEvents.init()
    }
}
*///? } neoforge {
import net.neoforged.fml.common.Mod
import net.typho.big_shot_lib.api.BigShotApi

@Mod(value = BigShotApi.MOD_ID)
class BigShotCommonInit(eventBus: IEventBus, modContainer: ModContainer) {
    init {
        BigShotCommonEvents.init(eventBus)
    }
}
//? }