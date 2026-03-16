package net.typho.big_shot_lib.api.util.resource

import com.mojang.serialization.Codec
import net.minecraft.core.Registry

@JvmRecord
data class NeoTagKey<T>(
    @JvmField
    val registry: NeoIdentifier,
    @JvmField
    val location: NeoIdentifier
) {
    companion object {
        @JvmStatic
        fun <T : Any> registry(location: NeoIdentifier): NeoTagKey<Registry<T>> {
            return NeoTagKey(NeoIdentifier("root"), location)
        }

        @JvmStatic
        fun <T : Any> codec(registry: NeoTagKey<out Registry<T>>): Codec<NeoTagKey<T>> {
            return NeoIdentifier.CODEC.xmap(
                { NeoTagKey(registry.location, it) },
                { it.location }
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <E : Any> cast(key: NeoTagKey<Registry<E>>): NeoTagKey<E>? {
        return if (registry == key.location) this as NeoTagKey<E> else null
    }
}
