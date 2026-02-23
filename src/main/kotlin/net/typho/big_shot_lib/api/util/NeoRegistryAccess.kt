package net.typho.big_shot_lib.api.util

import net.minecraft.core.Registry
import net.typho.big_shot_lib.api.util.resources.NeoResourceKey

interface NeoRegistryAccess {
    fun <T : Any> registry(key: NeoResourceKey<Registry<T>>): NeoRegistry<T>?
}