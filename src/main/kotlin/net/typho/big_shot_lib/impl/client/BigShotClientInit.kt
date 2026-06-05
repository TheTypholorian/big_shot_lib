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
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.util.NeoClientInitializer
import net.typho.big_shot_lib.api.util.NeoCommonInitializer
import net.typho.big_shot_lib.impl.NeoEventBusImpl
import kotlin.collections.component1
import kotlin.collections.component2

@Mod(value = BigShotApi.MOD_ID, dist = [Dist.CLIENT])
class BigShotClientInit(eventBus: IEventBus, modContainer: ModContainer) {
    init {
        val buses = NeoClientInitializer.entrypoints.associateWith {
            NeoClientEventBusImpl(
                ModList.get()
                    .getModContainerById(it.modId)
                    .orElseThrow { IllegalArgumentException("Unrecognized mod ${it.modId}, $it says it's part of it") }
                    .eventBus ?: throw NullPointerException("Cannot get event bus for ${it.modId}")
            )
        }

        buses.forEach { (init, bus) -> init.onInitialize(bus) }

        while (!NeoClientInitializer.listeners.isEmpty()) {
            val listeners = NeoClientInitializer.listeners.toMutableMap()
            NeoClientInitializer.listeners.clear()
            listeners.forEach { (mod, listeners) ->
                val bus = buses[mod] ?: throw NullPointerException("$mod isn't registered")
                listeners.forEach { it.accept(bus) }
            }
        }

        NeoClientInitializer.isInitDone = true
    }
}
//? }