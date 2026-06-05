package net.typho.big_shot_lib.impl.client

//? fabric {
/*import net.fabricmc.api.ClientModInitializer
import net.typho.big_shot_lib.api.client.util.NeoClientInitializer

class BigShotClientInit : ClientModInitializer {
    override fun onInitialize() {
        for (entrypoint in NeoClientInitializer.entrypoints) {
            entrypoint.onInitialize(NeoClientEventBusImpl)
        }
    }
}
*///? } neoforge {
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.ModList
import net.neoforged.fml.common.Mod
import net.typho.big_shot_lib.api.BigShotLibMod

@Mod(value = "big_shot_lib", dist = [Dist.CLIENT])
class BigShotClientInit(eventBus: IEventBus, modContainer: ModContainer) {
    init {
        val buses = BigShotLibMod.mods.associateWith {
            NeoClientEventBusImpl(
                ModList.get()
                    .getModContainerById(it.modId)
                    .orElseThrow { IllegalArgumentException("Unrecognized mod ${it.modId}, $it says it's part of it") }
                    .eventBus ?: throw NullPointerException("Cannot get event bus for ${it.modId}")
            )
        }

        buses.forEach { (init, bus) -> init.onInitializeClient(bus) }

        while (!BigShotLibMod.clientListeners.isEmpty()) {
            val listeners = BigShotLibMod.clientListeners.toMutableMap()
            BigShotLibMod.clientListeners.clear()
            listeners.forEach { (mod, listeners) ->
                val bus = buses[mod] ?: throw NullPointerException("$mod isn't registered")
                listeners.forEach { it.accept(bus) }
            }
        }

        BigShotLibMod.isInitDone = true
    }
}
//? }