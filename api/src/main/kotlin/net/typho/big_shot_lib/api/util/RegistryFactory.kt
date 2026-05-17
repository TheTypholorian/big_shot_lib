package net.typho.big_shot_lib.api.util

import com.mojang.serialization.Lifecycle
import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

interface RegistryFactory {
    fun <T : Any> create(location: Identifier, lifecycle: Lifecycle = Lifecycle.stable(), isIntrusive: Boolean = false): NeoRegistry<T>

    fun <T : Any> create(key: ResourceKey<Registry<T>>, lifecycle: Lifecycle = Lifecycle.stable(), isIntrusive: Boolean = false): NeoRegistry<T> {
        return create(key.location(), lifecycle, isIntrusive)
    }

    fun <T : Any> createDefaulted(location: Identifier, defaultKey: Identifier, lifecycle: Lifecycle = Lifecycle.stable(), isIntrusive: Boolean = false): NeoRegistry<T>

    fun <T : Any> createDefaulted(key: ResourceKey<Registry<T>>, defaultKey: Identifier, lifecycle: Lifecycle = Lifecycle.stable(), isIntrusive: Boolean = false): NeoRegistry<T> {
        return createDefaulted(key.location(), defaultKey, lifecycle, isIntrusive)
    }
}