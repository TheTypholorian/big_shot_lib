package net.typho.big_shot_lib.api

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.typho.big_shot_lib.api.event.NeoClientEventBus
import net.typho.big_shot_lib.api.client.rendering.NeoShaderLoader
import net.typho.big_shot_lib.api.event.NeoEventBus
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object BigShotLib : NeoCommonInitializer {
    override val modId: String = "big_shot_lib"
    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger("Big Shot Lib")

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> ResourceKey<out Registry<T>>.lookupOrThrow(error: String = "Couldn't find registry ${location()}") = BuiltInRegistries.REGISTRY.get(location())?.let { it as Registry<T> } ?: throw NullPointerException(error)

    @JvmStatic
    fun Identifier.toShortString(): String = if (namespace == Identifier.DEFAULT_NAMESPACE) path else toString()

    @JvmStatic
    fun Identifier.toString(delimiter: Char): String = "$namespace$delimiter$path"

    @JvmStatic
    fun Identifier.toShortString(delimiter: Char): String = if (namespace == Identifier.DEFAULT_NAMESPACE) path else toString(delimiter)

    @JvmStatic
    fun id(path: String): Identifier = Identifier.of(modId, path)

    override fun onInitialize(bus: NeoEventBus) {
    }
}