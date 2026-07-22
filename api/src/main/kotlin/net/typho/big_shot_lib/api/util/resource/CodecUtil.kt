package net.typho.big_shot_lib.api.util.resource

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.MapCodec
import com.mojang.serialization.MapLike
import com.mojang.serialization.RecordBuilder
import java.util.stream.Stream

object CodecUtil {
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <I, A : I, B : I> eitherSubclass(first: Codec<A>, second: Codec<B>): Codec<out I> {
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
    fun <I> anySubclass(first: Codec<out I>, vararg codecs: Codec<out I>): Codec<out I> {
        return codecs.fold(first) { accum, codec -> eitherSubclass(accum, codec) }
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

    @JvmStatic
    fun <H, V> MapCodec<H>.inject(
        valueCodec: MapCodec<V>,
        get: (holder: H) -> V,
        set: (holder: H, value: V) -> H
    ): MapCodec<H> {
        val holderCodec = this
        return object : MapCodec<H>() {
            override fun <T> keys(ops: DynamicOps<T>): Stream<T> {
                return Stream.concat(holderCodec.keys(ops), valueCodec.keys(ops))
            }

            override fun <T> decode(
                ops: DynamicOps<T>,
                input: MapLike<T>
            ): DataResult<H> {
                return holderCodec.decode(ops, input).flatMap { holder ->
                    valueCodec.decode(ops, input).map { value ->
                        set(holder, value)
                    }
                }
            }

            override fun <T> encode(
                input: H,
                ops: DynamicOps<T>,
                prefix: RecordBuilder<T>
            ): RecordBuilder<T> {
                return valueCodec.encode(get(input), ops, holderCodec.encode(input, ops, prefix))
            }
        }
    }
}