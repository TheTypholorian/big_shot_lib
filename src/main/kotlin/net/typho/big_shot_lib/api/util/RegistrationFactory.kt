package net.typho.big_shot_lib.api.util

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.typho.big_shot_lib.api.util.resource.NeoIdentifier
import net.typho.big_shot_lib.api.util.resource.NeoResourceKey

interface RegistrationFactory {
    fun <T : Any> begin(key: ResourceKey<Registry<T>>): RegistrationConsumer<T, NeoIdentifier>

    fun <T : Any> begin(key: ResourceKey<Registry<T>>, namespace: String): RegistrationConsumer<T, String>

    fun <T : Any> begin(key: NeoResourceKey<Registry<T>>): RegistrationConsumer<T, NeoIdentifier>

    fun <T : Any> begin(key: NeoResourceKey<Registry<T>>, namespace: String): RegistrationConsumer<T, String>

    fun beginBlocks(): RegistrationConsumer.Blocks<NeoIdentifier>

    fun beginBlocks(namespace: String): RegistrationConsumer.Blocks<String>

    fun beginItems(): RegistrationConsumer.Items<NeoIdentifier>

    fun beginItems(namespace: String): RegistrationConsumer.Items<String>
}