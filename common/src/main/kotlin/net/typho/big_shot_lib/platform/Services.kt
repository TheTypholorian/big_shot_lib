package net.typho.big_shot_lib.platform

import net.typho.big_shot_lib.platform.services.PlatformHelper
import java.util.*

object Services {
    val PLATFORM = load(PlatformHelper::class.java)

    fun <T> load(clazz: Class<T>): T {
        val loadedService = ServiceLoader.load(clazz)
            .findFirst()
            .orElseThrow {
                IllegalStateException("Failed to load service for ${clazz.name}")
            }
        return loadedService
    }
}