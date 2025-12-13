package net.typho.big_shot_lib

import net.minecraft.resources.ResourceLocation
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object BigShotLib {
    const val MOD_ID = "big_shot_lib"
    val LOGGER: Logger = LoggerFactory.getLogger("Big Shot Lib")

    fun init() {
    }

    fun id(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, path)
}