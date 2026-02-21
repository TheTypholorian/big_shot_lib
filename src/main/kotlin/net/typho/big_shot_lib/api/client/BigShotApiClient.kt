package net.typho.big_shot_lib.api.client

import net.minecraft.client.KeyMapping
import net.typho.big_shot_lib.api.client.registration.BigShotClientRegistrationEntrypoint
import net.typho.big_shot_lib.api.client.registration.KeyMappingFactory
import net.typho.big_shot_lib.api.client.registration.ResourceListenerFactory
import net.typho.big_shot_lib.api.client.registration.events.ClientEventFactory
import net.typho.big_shot_lib.api.client.rendering.shaders.NeoShaderRegistry
import net.typho.big_shot_lib.api.client.rendering.state.GlStateStack
import org.lwjgl.glfw.GLFW.GLFW_KEY_KP_9

object BigShotApiClient : BigShotClientRegistrationEntrypoint {
    @JvmField
    var debugGlStateStacksKey: KeyMapping? = null

    override fun registerReloadListeners(factory: ResourceListenerFactory) {
        factory.register(NeoShaderRegistry)
    }

    override fun registerKeyMappings(factory: KeyMappingFactory) {
        debugGlStateStacksKey = factory.create(
            "key.debugGlStateStacks",
            GLFW_KEY_KP_9,
            factory.debug
        )
    }

    override fun registerEvents(factory: ClientEventFactory) {
        factory.onFrameStart {
            while (debugGlStateStacksKey?.consumeClick() == true) {
                GlStateStack.openDebug()
            }
        }
        factory.onFrameEnd {
            GlStateStack.all.forEach { it.ensureEmpty() }
            GlStateStack.debugOut?.close()
            GlStateStack.debugOut = null
        }
    }
}