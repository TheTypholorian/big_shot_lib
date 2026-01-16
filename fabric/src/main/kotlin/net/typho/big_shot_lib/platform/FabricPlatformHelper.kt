package net.typho.big_shot_lib.platform

import net.fabricmc.loader.api.FabricLoader
import net.minecraft.resources.ResourceLocation
import net.typho.big_shot_lib.platform.services.PlatformHelper

class FabricPlatformHelper : PlatformHelper {
    override fun isDevelopmentEnvironment(): Boolean {
        return FabricLoader.getInstance().isDevelopmentEnvironment
    }

    override fun isFlagEnabled(id: ResourceLocation): Boolean {
        for (mod in FabricLoader.getInstance().allMods) {
            mod.metadata.getCustomValue(id.toString())?.let {
                if (it.asBoolean) {
                    return true
                }
            }
        }

        return false
    }
}