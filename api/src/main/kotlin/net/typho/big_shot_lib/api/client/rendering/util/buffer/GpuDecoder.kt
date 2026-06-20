package net.typho.big_shot_lib.api.client.rendering.util.buffer

import net.typho.big_shot_lib.api.util.buffer.NativeDataInput

interface GpuDecoder<P : GpuPacking, A> {
    fun decode(input: NativeDataInput): A
}