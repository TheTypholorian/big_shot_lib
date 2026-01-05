package net.typho.big_shot_lib.platform

import net.fabricmc.loader.api.FabricLoader
import net.typho.big_shot_lib.BigShotLib
import net.typho.big_shot_lib.platform.services.PlatformHelper

class FabricPlatformHelper : PlatformHelper {
    override fun isDevelopmentEnvironment(): Boolean {
        return FabricLoader.getInstance().isDevelopmentEnvironment
    }

    override fun shaderMixinsEnabled(): Boolean {
        for (mod in FabricLoader.getInstance().allMods) {
            mod.metadata.getCustomValue(BigShotLib.id("require_shader_mixins").toString())?.let {
                if (it.asBoolean) {
                    return true
                }
            }
        }

        return false
    }
}