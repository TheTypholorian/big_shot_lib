package net.typho.big_shot_lib.api.util

import net.minecraft.core.Registry
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import net.typho.big_shot_lib.api.util.resource.NeoResourceKey

interface RegisteredObject<T : Any> {
    val registry: NeoResourceKey<Registry<T>>
    val key: NeoIdentifier

    fun get(): T

    fun isRegistered(): Boolean
}