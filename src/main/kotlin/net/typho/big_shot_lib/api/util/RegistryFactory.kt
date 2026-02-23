package net.typho.big_shot_lib.api.util

import com.mojang.serialization.Lifecycle
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

interface RegistryFactory {
    fun <T : Any> create(id: ResourceIdentifier, lifecycle: Lifecycle = Lifecycle.stable(), isIntrusive: Boolean = false): NeoRegistry<T>

    fun <T : Any> createDefaulted(id: ResourceIdentifier, defaultKey: ResourceIdentifier, lifecycle: Lifecycle = Lifecycle.stable(), isIntrusive: Boolean = false): NeoRegistry<T>
}