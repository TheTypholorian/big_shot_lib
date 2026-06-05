package net.typho.big_shot_lib.api.util.content

import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.typho.big_shot_lib.api.client.event.NeoClientEventBus
import net.typho.big_shot_lib.api.event.NeoEventBus
import net.typho.big_shot_lib.api.plugin.Environment
import net.typho.big_shot_lib.api.plugin.OnlyIn

interface ContentFactory<T : Any> {
    val registry: ResourceKey<Registry<T>>

    fun begin(key: Identifier): ObjectBuilder<RegisteredObject<T>>

    fun end(bus: NeoEventBus) {
    }

    @OnlyIn(Environment.CLIENT)
    fun endClient(bus: NeoClientEventBus) {
    }
}