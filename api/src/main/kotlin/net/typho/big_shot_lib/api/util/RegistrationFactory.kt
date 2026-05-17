package net.typho.big_shot_lib.api.util

import net.minecraft.core.Registry
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

interface RegistrationFactory {
    fun <T : Any> begin(key: ResourceKey<Registry<T>>): RegistrationConsumer<T, Identifier>?

    fun <T : Any> begin(key: ResourceKey<Registry<T>>, namespace: String): RegistrationConsumer<T, String>?

    fun beginBlocks(): RegistrationConsumer.Blocks<Identifier>?

    fun beginBlocks(namespace: String): RegistrationConsumer.Blocks<String>?

    fun beginItems(): RegistrationConsumer.Items<Identifier>?

    fun beginItems(namespace: String): RegistrationConsumer.Items<String>?
}