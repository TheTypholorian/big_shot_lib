package net.typho.big_shot_lib.impl

//? fabric {
/*import net.fabricmc.api.ModInitializer
import net.typho.big_shot_lib.api.NeoCommonInitializer

class BigShotCommonInit : ModInitializer {
    override fun onInitialize() {
        for (entrypoint in NeoCommonInitializer.mods) {
            entrypoint.onInitialize(NeoEventBusImpl)
        }
    }
}
*///? } neoforge {
import net.neoforged.fml.common.Mod
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.ModList
import net.typho.big_shot_lib.api.NeoCommonInitializer
import net.typho.big_shot_lib.api.event.NeoEventBus
import net.typho.big_shot_lib.impl.event.NeoEventBusImpl

@Mod(value = "big_shot_lib")
class BigShotCommonInit(eventBus: IEventBus, modContainer: ModContainer) {
    init {
        val buses: MutableMap<String, NeoEventBus> = hashMapOf()

        fun getBus(mod: String): NeoEventBus {
            return buses.computeIfAbsent(mod) {
                NeoEventBusImpl(
                    ModList.get()
                        .getModContainerById(it)
                        .orElseThrow { IllegalArgumentException("Unrecognized mod $it") }
                        .eventBus ?: throw NullPointerException("Cannot get event bus for $it")
                )
            }
        }

        NeoCommonInitializer.mods.forEach { it.onInitialize(getBus(it.modId)) }

        while (!NeoCommonInitializer.listeners.isEmpty()) {
            val listeners = NeoCommonInitializer.listeners.toMutableMap()
            NeoCommonInitializer.listeners.clear()
            listeners.forEach { (mod, listeners) ->
                val bus = getBus(mod)
                listeners.forEach { it.accept(bus) }
            }
        }

        NeoCommonInitializer.isInitDone = true
    }
}
//? }