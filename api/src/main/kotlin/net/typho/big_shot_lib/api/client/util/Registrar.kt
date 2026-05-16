package net.typho.big_shot_lib.api.client.util

import net.minecraft.core.Registry
import net.typho.big_shot_lib.api.util.RegisteredObject
import net.typho.big_shot_lib.api.util.resource.NamedResource
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import net.typho.big_shot_lib.api.util.resource.NeoResourceKey

interface Registrar {
    fun <T : Any> createRegistry(id: NeoIdentifier): NeoResourceKey<out Registry<T>>

    fun <T : NamedResource> register(registry: NeoResourceKey<out Registry<T>>, value: T): RegisteredObject<T> = register(registry, value.location, value)

    fun <T : Any> register(registry: NeoResourceKey<out Registry<T>>, id: NeoIdentifier, value: T): RegisteredObject<T>
}