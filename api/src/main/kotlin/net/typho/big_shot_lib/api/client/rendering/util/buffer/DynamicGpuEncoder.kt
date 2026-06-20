package net.typho.big_shot_lib.api.client.rendering.util.buffer

import net.typho.big_shot_lib.api.util.buffer.MemoryPointer
import net.typho.big_shot_lib.api.util.buffer.MemoryWriter

interface DynamicGpuEncoder<P : GpuPacking, A> {
    val numPaddingBytes: Long

    fun sizeOf(value: A): Long

    fun alignmentOf(value: A): Long

    fun encode(value: A, output: MemoryWriter)

    fun encode(value: A): MemoryPointer.Native {
        val parent = toString()
        val buffer = MemoryPointer.alloc(sizeOf(value)) { "Encoded GPU Buffer from $parent" }
        encode(value, buffer.write())
        return buffer
    }
}