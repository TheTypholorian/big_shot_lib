package net.typho.big_shot_lib.impl

//? if <1.21.11 {
import net.minecraft.resources.ResourceLocation
//? } else {
/*import net.minecraft.resources.Identifier
*///? }

import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import net.typho.big_shot_lib.api.util.resource.NeoResourceKey
import net.typho.big_shot_lib.api.util.resource.NeoTagKey

//? if <1.21.11 {
val ResourceLocation.neo: NeoIdentifier
    get() = NeoIdentifier(namespace, path)
//? } else {
/*val Identifier.neo: NeoIdentifier
    get() = NeoIdentifier(namespace, path)
*///? }

//? if >=1.21.11 {
/*val NeoIdentifier.mojang: Identifier
    get() = Identifier.fromNamespaceAndPath(namespace, path)
*///? } else if >=1.21 {
val NeoIdentifier.mojang: ResourceLocation
    get() = ResourceLocation.fromNamespaceAndPath(namespace, path)
//? } else {
/*val NeoIdentifier.mojang: ResourceLocation
    get() = ResourceLocation(namespace, path)
*///? }

val <T : Any> ResourceKey<T>.neo: NeoResourceKey<T>
    //? if <1.21.11 {
    get() = NeoResourceKey(registry().neo, location().neo)
    //? } else {
    /*get() = NeoResourceKey(registry().neo, identifier().neo)
    *///? }

val <T : Any> NeoResourceKey<T>.mojang: ResourceKey<T>
    get() = ResourceKey.create(ResourceKey.createRegistryKey(registry.mojang), location.mojang)

val <T : Any> TagKey<T>.neo: NeoTagKey<T>
    get() = NeoTagKey(registry().neo.location, location().neo)

val <T : Any> NeoTagKey<T>.mojang: TagKey<T>
    get() = TagKey.create(ResourceKey.createRegistryKey(registry.mojang), location.mojang)
