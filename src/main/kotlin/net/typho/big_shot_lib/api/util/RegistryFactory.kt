package net.typho.big_shot_lib.api.util

import com.mojang.serialization.Lifecycle
import net.minecraft.core.Registry
import net.typho.big_shot_lib.api.util.resources.NeoResourceKey
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

interface RegistryFactory {
    fun <T : Any> create(location: ResourceIdentifier, lifecycle: Lifecycle = Lifecycle.stable(), isIntrusive: Boolean = false): NeoRegistry<T>

    fun <T : Any> create(key: NeoResourceKey<Registry<T>>, lifecycle: Lifecycle = Lifecycle.stable(), isIntrusive: Boolean = false): NeoRegistry<T> {
        return create(key.location, lifecycle, isIntrusive)
    }

    fun <T : Any> createDefaulted(location: ResourceIdentifier, defaultKey: ResourceIdentifier, lifecycle: Lifecycle = Lifecycle.stable(), isIntrusive: Boolean = false): NeoRegistry<T>

    fun <T : Any> createDefaulted(key: NeoResourceKey<Registry<T>>, defaultKey: ResourceIdentifier, lifecycle: Lifecycle = Lifecycle.stable(), isIntrusive: Boolean = false): NeoRegistry<T> {
        return createDefaulted(key.location, defaultKey, lifecycle, isIntrusive)
    }
}