package net.typho.big_shot_lib.api.registration

import com.mojang.serialization.Lifecycle
import net.minecraft.core.Registry
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

interface RegistryFactory {
    fun <T> create(id: ResourceIdentifier, lifecycle: Lifecycle = Lifecycle.stable()): Registry<T>

    fun <T> createDefaulted(id: ResourceIdentifier, defaultKey: ResourceIdentifier, lifecycle: Lifecycle = Lifecycle.stable()): Registry<T>
}