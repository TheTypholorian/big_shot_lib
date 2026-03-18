package net.typho.big_shot_lib.api.util.resource

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec

object NeoCodecs {
    @JvmStatic
    fun <V, T> createList(size: Int, codec: Codec<T>, to: (list: List<T>) -> V, from: (value: V) -> List<T>): Codec<V> {
        return codec.listOf(size, size).xmap(to, from)
    }

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <I, A : I, B : I> either(first: Codec<A>, second: Codec<B>): Codec<out I> {
        return Codec.either(first, second).xmap(
            { either -> either.map({ l -> l }, { r -> r }) },
            { value -> (value as? A).let { Either.left(it) } ?: Either.right(value as B) }
        )
    }

    @JvmStatic
    fun <I> either(first: Codec<I>, second: Codec<I>): Codec<I> {
        return Codec.either(first, second).xmap(
            { either -> either.map({ l -> l }, { r -> r }) },
            { value -> Either.left(value) }
        )
    }

    @JvmStatic
    fun <I> any(first: Codec<out I>, vararg codecs: Codec<out I>): Codec<out I> {
        return codecs.fold(first) { accum, codec -> either(accum, codec) }
    }

    @JvmStatic
    fun <I> any(first: Codec<I>, vararg codecs: Codec<I>): Codec<I> {
        return codecs.fold(first) { accum, codec -> either(accum, codec) }
    }

    @JvmStatic
    inline fun <reified E : Enum<E>> enumCodec(): Codec<E> {
        return Codec.STRING.xmap(
            { key -> enumValueOf<E>(key) },
            { entry -> entry.name }
        )
    }
}