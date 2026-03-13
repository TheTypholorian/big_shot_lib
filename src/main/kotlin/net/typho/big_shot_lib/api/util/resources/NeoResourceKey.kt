package net.typho.big_shot_lib.api.util.resources

import com.mojang.serialization.Codec
import net.minecraft.core.Registry

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
    }

    @Suppress("UNCHECKED_CAST")
    fun <E> cast(key: NeoResourceKey<Registry<E>>): NeoResourceKey<E>? {
        return if (registry == key.location) this as NeoResourceKey<E> else null
    }
}
