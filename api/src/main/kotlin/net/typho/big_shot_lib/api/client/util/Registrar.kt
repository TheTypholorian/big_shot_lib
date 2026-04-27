package net.typho.big_shot_lib.api.client.util

import net.minecraft.core.Registry
import net.typho.big_shot_lib.api.util.NeoRegistry
import net.typho.big_shot_lib.api.util.RegisteredObject
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier

interface Registrar {
    fun <T : Any> register(registry: NeoRegistry<out Registry<T>>, id: NeoIdentifier, value: T): RegisteredObject<T>
}