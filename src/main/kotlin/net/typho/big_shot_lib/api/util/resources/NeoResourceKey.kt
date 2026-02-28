package net.typho.big_shot_lib.api.util.resources

import com.mojang.serialization.Codec
import net.minecraft.core.Registry

@JvmRecord
data class NeoResourceKey<T>(
    @JvmField
    val registry: ResourceIdentifier,
    @JvmField
    val location: ResourceIdentifier
) {
    companion object {
        @JvmStatic
        fun <T : Any> registry(location: ResourceIdentifier): NeoResourceKey<Registry<T>> {
            return NeoResourceKey(ResourceIdentifier("root"), location)
        }

        @JvmStatic
        fun <T> codec(registry: NeoResourceKey<out Registry<T>>): Codec<NeoResourceKey<T>> {
            return ResourceIdentifier.CODEC.xmap(
                { NeoResourceKey(registry.location, it) },
                { it.location }
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <E> cast(key: NeoResourceKey<Registry<E>>): NeoResourceKey<E>? {
        return if (registry == key.location) this as NeoResourceKey<E> else null
    }
}
