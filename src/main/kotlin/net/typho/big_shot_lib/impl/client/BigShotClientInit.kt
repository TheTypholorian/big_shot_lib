package net.typho.big_shot_lib.impl.client

import net.minecraft.resources.Identifier
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
import net.typho.big_shot_lib.api.client.rendering.util.NeoRenderType
import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint

@Mod(value = BigShotApi.MOD_ID, dist = [Dist.CLIENT])
class BigShotClientInit(eventBus: IEventBus, modContainer: ModContainer) {
    init {
        for (entrypoint in BigShotClientEntrypoint.entrypoints) {
            entrypoint.onInitializeClient()
        }

        BigShotClientEvents.init()
        //eventBus.register(BigShotClientEvents.ScrewYouNeoforge())

        val type = NeoRenderType.BUILTINS.entityTranslucent(Identifier.of("test", "dummy"), true)
        BigShotApi.LOGGER.info("Entity Translucent: ${type.format} ${type.drawState.blend} ${type.drawState.colorMask} ${type.drawState.cull} ${type.drawState.depth} ${type.drawState.layering} ${type.drawState.lightmap} ${type.drawState.overlay} ${type.drawState.shader} ${type.defaultBufferSize}")
    }
}
//? }