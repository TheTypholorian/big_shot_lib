package net.typho.big_shot_lib.api.client

import net.typho.big_shot_lib.api.client.opengl.shaders.NeoShaderRegistry
import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint
import net.typho.big_shot_lib.api.client.util.ResourceListenerFactory

class BigShotApiClient : BigShotClientEntrypoint {
    override fun registerReloadListeners(factory: ResourceListenerFactory) {
        factory.register(NeoShaderRegistry)
    }
}