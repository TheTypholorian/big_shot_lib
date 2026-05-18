package net.typho.big_shot_lib.api

import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.typho.big_shot_lib.api.util.NeoRegistry
import net.typho.big_shot_lib.api.util.WrapperUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object BigShotApi {
    const val MOD_ID = "big_shot_lib"
    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger("Big Shot Lib")

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> ResourceKey<out Registry<T>>.lookupOrThrow(error: String = "Couldn't find registry ${location()}") = NeoRegistry.REGISTRY.get(location())?.let { WrapperUtil.INSTANCE.wrap(it) as NeoRegistry<T> } ?: throw NullPointerException(error)

    @JvmStatic
    fun Identifier.toShortString() = if (namespace == Identifier.DEFAULT_NAMESPACE) path else toString()

    @JvmStatic
    fun id(path: String): Identifier = Identifier.of(MOD_ID, path)
}