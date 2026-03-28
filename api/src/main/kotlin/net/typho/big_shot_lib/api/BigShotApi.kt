package net.typho.big_shot_lib.api

import net.typho.big_shot_lib.api.util.NeoServiceLoader
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName

object BigShotApi {
    const val MOD_ID = "big_shot_lib"
    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger("Big Shot Lib")

    @JvmStatic
    fun id(path: String): NeoIdentifier = NeoIdentifier(MOD_ID, path)

    @JvmStatic
    fun <T : Any> KClass<T>.loadService(): T = NeoServiceLoader.load(java).firstOrNull() ?: throw IllegalStateException("Could not find service implementation for $jvmName")

    @JvmStatic
    fun <T : Any> KClass<T>.loadServices(): List<T> = NeoServiceLoader.load(java)
}