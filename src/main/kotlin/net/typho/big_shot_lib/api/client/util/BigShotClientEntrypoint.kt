package net.typho.big_shot_lib.api.client.util

import net.typho.big_shot_lib.api.BigShotApi.loadServices
import net.typho.big_shot_lib.api.client.util.events.ClientEventFactory

interface BigShotClientEntrypoint {
    fun registerReloadListeners(factory: ResourceListenerFactory) {
    }

    fun registerKeyMappings(factory: KeyMappingFactory) {
    }

    fun registerEvents(factory: ClientEventFactory) {
    }

    fun registerDebugScreenInfo(factory: DebugScreenFactory) {
    }

    fun registerDynamicBuffers(factory: DynamicBufferFactory) {
    }

    fun registerShaderMixins(factory: ShaderMixinFactory) {
    }

    companion object : BigShotClientEntrypoint {
        @JvmField
        val entrypoints = BigShotClientEntrypoint::class.loadServices()

        override fun registerReloadListeners(factory: ResourceListenerFactory) {
            entrypoints.forEach { it.registerReloadListeners(factory) }
        }

        override fun registerKeyMappings(factory: KeyMappingFactory) {
            entrypoints.forEach { it.registerKeyMappings(factory) }
        }

        override fun registerEvents(factory: ClientEventFactory) {
            entrypoints.forEach { it.registerEvents(factory) }
        }

        override fun registerDebugScreenInfo(factory: DebugScreenFactory) {
            entrypoints.forEach { it.registerDebugScreenInfo(factory) }
        }

        override fun registerDynamicBuffers(factory: DynamicBufferFactory) {
            entrypoints.forEach { it.registerDynamicBuffers(factory) }
        }

        override fun registerShaderMixins(factory: ShaderMixinFactory) {
            entrypoints.forEach { it.registerShaderMixins(factory) }
        }
    }
}