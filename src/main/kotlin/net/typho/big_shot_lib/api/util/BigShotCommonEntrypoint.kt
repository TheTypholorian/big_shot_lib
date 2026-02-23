package net.typho.big_shot_lib.api.util

import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.util.events.CommonEventFactory
import net.typho.big_shot_lib.api.util.platform.PlatformUtil

interface BigShotCommonEntrypoint {
    fun registerRegistries(factory: RegistryFactory) {
    }

    fun registerContent(factory: RegistrationFactory) {
    }

    fun registerEvents(factory: CommonEventFactory) {
    }

    companion object : BigShotCommonEntrypoint {
        @JvmField
        val id = BigShotApi.id("common")
        @JvmField
        val entrypoints = PlatformUtil.INSTANCE.mods.mapNotNull { it.loadEntrypoint(id, BigShotCommonEntrypoint::class.java) }

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