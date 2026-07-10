package net.typho.big_shot_lib.api.client.rendering.codec

import net.typho.big_shot_lib.api.util.buffer.MemoryReader

interface GpuDecoder<P : GpuPacking, A> {
    fun decode(input: MemoryReader): A
}