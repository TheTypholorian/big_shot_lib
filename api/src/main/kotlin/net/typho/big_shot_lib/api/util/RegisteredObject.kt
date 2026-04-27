package net.typho.big_shot_lib.api.util

import net.minecraft.core.Registry
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import net.typho.big_shot_lib.api.util.resource.NeoResourceKey
import kotlin.reflect.KProperty

interface RegisteredObject<T : Any> {
    val registry: NeoResourceKey<out Registry<T>>
    val key: NeoIdentifier
    val value: T

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = value

    operator fun invoke() = value

    fun isRegistered(): Boolean
}