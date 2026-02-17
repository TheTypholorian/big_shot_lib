package net.typho.big_shot_lib

import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.typho.big_shot_lib.api.BigShotApi

@Mod(BigShotApi.MOD_ID)
@OnlyIn(Dist.CLIENT)
class BigShotLibNeoForge(eventBus: IEventBus, modContainer: ModContainer) {
    init {
        BigShotLib.init()
    }
}