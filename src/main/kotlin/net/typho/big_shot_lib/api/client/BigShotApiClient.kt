package net.typho.big_shot_lib.api.client

import net.typho.big_shot_lib.api.client.opengl.shaders.NeoShaderRegistry
import net.typho.big_shot_lib.api.client.opengl.state.GlStateManager
import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint
import net.typho.big_shot_lib.api.client.util.ResourceListenerFactory
import net.typho.big_shot_lib.api.client.util.events.ClientEventFactory

class BigShotApiClient : BigShotClientEntrypoint {
    override fun registerReloadListeners(factory: ResourceListenerFactory) {
        factory.register(NeoShaderRegistry)
    }

    override fun registerEvents(factory: ClientEventFactory) {
        factory.onFrameEnd {
            GlStateManager.all.forEach { it.ensureEmpty() }
        }
    }
}