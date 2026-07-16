package net.typho.big_shot_lib.client.api.rendering.codec

import net.typho.big_shot_lib.api.util.buffer.MemoryReader
import net.typho.big_shot_lib.api.util.buffer.MemoryWriter

interface GpuPacking {
    fun structBuilder(): StructBuilder

    fun <A> encode(codec: GpuCodec<*, A>, value: A, padding: Long, output: MemoryWriter) {
        codec.encode(value, output)
        output.skip(padding)
    }

    fun <A> decode(codec: GpuCodec<*, A>, padding: Long, input: MemoryReader): A {
        val v = codec.decode(input)
        input.skip(padding)
        return v
    }

    interface StructBuilder {
        fun add(component: GpuCodec<*, *>): StructBuilder

        /**
         * @return The number of padding bytes after writing the component
         */
        operator fun get(componentIndex: Int): Long

        /**
         * @return The alignment of the struct
         */
        fun end(): Long

        fun <A> end(last: DynamicGpuCodec<*, A>): (A) -> Long
    }
}