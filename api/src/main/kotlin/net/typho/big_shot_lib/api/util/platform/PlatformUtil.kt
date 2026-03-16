package net.typho.big_shot_lib.api.util.platform

import net.typho.big_shot_lib.api.BigShotApi.loadService

interface PlatformUtil {
    val loader: ModLoader
    val mods: Collection<ModContainer>

    fun isDevEnv(): Boolean

    companion object {
        @JvmField
        val INSTANCE: PlatformUtil = PlatformUtil::class.loadService()
    }
}