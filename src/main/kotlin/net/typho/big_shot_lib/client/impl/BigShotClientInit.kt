package net.typho.big_shot_lib.client.impl

//? fabric {
import net.fabricmc.api.ClientModInitializer
import net.typho.big_shot_lib.client.api.NeoClientInitializer

class BigShotClientInit : ClientModInitializer {
    override fun onInitializeClient() {
        for (entrypoint in NeoClientInitializer.mods) {
            entrypoint.onInitializeClient(NeoClientEventBusImpl)
        }
    }
}
//? } neoforge {
/*import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.ModList
import net.neoforged.fml.common.Mod
import net.typho.big_shot_lib.client.api.NeoClientInitializer
import net.typho.big_shot_lib.api.event.NeoClientEventBus

@Mod(value = "big_shot_lib", dist = [Dist.CLIENT])
class BigShotClientInit(eventBus: IEventBus, modContainer: ModContainer) {
    init {
        val buses: MutableMap<String, NeoClientEventBus> = hashMapOf()

        fun getBus(mod: String): NeoClientEventBus {
            return buses.computeIfAbsent(mod) {
                NeoClientEventBusImpl(
                    ModList.get()
                        .getModContainerById(it)
                        .orElseThrow { IllegalArgumentException("Unrecognized mod $it") }
                        .eventBus ?: throw NullPointerException("Cannot get event bus for $it")
                )
            }
        }

        NeoClientInitializer.mods.forEach { it.onInitializeClient(getBus(it.modId)) }

        while (!NeoClientInitializer.clientListeners.isEmpty()) {
            val listeners = NeoClientInitializer.clientListeners.toMutableMap()
            NeoClientInitializer.clientListeners.clear()
            listeners.forEach { (mod, listeners) ->
                val bus = getBus(mod)
                listeners.forEach { it.accept(bus) }
            }
        }

        NeoClientInitializer.isClientInitDone = true
    }
}
*///? }