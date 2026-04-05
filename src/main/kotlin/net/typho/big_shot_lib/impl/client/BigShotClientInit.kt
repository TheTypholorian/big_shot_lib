package net.typho.big_shot_lib.impl.client

import net.typho.big_shot_lib.impl.client.util.BigShotClientEvents

//? fabric {
/*import net.fabricmc.api.ClientModInitializer

class BigShotClientInit : ClientModInitializer {
    override fun onInitializeClient() {
        BigShotClientEvents.init()
    }
}
*///? } neoforge {
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.typho.big_shot_lib.api.BigShotApi

@Mod(value = BigShotApi.MOD_ID, dist = [Dist.CLIENT])
class BigShotClientInit(eventBus: IEventBus, modContainer: ModContainer) {
    init {
        eventBus.register(BigShotClientEvents::class.java)
        BigShotClientEvents.init()
    }
}
//? }