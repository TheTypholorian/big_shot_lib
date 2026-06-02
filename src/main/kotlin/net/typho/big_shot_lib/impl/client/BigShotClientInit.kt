package net.typho.big_shot_lib.impl.client

//? fabric {
import net.fabricmc.api.ClientModInitializer
import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint

class BigShotClientInit : ClientModInitializer {
    override fun onInitializeClient() {
        for (entrypoint in BigShotClientEntrypoint.entrypoints) {
            entrypoint.onInitializeClient(NeoClientEventBusImpl)
        }
    }
}
//? } neoforge {
/*import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderType
import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint

@Mod(value = BigShotApi.MOD_ID, dist = [Dist.CLIENT])
class BigShotClientInit(eventBus: IEventBus, modContainer: ModContainer) {
    init {
        for (entrypoint in BigShotClientEntrypoint.entrypoints) {
            entrypoint.onInitializeClient()
        }

        BigShotClientEvents.init()

        val type = NeoRenderType.BUILTINS.entityTranslucent(Identifier.of("test", "dummy"), true)
        BigShotApi.LOGGER.info("Entity Translucent: ${type.format} ${type.blend} ${type.writeColor} ${type.writeDepth} ${type.cull} ${type.depth} ${type.layering} ${type.lightmap} ${type.overlay} ${type.defaultBufferSize}")
    }
}
*///? }