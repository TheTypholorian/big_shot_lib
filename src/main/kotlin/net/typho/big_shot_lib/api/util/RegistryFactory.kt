package net.typho.big_shot_lib.api.util

import com.mojang.serialization.Lifecycle
import net.minecraft.core.Registry
import net.typho.big_shot_lib.api.util.resources.NeoIdentifier
import net.typho.big_shot_lib.api.util.resources.NeoResourceKey

interface RegistryFactory {
    fun <T : Any> create(location: NeoIdentifier, lifecycle: Lifecycle = Lifecycle.stable(), isIntrusive: Boolean = false): NeoRegistry<T>

    fun <T : Any> create(key: NeoResourceKey<Registry<T>>, lifecycle: Lifecycle = Lifecycle.stable(), isIntrusive: Boolean = false): NeoRegistry<T> {
        return create(key.location, lifecycle, isIntrusive)
    }

    fun <T : Any> createDefaulted(location: NeoIdentifier, defaultKey: NeoIdentifier, lifecycle: Lifecycle = Lifecycle.stable(), isIntrusive: Boolean = false): NeoRegistry<T>

    fun <T : Any> createDefaulted(key: NeoResourceKey<Registry<T>>, defaultKey: NeoIdentifier, lifecycle: Lifecycle = Lifecycle.stable(), isIntrusive: Boolean = false): NeoRegistry<T> {
        return createDefaulted(key.location, defaultKey, lifecycle, isIntrusive)
    }
}