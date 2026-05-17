package net.typho.big_shot_lib.api.util

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey

interface NeoRegistryAccess {
    fun <T : Any> registry(key: ResourceKey<Registry<T>>): NeoRegistry<T>?
}