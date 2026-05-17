package net.typho.big_shot_lib.api.client.util

import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.typho.big_shot_lib.api.util.RegisteredObject
import net.typho.big_shot_lib.api.util.resource.NamedResource

interface Registrar {
    fun <T : Any> createRegistry(id: Identifier): ResourceKey<out Registry<T>>

    fun <T : NamedResource> register(registry: ResourceKey<out Registry<T>>, value: T): RegisteredObject<T> = register(registry, value.location, value)

    fun <T : Any> register(registry: ResourceKey<out Registry<T>>, id: Identifier, value: T): RegisteredObject<T>
}