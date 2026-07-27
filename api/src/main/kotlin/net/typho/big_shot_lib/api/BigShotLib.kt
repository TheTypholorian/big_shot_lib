package net.typho.big_shot_lib.api

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.typho.big_shot_lib.api.event.NeoEventBus
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object BigShotLib : NeoCommonInitializer {
    const val MOD_ID = "big_shot_lib"
    override val modId: String = MOD_ID
    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger("Big Shot Lib")

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> ResourceKey<out Registry<T>>.lookupOrThrow(error: String = "Couldn't find registry ${identifier()}") = BuiltInRegistries.REGISTRY.get(identifier())?.let { it as Registry<T> } ?: throw NullPointerException(error)

    @JvmStatic
    fun id(path: String): Identifier = Identifier.of(modId, path)

    override fun onInitialize(bus: NeoEventBus) {
    }
}