package net.typho.big_shot_lib.api.util.resource

import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Pair
import com.mojang.datafixers.util.Unit
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.MapCodec
import com.mojang.serialization.MapLike
import com.mojang.serialization.RecordBuilder
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.InternalUtil
import java.util.Optional
import java.util.function.Consumer
import java.util.function.Function
import java.util.stream.Stream

object NeoCodecs {
    @JvmStatic
    fun <E> listOf(codec: Codec<E>, min: Int, max: Int): Codec<List<E>> {
        return ListCodec(codec, min, max)
    }

    @JvmStatic
    fun <V, T> createList(size: Int, codec: Codec<T>, to: (list: List<T>) -> V, from: (value: V) -> List<T>): Codec<V> {
        return listOf(codec, size, size).xmap(to, from)
    }

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
                    } ?: InternalUtil.INSTANCE.dataResultError { "Could not find key $valueKey injected by $injector via big shot lib in $input" }
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

    @JvmRecord
    data class ListCodec<E>(
        @JvmField
        val elementCodec: Codec<E>,
        @JvmField
        val minSize: Int,
        @JvmField
        val maxSize: Int
    ) : Codec<List<E>> {
        private fun <R> createTooShortError(size: Int): DataResult<R> {
            return InternalUtil.INSTANCE.dataResultError { "List is too short: " + size + ", expected range [" + this.minSize + "-" + this.maxSize + "]" }
        }

        private fun <R> createTooLongError(size: Int): DataResult<R> {
            return InternalUtil.INSTANCE.dataResultError { "List is too long: " + size + ", expected range [" + this.minSize + "-" + this.maxSize + "]" }
        }

        override fun <T> encode(
            input: List<E>,
            ops: DynamicOps<T>,
            prefix: T
        ): DataResult<T> {
            if (input.size < this.minSize) {
                return createTooShortError(input.size)
            } else if (input.size > this.maxSize) {
                return createTooLongError(input.size)
            } else {
                val builder = ops.listBuilder()

                for (element in input) {
                    builder.add(elementCodec.encodeStart<T?>(ops, element))
                }

                return builder.build(prefix)
            }
        }

        override fun <T> decode(
            ops: DynamicOps<T>,
            input: T
        ): DataResult<Pair<List<E>, T>> {
            return ops.getList(input).setLifecycle(Lifecycle.stable())
                .flatMap(Function { stream ->
                    val decoder = DecoderState(ops)
                    stream!!.accept(Consumer { value -> decoder.accept(value) })
                    decoder.build()
                })
        }

        private inner class DecoderState<T>(
            private val ops: DynamicOps<T>
        ) {
            private val elements = arrayListOf<E>()
            private val failed = Stream.builder<T>()
            private var result: DataResult<Unit?> = DataResult.success(Unit.INSTANCE, Lifecycle.stable())
            private var totalCount = 0

            fun accept(value: T) {
                totalCount++

                if (elements.size >= maxSize) {
                    failed.add(value)
                } else {
                    val elementResult = elementCodec.decode(ops, value)
                    elementResult.error()
                        .ifPresent(Consumer { error -> failed.add(value) })
                    elementResult.resultOrPartial(BigShotApi.LOGGER::warn)
                        .ifPresent(Consumer { pair -> elements.add(pair.getFirst()) })
                    result = result.apply2stable(
                        { result, element -> result },
                        elementResult
                    )
                }
            }

            fun build(): DataResult<Pair<List<E>, T>> {
                if (elements.size < minSize) {
                    return createTooShortError(elements.size)
                } else {
                    val errors = ops.createList(failed.build())
                    val pair = Pair.of(elements as List<E>, errors)
                    if (totalCount > maxSize) {
                        result = createTooLongError(totalCount)
                    }

                    return result.map(Function { ignored: Unit? -> pair })
                        .setPartial(pair)
                }
            }
        }
    }
}