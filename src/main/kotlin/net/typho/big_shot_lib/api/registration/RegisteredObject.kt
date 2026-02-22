package net.typho.big_shot_lib.api.registration

import net.minecraft.core.Registry
import net.typho.big_shot_lib.api.util.resources.NeoResourceKey
import net.typho.big_shot_lib.api.util.resources.ResourceIdentifier

interface RegisteredObject<T> {
    val registry: NeoResourceKey<Registry<T>>
    val key: ResourceIdentifier

    fun get(): T

    fun isRegistered(): Boolean
}