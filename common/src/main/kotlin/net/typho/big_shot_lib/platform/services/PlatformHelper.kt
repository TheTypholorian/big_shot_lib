package net.typho.big_shot_lib.platform.services

import net.minecraft.resources.ResourceLocation

interface PlatformHelper {
    fun isDevelopmentEnvironment(): Boolean

    fun isFlagEnabled(id: ResourceLocation): Boolean
}