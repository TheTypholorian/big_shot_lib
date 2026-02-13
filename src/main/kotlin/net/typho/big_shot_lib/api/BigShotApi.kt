package net.typho.big_shot_lib.api

import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object BigShotApi {
    const val MOD_ID = "big_shot_lib"
    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger("Big Shot Lib")

    @JvmStatic
    fun id(path: String): ResourceIdentifier = ResourceIdentifier(MOD_ID, path)
}