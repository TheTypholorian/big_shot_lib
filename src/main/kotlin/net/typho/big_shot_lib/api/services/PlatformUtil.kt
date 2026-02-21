package net.typho.big_shot_lib.api.services

import net.typho.big_shot_lib.api.BigShotApi.loadService
import net.typho.big_shot_lib.api.util.ModLoader

interface PlatformUtil {
    val loader: ModLoader
    val mods: Collection<ModContainer>

    companion object {
        @JvmField
        val INSTANCE: PlatformUtil = PlatformUtil::class.loadService()
    }
}