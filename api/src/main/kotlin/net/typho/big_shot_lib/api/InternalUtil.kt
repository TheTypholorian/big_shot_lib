package net.typho.big_shot_lib.api

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTab
import net.typho.big_shot_lib.api.event.RegistryBuilder
import net.typho.big_shot_lib.api.util.NeoServiceLoader.loadService

interface InternalUtil {
    fun <T> createRegistryBuilder(
        key: ResourceKey<Registry<T>>
    ): RegistryBuilder<T>

    fun createCreativeTabBuilder(): CreativeModeTab.Builder

    companion object {
        @JvmStatic
        @get:JvmName("getInstance")
        val INSTANCE by lazy { InternalUtil::class.loadService() }
    }
}