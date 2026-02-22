package net.typho.big_shot_lib.api.registration

import com.mojang.serialization.Lifecycle
import net.typho.big_shot_lib.api.util.NeoRegistry
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

interface RegistryFactory {
    fun <T> create(id: ResourceIdentifier, lifecycle: Lifecycle = Lifecycle.stable(), isIntrusive: Boolean = false): NeoRegistry<T>

    fun <T> createDefaulted(id: ResourceIdentifier, defaultKey: ResourceIdentifier, lifecycle: Lifecycle = Lifecycle.stable(), isIntrusive: Boolean = false): NeoRegistry<T>
}