package net.typho.big_shot_lib.api.util.resource

import com.mojang.serialization.Codec
import net.minecraft.client.Minecraft
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.typho.big_shot_lib.api.util.NeoRegistry
import net.typho.big_shot_lib.api.util.WrapperUtil

@JvmRecord
data class NeoResourceKey<T>(
    @JvmField
    val registry: NeoIdentifier,
    @JvmField
    val location: NeoIdentifier
) {
    companion object {
        @JvmStatic
        fun <T : Any> registry(location: NeoIdentifier): NeoResourceKey<Registry<T>> {
            return NeoResourceKey(NeoIdentifier("root"), location)
        }

        @JvmStatic
        fun <T : Any> codec(registry: NeoResourceKey<out Registry<T>>): Codec<NeoResourceKey<T>> {
            return NeoIdentifier.CODEC.xmap(
                { NeoResourceKey(registry.location, it) },
                { it.location }
            )
        }

        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun <T : Any> NeoResourceKey<out Registry<T>>.lookup() = NeoRegistry.REGISTRY.get(location)?.let { WrapperUtil.INSTANCE.wrap(it) as NeoRegistry<T> }

        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun <T : Any> NeoResourceKey<out Registry<T>>.lookupOrThrow(error: String = "Couldn't find registry $location") = NeoRegistry.REGISTRY.get(location)?.let { WrapperUtil.INSTANCE.wrap(it) as NeoRegistry<T> } ?: throw NullPointerException(error)
    }

    @Suppress("UNCHECKED_CAST")
    fun <E : Any> cast(key: NeoResourceKey<Registry<E>>): NeoResourceKey<E>? {
        return if (registry == key.location) this as NeoResourceKey<E> else null
    }
}
