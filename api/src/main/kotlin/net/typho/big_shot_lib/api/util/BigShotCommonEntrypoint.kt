package net.typho.big_shot_lib.api.util

import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadServices
import net.typho.big_shot_lib.api.util.event.CommonEventFactory

interface BigShotCommonEntrypoint {
    fun registerRegistries(factory: RegistryFactory) {
    }

    fun registerContent(factory: RegistrationFactory) {
    }

    fun registerEvents(factory: CommonEventFactory) {
    }

    companion object : BigShotCommonEntrypoint {
        val entrypoints by lazy { BigShotCommonEntrypoint::class.loadServices() }

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