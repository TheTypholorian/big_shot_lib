package net.typho.big_shot_lib.api.event

import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.typho.big_shot_lib.api.InternalUtil

interface RegistryBuilder<T> {
    var sync: Boolean
    var defaultKey: Identifier?

    companion object {
        @JvmStatic
        @JvmName("create")
        operator fun <T> invoke(key: ResourceKey<Registry<T>>) = InternalUtil.INSTANCE.createRegistryBuilder(key)
    }
}