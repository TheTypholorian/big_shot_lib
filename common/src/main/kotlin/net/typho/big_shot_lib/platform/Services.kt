package net.typho.big_shot_lib.platform

import net.typho.big_shot_lib.platform.services.PlatformHelper
import java.util.*

object Services {
    @JvmField
    val PLATFORM = load(PlatformHelper::class.java)

    @JvmStatic
    fun <T> load(clazz: Class<T>): T {
        val loadedService = ServiceLoader.load(clazz)
            .findFirst()
            .orElseThrow {
                IllegalStateException("Failed to load service for ${clazz.name}")
            }
        return loadedService
    }
}