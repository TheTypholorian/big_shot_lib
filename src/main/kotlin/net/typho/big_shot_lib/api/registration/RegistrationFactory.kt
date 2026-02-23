package net.typho.big_shot_lib.api.registration

import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.typho.big_shot_lib.api.util.resources.NeoResourceKey

interface RegistrationFactory {
    fun <T : Any> begin(key: ResourceKey<Registry<T>>, namespace: String): RegistrationConsumer<T>

    fun <T : Any> begin(key: NeoResourceKey<Registry<T>>, namespace: String): RegistrationConsumer<T>

    fun beginBlocks(namespace: String): RegistrationConsumer.Blocks

    fun beginItems(namespace: String): RegistrationConsumer.Items
}