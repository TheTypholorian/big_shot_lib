package net.typho.big_shot_lib.api.util.resources

import com.mojang.serialization.Codec
import net.minecraft.core.Registry

@JvmRecord
data class NeoTagKey<T>(
    @JvmField
    val registry: ResourceIdentifier,
    @JvmField
    val location: ResourceIdentifier
) {
    companion object {
        @JvmStatic
        fun <T : Any> registry(location: ResourceIdentifier): NeoTagKey<Registry<T>> {
            return NeoTagKey(ResourceIdentifier("root"), location)
        }

        @JvmStatic
        fun <T : Any> codec(registry: NeoTagKey<out Registry<T>>): Codec<NeoTagKey<T>> {
            return ResourceIdentifier.CODEC.xmap(
                { NeoTagKey(registry.location, it) },
                { it.location }
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <E> cast(key: NeoTagKey<Registry<E>>): NeoTagKey<E>? {
        return if (registry == key.location) this as NeoTagKey<E> else null
    }
}
