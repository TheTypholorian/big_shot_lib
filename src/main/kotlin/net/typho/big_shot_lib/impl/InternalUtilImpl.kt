package net.typho.big_shot_lib.impl

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTab
import net.typho.big_shot_lib.api.InternalUtil
import net.typho.big_shot_lib.api.event.RegistryBuilder

object InternalUtilImpl : InternalUtil {
    override fun <T> createRegistryBuilder(key: ResourceKey<Registry<T>>): RegistryBuilder<T> {
        return RegistryBuilderImpl(key)
    }

    override fun createCreativeTabBuilder(): CreativeModeTab.Builder {
        return CreativeModeTab.builder()
    }
}