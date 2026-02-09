package net.typho.big_shot_lib.api.util

import net.minecraft.resources.ResourceLocation
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object BigShotModUtil {
    const val MOD_ID = "big_shot_lib"
    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger("Big Shot Lib")

    @JvmStatic
    fun id(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, path)
}