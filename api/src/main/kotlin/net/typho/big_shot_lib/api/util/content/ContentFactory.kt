package net.typho.big_shot_lib.api.util.content

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey

interface ContentFactory<T : Any> {
    val registry: ResourceKey<Registry<T>>
}