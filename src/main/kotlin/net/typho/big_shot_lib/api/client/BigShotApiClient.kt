package net.typho.big_shot_lib.api.client

import net.minecraft.client.KeyMapping
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.opengl.shaders.NeoShaderRegistry
import net.typho.big_shot_lib.api.client.opengl.shaders.mixins.ShaderLocationMapperMixin
import net.typho.big_shot_lib.api.client.opengl.shaders.mixins.ShaderVersionUpdaterMixin
import net.typho.big_shot_lib.api.client.opengl.state.GlStateStack
import net.typho.big_shot_lib.api.client.util.BigShotClientEntrypoint
import net.typho.big_shot_lib.api.client.util.KeyMappingFactory
import net.typho.big_shot_lib.api.client.util.ResourceListenerFactory
import net.typho.big_shot_lib.api.client.util.ShaderMixinFactory
import net.typho.big_shot_lib.api.client.util.events.ClientEventFactory
import org.lwjgl.glfw.GLFW.GLFW_KEY_KP_9

object BigShotApiClient : BigShotClientEntrypoint {
    @JvmField
    var debugGlStateStacksKey: KeyMapping? = null

    override fun registerReloadListeners(factory: ResourceListenerFactory) {
        factory.register(NeoShaderRegistry)
    }

    override fun registerKeyMappings(factory: KeyMappingFactory) {
        debugGlStateStacksKey = factory.create(
            BigShotApi.id("debug_gl_state_stacks"),
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

    override fun registerShaderMixins(factory: ShaderMixinFactory) {
        factory.register(ShaderVersionUpdaterMixin)
        factory.register(ShaderLocationMapperMixin)
    }
}