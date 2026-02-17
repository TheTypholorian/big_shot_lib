package net.typho.big_shot_lib.api.util

import net.typho.big_shot_lib.api.util.resources.NeoResourceKey
import net.typho.big_shot_lib.api.util.resources.NeoTagKey
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

interface NeoRegistry<T> {
    fun key(): NeoResourceKey<T>

    fun get(value: ResourceIdentifier): T?

    fun get(value: NeoResourceKey<T>) = get(value.location)

    fun getKey(value: T): NeoResourceKey<T>

    fun contains(value: ResourceIdentifier): Boolean

    fun contains(value: NeoResourceKey<T>) = contains(value.location)

    fun keys(): Set<NeoResourceKey<T>>

    fun entries(): Set<Pair<NeoResourceKey<T>, T>>

    fun values(): Collection<T>

    fun getTag(key: NeoTagKey<T>): Set<T>?

    fun tags(): Set<NeoTagKey<T>>
}