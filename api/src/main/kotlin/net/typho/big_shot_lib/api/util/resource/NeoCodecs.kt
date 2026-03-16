package net.typho.big_shot_lib.api.util.resource

import com.mojang.serialization.Codec

object NeoCodecs {
    @JvmStatic
    fun <V, T> createList(size: Int, codec: Codec<T>, to: (list: List<T>) -> V, from: (value: V) -> List<T>): Codec<V> {
        return codec.listOf(size, size).xmap(to, from)
    }

    @JvmStatic
    inline fun <reified E : Enum<E>> enumCodec(): Codec<E> {
        return Codec.STRING.xmap(
            { key -> enumValueOf<E>(key) },
            { entry -> entry.name }
        )
    }
}