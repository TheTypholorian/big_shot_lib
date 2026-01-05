package net.typho.big_shot_lib.platform.services

interface PlatformHelper {
    fun isDevelopmentEnvironment(): Boolean

    fun shaderMixinsEnabled(): Boolean
}