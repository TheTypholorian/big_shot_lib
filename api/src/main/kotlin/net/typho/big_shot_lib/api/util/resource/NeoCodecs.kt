package net.typho.big_shot_lib.api.util.resource

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.MapCodec
import com.mojang.serialization.MapLike
import com.mojang.serialization.RecordBuilder
import net.typho.big_shot_lib.api.util.Extension
import net.typho.big_shot_lib.api.util.Extension.Companion.castTo
import java.util.Optional
import java.util.stream.Stream

object NeoCodecs {
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
    fun <H, V> inject(
        injector: String,
        holderCodec: MapCodec<H>,
        valueCodec: Codec<V>,
        valueKey: String,
        get: (holder: H) -> V,
        set: (holder: H, value: V) -> H
    ): MapCodec<H> {
        return object : MapCodec<H>() {
            override fun <T> keys(ops: DynamicOps<T>): Stream<T> {
                val list = ArrayList(holderCodec.keys(ops).toList())
                list.add(ops.createString(valueKey))
                return list.stream()
            }

            override fun <T> decode(
                ops: DynamicOps<T>,
                input: MapLike<T>
            ): DataResult<H> {
                return holderCodec.decode(ops, input).flatMap { holder ->
                    input.get(valueKey)?.let { value ->
                        valueCodec.decode(ops, value).map { decoded ->
                            set(holder, decoded.first)
                        }.mapError { error -> "Error decoding $valueKey injected by $injector via big shot lib: $error" }
                    } ?: DataResult.error { "Could not find key $valueKey injected by $injector via big shot lib in $input" }
                }
            }

            override fun <T> encode(
                input: H,
                ops: DynamicOps<T>,
                prefix: RecordBuilder<T>
            ): RecordBuilder<T> {
                val builder = holderCodec.encode(input, ops, prefix)
                builder.add(valueKey, valueCodec.encodeStart(ops, get(input)).mapError { error -> "Error encoding $valueKey injected by $injector via big shot lib: $error" })
                return builder
            }
        }
    }

    @JvmStatic
    fun <H, V : Any> injectOptional(
        injector: String,
        holderCodec: MapCodec<H>,
        valueCodec: Codec<V>,
        valueKey: String,
        get: (holder: H) -> Optional<V>,
        set: (holder: H, value: Optional<V>) -> H
    ): MapCodec<H> {
        return object : MapCodec<H>() {
            override fun <T> keys(ops: DynamicOps<T>): Stream<T> {
                val list = ArrayList(holderCodec.keys(ops).toList())
                list.add(ops.createString(valueKey))
                return list.stream()
            }

            override fun <T> decode(
                ops: DynamicOps<T>,
                input: MapLike<T>
            ): DataResult<H> {
                return holderCodec.decode(ops, input).flatMap { holder ->
                    input.get(valueKey)?.let { value ->
                        valueCodec.decode(ops, value).map { decoded ->
                            set(holder, Optional.ofNullable(decoded.first))
                        }.mapError { error -> "Error decoding $valueKey injected by $injector via big shot lib: $error" }
                    } ?: DataResult.success(set(holder, Optional.empty()))
                }
            }

            override fun <T> encode(
                input: H,
                ops: DynamicOps<T>,
                prefix: RecordBuilder<T>
            ): RecordBuilder<T> {
                val builder = holderCodec.encode(input, ops, prefix)
                get(input).ifPresent { builder.add(valueKey, valueCodec.encodeStart(ops, it).mapError { error -> "Error encoding $valueKey injected by $injector via big shot lib: $error" }) }
                return builder
            }
        }
    }
}