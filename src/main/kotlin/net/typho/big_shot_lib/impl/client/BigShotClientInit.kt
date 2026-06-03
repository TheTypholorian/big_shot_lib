package net.typho.big_shot_lib.impl.client

//? fabric {
/*import net.fabricmc.api.ClientModInitializer
import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint

class BigShotClientInit : ClientModInitializer {
    override fun onInitializeClient() {
        for (entrypoint in BigShotClientEntrypoint.entrypoints) {
            entrypoint.onInitializeClient(NeoClientEventBusImpl)
        }
    }
}
*///? } neoforge {
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.ModList
import net.neoforged.fml.common.Mod
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint

@Mod(value = BigShotApi.MOD_ID, dist = [Dist.CLIENT])
class BigShotClientInit(eventBus: IEventBus, modContainer: ModContainer) {
    init {
        for (entrypoint in BigShotClientEntrypoint.entrypoints) {
            entrypoint.onInitializeClient(
                NeoClientEventBusImpl(
                    ModList.get()
                        .getModContainerById(entrypoint.modId)
                        .orElseThrow { IllegalArgumentException("Unrecognized mod ${entrypoint.modId}, $entrypoint says it's part of it") }
                        .eventBus ?: throw NullPointerException("Cannot get event bus for ${entrypoint.modId}")
                )
            )
        }
    }
}
//? }