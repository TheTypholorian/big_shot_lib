package net.typho.big_shot_lib.api.util.content

import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.typho.big_shot_lib.api.event.NeoEventBus

interface ContentFactory<T : Any> {
    val registry: ResourceKey<Registry<T>>

    fun begin(key: ResourceKey<T>): ObjectBuilder<RegisteredObject<T>>

    fun end(bus: NeoEventBus)
}