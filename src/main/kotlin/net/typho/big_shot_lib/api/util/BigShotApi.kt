package net.typho.big_shot_lib.api.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object BigShotApi {
    const val MOD_ID = "big_shot_lib"
    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger("Big Shot Lib")

    @JvmStatic
    fun id(path: String): ResourceIdentifier = ResourceIdentifier.fromNamespaceAndPath(MOD_ID, path)

    @JvmStatic
    fun ResourceIdentifier.equals(namespace: String, path: String): Boolean {
        return this.namespace == namespace && this.path == path
    }
}