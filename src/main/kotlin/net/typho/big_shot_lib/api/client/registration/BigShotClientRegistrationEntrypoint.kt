package net.typho.big_shot_lib.api.client.registration

import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.client.registration.events.ClientEventFactory
import net.typho.big_shot_lib.api.services.PlatformUtil

interface BigShotClientRegistrationEntrypoint {
    fun registerReloadListeners(factory: ResourceListenerFactory)

    fun registerKeyMappings(factory: KeyMappingFactory)

    fun registerEvents(factory: ClientEventFactory)

    companion object : BigShotClientRegistrationEntrypoint {
        @JvmField
        val id = BigShotApi.id("client_events")
        @JvmField
        val entrypoints = PlatformUtil.INSTANCE.mods.mapNotNull { it.loadEntrypoint<BigShotClientRegistrationEntrypoint>(id) }

        override fun registerReloadListeners(factory: ResourceListenerFactory) {
            entrypoints.forEach { it.registerReloadListeners(factory) }
        }

        override fun registerKeyMappings(factory: KeyMappingFactory) {
            entrypoints.forEach { it.registerKeyMappings(factory) }
        }

        override fun registerEvents(factory: ClientEventFactory) {
            entrypoints.forEach { it.registerEvents(factory) }
        }
    }
}