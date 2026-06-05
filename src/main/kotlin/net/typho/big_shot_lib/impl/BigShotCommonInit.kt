package net.typho.big_shot_lib.impl

//? fabric {
/*import net.fabricmc.api.ModInitializer
import net.typho.big_shot_lib.api.util.NeoCommonInitializer

class BigShotCommonInit : ModInitializer {
    override fun onInitialize() {
        for (entrypoint in NeoCommonInitializer.entrypoints) {
            entrypoint.onInitialize(NeoEventBusImpl)
        }
    }
}
*///? } neoforge {
import net.neoforged.fml.common.Mod
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.ModList
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.util.NeoCommonInitializer

@Mod(value = BigShotApi.MOD_ID)
class BigShotCommonInit(eventBus: IEventBus, modContainer: ModContainer) {
    init {
        val buses = NeoCommonInitializer.entrypoints.associateWith {
            NeoEventBusImpl(
                ModList.get()
                    .getModContainerById(it.modId)
                    .orElseThrow { IllegalArgumentException("Unrecognized mod ${it.modId}, $it says it's part of it") }
                    .eventBus ?: throw NullPointerException("Cannot get event bus for ${it.modId}")
            )
        }

        buses.forEach { (init, bus) -> init.onInitialize(bus) }

        while (!NeoCommonInitializer.listeners.isEmpty()) {
            val listeners = NeoCommonInitializer.listeners.toMutableMap()
            NeoCommonInitializer.listeners.clear()
            listeners.forEach { (mod, listeners) ->
                val bus = buses[mod] ?: throw NullPointerException("$mod isn't registered")
                listeners.forEach { it.accept(bus) }
            }
        }

        NeoCommonInitializer.isInitDone = true
    }
}
//? }