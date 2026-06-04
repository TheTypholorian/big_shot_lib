package net.typho.big_shot_lib.api.util.resource

import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

interface RegisteredResource<T : Any> : NamedResource {
    val key: ResourceKey<T>
    override val location: Identifier
        get() = key.location()
}