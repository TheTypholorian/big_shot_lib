package net.typho.big_shot_lib.api.client.rendering.util.buffer

import net.typho.big_shot_lib.api.util.buffer.NativeBuffer
import net.typho.big_shot_lib.api.util.buffer.NativeDataOutput

interface DynamicGpuEncoder<P : GpuPacking, A> {
    val numPaddingBytes: Long

    fun sizeOf(value: A): Long

    fun alignmentOf(value: A): Long

    fun encode(value: A, output: NativeDataOutput)

    fun encode(value: A): NativeBuffer.Raw {
        val buffer = NativeBuffer.Raw(sizeOf(value))
        encode(value, buffer.write())
        return buffer
    }
}