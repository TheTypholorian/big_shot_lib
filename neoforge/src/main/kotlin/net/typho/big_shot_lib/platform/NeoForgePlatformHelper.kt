package net.typho.big_shot_lib.platform

import net.neoforged.fml.ModList
import net.neoforged.fml.loading.FMLLoader
import net.typho.big_shot_lib.BigShotLib
import net.typho.big_shot_lib.platform.services.PlatformHelper

class NeoForgePlatformHelper : PlatformHelper {
    override fun isDevelopmentEnvironment(): Boolean {
        return !FMLLoader.isProduction()
    }

    override fun shaderMixinsEnabled(): Boolean {
        for (mod in ModList.get().mods) {
            mod.modProperties[BigShotLib.id("require_shader_mixins").toString()]?.let {
                if (it as Boolean) {
                    return true
                }
            }
        }

        return false
    }
}