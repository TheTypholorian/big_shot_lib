package net.typho.big_shot_lib.api.registration

import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.registration.events.CommonEventFactory
import net.typho.big_shot_lib.api.services.PlatformUtil

interface BigShotCommonRegistrationEntrypoint {
    fun registerRegistries(factory: RegistryFactory)

    fun registerContent(factory: RegistrationFactory)

    fun registerEvents(factory: CommonEventFactory)

    companion object : BigShotCommonRegistrationEntrypoint {
        @JvmField
        val id = BigShotApi.id("common_events")
        @JvmField
        val entrypoints = PlatformUtil.INSTANCE.mods.mapNotNull { it.loadEntrypoint(id, BigShotCommonRegistrationEntrypoint::class.java) }

        override fun registerRegistries(factory: RegistryFactory) {
            entrypoints.forEach { it.registerRegistries(factory) }
        }

        override fun registerContent(factory: RegistrationFactory) {
            entrypoints.forEach { it.registerContent(factory) }
        }

        override fun registerEvents(factory: CommonEventFactory) {
            entrypoints.forEach { it.registerEvents(factory) }
        }
    }
}