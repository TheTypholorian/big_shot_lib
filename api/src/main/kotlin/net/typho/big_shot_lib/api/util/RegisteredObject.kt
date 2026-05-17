package net.typho.big_shot_lib.api.util

import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import kotlin.reflect.KProperty

interface RegisteredObject<T : Any> {
    val registry: ResourceKey<out Registry<T>>
    val key: Identifier
    val value: T

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = value

    operator fun invoke() = value

    fun isRegistered(): Boolean
}