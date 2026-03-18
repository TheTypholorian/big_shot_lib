package net.typho.big_shot_lib.api.util.resource

import com.mojang.datafixers.util.Either
import com.mojang.datafixers.util.Pair
import com.mojang.datafixers.util.Unit
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.Lifecycle
import net.typho.big_shot_lib.api.BigShotApi
import net.typho.big_shot_lib.api.util.WrapperUtil
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
            return WrapperUtil.INSTANCE.dataResultError { "List is too short: " + size + ", expected range [" + this.minSize + "-" + this.maxSize + "]" }
        }

        private fun <R> createTooLongError(size: Int): DataResult<R> {
            return WrapperUtil.INSTANCE.dataResultError { "List is too long: " + size + ", expected range [" + this.minSize + "-" + this.maxSize + "]" }
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