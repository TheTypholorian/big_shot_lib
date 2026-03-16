package net.typho.big_shot_lib.api.client

import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint
import net.typho.big_shot_lib.api.client.util.ResourceListenerFactory

object BigShotApiClient : BigShotClientEntrypoint {
    override fun registerReloadListeners(factory: ResourceListenerFactory) {
        // TODO
        //factory.register(NeoShaderRegistry)
    }
}