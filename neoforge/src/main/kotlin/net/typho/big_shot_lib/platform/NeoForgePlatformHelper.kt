package net.typho.big_shot_lib.platform

import net.minecraft.resources.ResourceLocation
import net.neoforged.fml.ModList
import net.neoforged.fml.loading.FMLLoader
import net.typho.big_shot_lib.platform.services.PlatformHelper

class NeoForgePlatformHelper : PlatformHelper {
    override fun isDevelopmentEnvironment(): Boolean {
        return !FMLLoader.isProduction()
    }

    override fun isFlagEnabled(id: ResourceLocation): Boolean {
        for (mod in ModList.get().mods) {
            mod.modProperties[id.toString()]?.let {
                if (it as Boolean) {
                    return true
                }
            }
        }

        return false
    }
}