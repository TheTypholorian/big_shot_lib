package net.typho.big_shot_lib

import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.typho.big_shot_lib.api.util.resources.NeoResourceKey
import net.typho.big_shot_lib.api.util.resources.NeoTagKey
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

object BigShotLib {
    @JvmStatic
    fun ResourceLocation.toNeo(): ResourceIdentifier = ResourceIdentifier(namespace, path)

    @JvmStatic
    fun ResourceIdentifier.toMojang(): ResourceLocation = ResourceLocation.fromNamespaceAndPath(namespace, path)

    @JvmStatic
    fun <T> ResourceKey<T>.toNeo(): NeoResourceKey<T> = NeoResourceKey(registry().toNeo(), location().toNeo())

    @JvmStatic
    fun <T> NeoResourceKey<T>.toMojang(): ResourceKey<T> = ResourceKey.create(ResourceKey.createRegistryKey(registry.toMojang()), location.toMojang())

    @JvmStatic
    fun <T> TagKey<T>.toNeo(): NeoTagKey<T> = NeoTagKey(registry().toNeo().location, location().toNeo())

    @JvmStatic
    fun <T> NeoTagKey<T>.toMojang(): TagKey<T> = TagKey.create(ResourceKey.createRegistryKey(registry.toMojang()), location.toMojang())

    @JvmStatic
    fun init() {
    }
}