package net.typho.big_shot_lib.impl

import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import net.typho.big_shot_lib.api.util.resource.NeoResourceKey
import net.typho.big_shot_lib.api.util.resource.NeoTagKey

val ResourceLocation.neo: NeoIdentifier
    get() = NeoIdentifier(namespace, path)

val NeoIdentifier.mojang: ResourceLocation
    //? if >=1.21 {
    /*get() = ResourceLocation.fromNamespaceAndPath(namespace, path)
    *///? } else {
    get() = ResourceLocation(namespace, path)
    //? }

val <T : Any> ResourceKey<T>.neo: NeoResourceKey<T>
    get() = NeoResourceKey(registry().neo, location().neo)

val <T : Any> NeoResourceKey<T>.mojang: ResourceKey<T>
    get() = ResourceKey.create(ResourceKey.createRegistryKey(registry.mojang), location.mojang)

val <T : Any> TagKey<T>.neo: NeoTagKey<T>
    get() = NeoTagKey(registry().neo.location, location().neo)

val <T : Any> NeoTagKey<T>.mojang: TagKey<T>
    get() = TagKey.create(ResourceKey.createRegistryKey(registry.mojang), location.mojang)
